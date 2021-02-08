package com.leyou;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.client.ItemClient;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.SpuDTO;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.SearchRepository;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cbh
 * @PackageName:com.leyou
 * @ClassName:ItemClientTest
 * @Description:
 * @date 2021-02-03 21:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ItemClientTest {
	@Autowired
	private ItemClient itemClient;

	@Autowired
	private SearchService searchService;

	@Autowired
	private SearchRepository searchRepository;

	@Test
	public void test1(){
		List<Sku> skus = itemClient.findSkusBySpuId(3L);
		skus.forEach(System.out::println);
	}

	@Test
	public void test2(){
		SpuDetail spuDetail = itemClient.findSpuDetailBySpuId(3L);
		System.out.println(spuDetail);
	}

	@Test
	public void test3(){
		List<SpecParam> specParams = itemClient.findSpecParams(1L, 2L, true);
		specParams.forEach(System.out::println);
	}

	@Test
	public void test4(){
		PageResult<SpuDTO> xxx = itemClient.spuPageQuery(1, 2, "xxx", true);
		System.out.println(xxx);
	}

	@Test
	public void test5(){
		//1.分页导入
		int page = 1;
		int rows = 100;
		//总页数
		int totalPage = 1;

		while (page <= totalPage){
			//1.调用微服务只导入上架的商品
			PageResult<SpuDTO> SpuDTOPageResult = itemClient.spuPageQuery(page, rows, null, true);

			//2.获取spu列表集合
			List<SpuDTO> SpuDTOList = SpuDTOPageResult.getItems();

			if (CollectionUtils.isNotEmpty(SpuDTOList)){
				List<Goods> goodsList = SpuDTOList.stream().map(SpuDTO -> buildGoods(SpuDTO)).collect(Collectors.toList());
			}
			totalPage = SpuDTOPageResult.getTotalPage().intValue();

			//下一页
			page++;
		}
	}

	@Test
	public void indexWrite(){
		searchService.importData();
	}

	public Goods buildGoods(SpuDTO SpuDTO){
		Goods goods = new Goods();
		BeanUtils.copyProperties(SpuDTO, goods);
		System.out.println(goods);
		return goods;
	}

	@Test
	public void test6(){
		SpecParam specParam = new SpecParam();
		specParam.setSegments("0-1.0,1.0-1.5,1.5-2.0,2.0-2.5,2.5-");
		chooseSegment(1.5, specParam);
	}

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
}
