package com.leyou.item.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyou.common.exception.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.service
 * @ClassName:BrandService
 * @Description:
 * @date 2021-01-28 12:06
 */
@Service
@Transactional
public class BrandService {

	@Autowired(required = false)
	private BrandMapper brandMapper;

	public PageResult<Brand> brandPageQuery(Integer page, Integer rows, String key, String sortBy, Boolean desc) {
		//1.封装查询条件
		IPage<Brand> iPage = new Page<>(page, rows);

		//1.2wrapper 查询条件
		QueryWrapper<Brand> wrapper = Wrappers.query();

		//1.3把需要的条件封装进去QueryWrapper里面去
		if (StringUtils.isNotEmpty(key)) {
			/**
			 * 参数一：查询字段名称(不是属性名称)
			 * 参数二:参数值
			 */
			wrapper.like("name", key).or().eq("letter", key.toUpperCase());
		}

		if (StringUtils.isNotEmpty(sortBy)) {
			if (desc) {
				//降序
				wrapper.orderByDesc(sortBy);
			} else {
				//升序
				wrapper.orderByAsc(sortBy);
			}
		}

		//2.查询数据，获取结果
		iPage = brandMapper.selectPage(iPage, wrapper);

		//3.处理(封装)结果
		//iPage.getTotal():总记录数
		//iPage.getPages():总记录数
		//iPage.getRecords():数据列表
		if (CollectionUtils.isEmpty(iPage.getRecords())){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		PageResult<Brand> pageResult = new PageResult<>(iPage.getTotal(), iPage.getPages(), iPage.getRecords());

		//4.返回结果
		return pageResult;
	}

	public void saveBrand(Brand brand, List<Long> cids) {
		try {
			brandMapper.insert(brand);
			brandMapper.saveCategoryAndBrand(brand.getId(), cids);
		}catch (Exception e){
			e.printStackTrace();
			throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
		}
	}

	public Brand findBrandById(Long id) {
		Brand brand = brandMapper.selectById(id);
		if (null == brand){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		return brand;
	}

	public List<Brand> findBrandsByCid(Long id) {
		List<Brand> brands = brandMapper.findBrandsByCid(id);
		if (CollectionUtils.isEmpty(brands)){
			throw  new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		return brands;
	}

	public List<Brand> findCategoriesByIds(List<Long> ids) {

		List<Brand> brands = brandMapper.selectBatchIds(ids);
		if (CollectionUtils.isEmpty(brands)){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		return brands;
	}
}
