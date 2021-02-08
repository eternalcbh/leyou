package com.leyou.item.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.utils.BeanHelper;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cbh
 * @PackageName:com.leyou.item.service
 * @ClassName:GoodsService
 * @Description:商品的业务层
 * @date 2021-01-30 21:40
 */
@Service
@Transactional
public class GoodsService extends ServiceImpl<SkuMapper, Sku> {
	@Autowired(required = false)
	private SpuMapper spuMapper;

	@Autowired(required = false)
	private SpuDetailMapper spuDetailMapper;

	@Autowired(required = false)
	private SkuMapper skuMapper;

	@Autowired(required = false)
	private BrandService brandService;

	@Autowired(required = false)
	private CategoryService categoryService;

	public PageResult<SpuDTO> spuPageQuery(Integer page, Integer rows, String key, Boolean saleable) {
		QueryWrapper<Spu> query = Wrappers.query();

		//1.因为and的优先级较高，所以要拼接字符串
		if (StringUtils.isNotEmpty(key)) {
			query.and(q -> q.like("name", key).or().like("sub_title", key));
		}

		//2.上下架
		if (null != saleable) {
			query.eq("saleable", saleable);
		}

		//3.拼接分页查询条件
		IPage<Spu> iPage = new Page(page, rows);

		//4.拿出数据
		iPage = spuMapper.selectPage(iPage, query);

		List<SpuDTO> spuDTOList = BeanHelper.copyWithCollection(iPage.getRecords(), SpuDTO.class);

		getCategoryNameAndBrandName(spuDTOList);

		return new PageResult<>(iPage.getTotal(), iPage.getPages(), spuDTOList);
	}

	private void getCategoryNameAndBrandName(List<SpuDTO> spuDTOList) {
		for (SpuDTO spuDTO : spuDTOList) {
			Brand brand = brandService.findBrandById(spuDTO.getBrandId());
			spuDTO.setBrandName(brand.getName());

			//2.查询分类列表
			List<Long> list = Arrays.asList(spuDTO.getCid1(), spuDTO.getCid2(), spuDTO.getCid3());
			List<Category> categories = categoryService.findCategoriesByIds(list);

			String categoryName = categories.stream().map(Category::getName).collect(Collectors.joining("/"));

			spuDTO.setCategoryName(categoryName);
		}
	}

	public void saveGoods(SpuDTO spuDTO) {
		try {
			Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
			//设置下架信息
			spu.setSaleable(false);
			//保存
			spuMapper.insert(spu);

			//存入spuDetail
			SpuDetail spuDetail = spuDTO.getSpuDetail();
			//设置SpuId
			spuDetail.setSpuId(spu.getId());
			spuDetailMapper.insert(spuDetail);

			//保存Lisk<stu>对象
			List<Sku> skuList = spuDTO.getSkus();

			//给skuList设置SpuId
			skuList.forEach(sku -> {
				sku.setSpuId(spu.getId());
				skuMapper.insert(sku);
			});

			//批量增加
//			this.saveBatch(skuList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
		}

	}

	public void updateSaleable(Long id, Boolean saleable) {
		try {
			Spu spu = new Spu();
			spu.setId(id);
			spu.setSaleable(saleable);
			spuMapper.updateById(spu);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
		}
	}

	/**
	 * 根据spuId查找sku集合
	 * @param id
	 * @return
	 */
	public List<Sku> findSkusBySpuId(Long id) {
		Sku sku = new Sku();
		sku.setSpuId(id);
		QueryWrapper<Sku> query = Wrappers.query(sku);
		List<Sku> skus = skuMapper.selectList(query);

		if (CollectionUtils.isEmpty(skus)) {
			throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
		}
		return skus;
	}


	public SpuDetail findSpuDetailBySpuId(Long id) {
		SpuDetail spuDetail = spuDetailMapper.selectById(id);
		if (null != spuDetail){
			return spuDetail;
		}
		throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
	}
}
