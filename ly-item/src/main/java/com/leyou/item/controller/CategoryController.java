package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.controller
 * @ClassName:CategoryController
 * @Description:分类controller
 * @date 2021-01-26 23:16
 */
@RestController
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	/**
	 * 根据父id查询分类
	 */
	@RequestMapping("/category/of/parent")
	public ResponseEntity<List<Category>> findCategoriesById(@RequestParam("pid") Long pid) {
		List<Category> categories = categoryService.findCategoriesById(pid);
		return ResponseEntity.ok(categories);
	}

	/**
	 * 根据分类ID集合 查询 分类对象集合（批量查询）
	 */
	@GetMapping("/category/list")
	public ResponseEntity<List<Category>> findCategoriesByIds(@RequestParam("ids") List<Long> ids){
		List<Category> categories = categoryService.findCategoriesByIds(ids);
		return ResponseEntity.ok(categories);
	}
}
