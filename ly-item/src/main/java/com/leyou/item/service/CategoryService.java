package com.leyou.item.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leyou.common.exception.pojo.ExceptionEnum;
import com.leyou.common.exception.pojo.LyException;
import com.leyou.item.pojo.Category;
import com.leyou.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.service
 * @ClassName:CategoryService
 * @Description:
 * @date 2021-01-26 23:15
 */
@Service
@Transactional
public class CategoryService {
	@Autowired(required = false)
	private CategoryMapper categoryMapper;

	public List<Category> findCategoriesById(Long pid) {
		/**
		 * 1.构建查询条件
		 * Querywrapper：用于封装所有查询条件
		 *      QueryWrapper对象构建：
		 *          1）QueryWrapper query = Wrappers.query() 无参  自定义查询方式，例如，同时分页+条件判断等
		 *          2）QueryWrapper query = Wrappers.query(T) 有参
		 */
		Category category = new Category();

		//1.设置条件
		category.setParentId(pid);

		QueryWrapper<Category> wrapper = Wrappers.query(category);

		//2.执行查询
		List<Category> categories = categoryMapper.selectList(wrapper);

		//3.处理结果
		if (CollectionUtils.isEmpty(categories)){
			throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
		}

		//4.返回结果
		return categories;
	}
}
