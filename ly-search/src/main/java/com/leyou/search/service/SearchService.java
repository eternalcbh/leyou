package com.leyou.search.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.utils.HighlightUtils;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.client.ItemClient;
import com.leyou.item.pojo.*;
import com.leyou.search.dto.GoodsDTO;
import com.leyou.search.dto.SearchRequest;
import com.leyou.search.dto.SearchResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.SearchRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cbh
 * @PackageName:com.leyou.search.service
 * @ClassName:SearchService
 * @Description: 搜索业务
 * @date 2021-02-03 21:00
 */
@Service
public class SearchService {
	/**
	 * 基本搜索注册
	 */
	@Autowired
	private SearchRepository searchRepository;

	/**
	 * 高级搜索注册
	 */
	@Autowired
	private ElasticsearchTemplate esTemplate;

	@Autowired
	private ItemClient itemClient;

	/**
	 * 从MySql中导入数据到es的方法
	 */
	public void importData() {
		//1.分页导入
		int page = 1;
		int rows = 100;
		//总页数
		long totalPage = 1;

		while (page <= totalPage) {
			//1.调用微服务只导入上架的商品
			PageResult<SpuDTO> spuDTOPageResult = itemClient.spuPageQuery(page, rows, null, true);

			//2.获取spu列表集合
			List<SpuDTO> spuDTOList = spuDTOPageResult.getItems();

			if (CollectionUtils.isNotEmpty(spuDTOList)) {
				List<Goods> collect = spuDTOList.stream().map(spuDTO -> buildGoods(spuDTO)).collect(Collectors.toList());

				searchRepository.saveAll(collect);
			}

			totalPage = spuDTOPageResult.getTotalPage();

			//下一页
			page++;
		}
	}

	/**
	 * 将SpuDTO转为Goods
	 */
	public Goods buildGoods(SpuDTO spuDTO) {
		Goods goods = new Goods();
		goods.setId(spuDTO.getId());
		goods.setSpuName(spuDTO.getName());
		goods.setSubTitle(spuDTO.getSubTitle());
		goods.setCreateTime(System.currentTimeMillis());
		goods.setBrandId(spuDTO.getBrandId());
		goods.setCategoryId(spuDTO.getCid3());

		//设置Price
		List<Sku> skus = itemClient.findSkusBySpuId(spuDTO.getId());

		//使用List存储每个sku的数据
		List<Map<String, Object>> skuMapList = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(skus)) {
			skus.forEach(sku -> {
				HashMap<String, Object> skuMap = new HashMap<>();
				skuMap.put("id", sku.getId());
				skuMap.put("images", sku.getImages());
				skuMap.put("price", sku.getPrice());
				skuMapList.add(skuMap);
			});
			goods.setSkus(JsonUtils.toString(skuMapList));

			//封装需要的sku的值
			Set<Long> price = skus.stream().map(Sku::getPrice).collect(Collectors.toSet());
			//设置price
			goods.setPrice(price);

			//所有需要被搜索的信息，包含三个分类名称+品牌名称+spuName+subTitle+所有Sku的title
			String all = new StringBuilder().append(spuDTO.getCategoryName()).append(" ").append(spuDTO.getBrandName()).
					append(" ").append(spuDTO.getName()).append(" ").append(spuDTO.getSubTitle()).append(" ").
					append(skus.stream().map(Sku::getTitle).collect(Collectors.joining(" "))).toString();

			//设置All
			goods.setAll(all);

			//设置动态过滤
			//1.找出参数
			List<SpecParam> specParams = itemClient.findSpecParams(null, spuDTO.getCid3(), true);

			SpuDetail spuDetail = itemClient.findSpuDetailBySpuId(spuDTO.getId());

			//4.参数集合Map
			HashMap<String, Object> specs = new HashMap<>();

			if (CollectionUtils.isNotEmpty(specParams)) {

				//2.通用参数
				String genericSpec = spuDetail.getGenericSpec();

				//3.特殊参数
				String specialSpec = spuDetail.getSpecialSpec();

				specParams.forEach(specParam -> {
					Object value = null;

					if (specParam.getGeneric()) {
						//4.通用参数
						Map<Long, Object> genericMap = JsonUtils.toMap(genericSpec, Long.class, Object.class);

						//获取通用参数的值
						value = genericMap.get(specParam.getId());
					} else {
						//5.特殊参数
						Map<Long, List<Object>> specialMap = JsonUtils.nativeRead(specialSpec, new TypeReference<Map<Long, List<Object>>>() {
						});
						//特殊参数的值
						value = specialMap.get(specParam.getId());
					}

					if (specParam.getNumeric()) {
						value = chooseSegment(value, specParam);
					}

					specs.put(specParam.getName(), value);
				});
				//存入商品中
				goods.setSpecs(specs);
			}
		}
		return goods;
	}

	/**
	 * 将数子类型的参数转换为区间范围的方法
	 *
	 * @param value
	 * @param p
	 * @return
	 */
	private String chooseSegment(Object value, SpecParam p) {
		if (value == null || StringUtils.isBlank(value.toString())) {
			return "其它";
		}
		double val = parseDouble(value.toString());
		String result = "其它";
		// 保存数值段
		for (String segment : p.getSegments().split(",")) {
			String[] segs = segment.split("-");
			// 获取数值范围
			double begin = parseDouble(segs[0]);
			double end = Double.MAX_VALUE;
			if (segs.length == 2) {
				end = parseDouble(segs[1]);
			}
			// 判断是否在范围内
			if (val >= begin && val < end) {
				if (segs.length == 1) {
					result = segs[0] + p.getUnit() + "以上";
				} else if (begin == 0) {
					result = segs[1] + p.getUnit() + "以下";
				} else {
					result = segment + p.getUnit();
				}
				break;
			}
		}
		return result;
	}

	private double parseDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public SearchResult<GoodsDTO> goodsSearchPage(SearchRequest searchRequest) {
		//1.创建SearchResult<GoodsDTO>对象
		SearchResult<GoodsDTO> searchResult = new SearchResult<>();

		//2.封装SearchResult<GoodDTO>对象
		//2.1查询商品分页列表
		PageResult<GoodsDTO> pageResult = itemQueryPage(searchRequest);

		//2.2商品搜索过滤条件
		Map<String, Object> filterConditions = filterConditionsQuery(searchRequest);

		searchResult.setItems(pageResult.getItems());
		searchResult.setTotalPage(pageResult.getTotalPage());
		searchResult.setTotal(pageResult.getTotal());
		searchResult.setFilterConditions(filterConditions);
		return searchResult;
	}
	/**
	 * 查询搜索的过滤条件
	 * @param searchRequest
	 * @return
	 */
	private Map<String, Object> filterConditionsQuery(SearchRequest searchRequest) {
		//1.创建Map集合存储所有过滤条件
		LinkedHashMap<String, Object> filterConditionsMap = new LinkedHashMap<>();

		//2.封装Map集合所有的过滤条件
		NativeSearchQueryBuilder queryBuilder = createNativeQueryBuilder(searchRequest);

		//3.添加结果过滤
		queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""},null));

		//4.追加聚合条件
		String categoryAgg = "categoryAgg";
		String brandAgg = "brandAgg";

		//执行聚合查询
		queryBuilder.addAggregation(AggregationBuilders.terms(categoryAgg).field("categoryId"));
		queryBuilder.addAggregation(AggregationBuilders.terms(brandAgg).field("brandId"));

		//执行聚合查询
		AggregatedPage<Goods> aggregatedPage = esTemplate.queryForPage(queryBuilder.build(), Goods.class);

		//获取集合
		Aggregations aggregations = aggregatedPage.getAggregations();

		Terms terms = aggregations.get(categoryAgg);


		//获取分类的ids
		List<Long> categoryIds = terms.getBuckets().stream()
				.map(Terms.Bucket::getKeyAsNumber)
				.map(Number::longValue)
				.collect(Collectors.toList());

		List<Category> categories = itemClient.findCategoriesByIds(categoryIds);

		filterConditionsMap.put("分类", categories);

		Terms brandTerm = aggregations.get(brandAgg);

		List<Long> brandIds = brandTerm.getBuckets().stream()
				.map(Terms.Bucket::getKeyAsNumber)
				.map(Number::longValue)
				.collect(Collectors.toList());

		List<Brand> brandList = itemClient.findBrandsByIds(brandIds);

		filterConditionsMap.put("品牌", brandList);

		categoryIds.forEach(categoryId ->{
			List<SpecParam> specParams = itemClient.findSpecParams(null, categoryId, true);
			specParams.forEach(specParam -> {
				queryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
			});
			//执行聚合查询
			Aggregations specParamAggregations = esTemplate.queryForPage(queryBuilder.build(), Goods.class).getAggregations();
			specParams.forEach(specParam -> {
				Terms specParamTerms = specParamAggregations.get(specParam.getName());
				List<Object> specParamList = specParamTerms.getBuckets().stream().map(Terms.Bucket::getKey).collect(Collectors.toList());
				filterConditionsMap.put(specParam.getName(), specParamList);
			});
		});

		return filterConditionsMap;
	}

	/**
	 * 查询商品分页列表结果
	 *
	 * @param searchRequest
	 * @return
	 */
	private PageResult<GoodsDTO> itemQueryPage(SearchRequest searchRequest) {
		//1.本地查找器
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

		//2.Bool过滤器
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		//3.往布尔组合查询构造器添加条件
		boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchRequest.getKey(), "all", "spuName"));

		//4.把布尔组合构造器，添加到本地查询器
		queryBuilder.withQuery(boolQueryBuilder);

		//查询
		queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "spuName", "subTitle", "skus"}, null));

		//获取集合
		queryBuilder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));

		HighlightUtils.highlightField(queryBuilder, "spuName");

		//集合拷贝
		AggregatedPage<Goods> pageBean = esTemplate.queryForPage(queryBuilder.build(), Goods.class, HighlightUtils.highlightBody(Goods.class, "spuName"));

		List<Goods> goodsList = pageBean.getContent();

		List<GoodsDTO> goodsDTOList = BeanHelper.copyWithCollection(goodsList, GoodsDTO.class);

		PageResult<GoodsDTO> pageResult = new PageResult<>(
				pageBean.getTotalElements(),
				Long.valueOf(pageBean.getTotalPages()),
				goodsDTOList
		);

		return pageResult;
	}

	public NativeSearchQueryBuilder createNativeQueryBuilder(SearchRequest searchRequest){
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

		//布尔过滤器
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchRequest.getKey(), "all", "spuName"));

		queryBuilder.withQuery(boolQueryBuilder);
		return queryBuilder;
	}
}
