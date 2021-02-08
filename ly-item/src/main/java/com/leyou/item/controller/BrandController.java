package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.controller
 * @ClassName:BrandController
 * @Description:
 * @date 2021-01-28 12:06
 */
@RestController
public class BrandController {

	@Autowired
	private BrandService brandService;

	@GetMapping("/brand/page")
	public ResponseEntity<PageResult<Brand>> brandPageQuery(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "5") Integer rows,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "desc", required = false) Boolean desc
	) {
		PageResult<Brand> pageResult = brandService.brandPageQuery(page, rows, key, sortBy, desc);
		return ResponseEntity.ok(pageResult);
	}

	@PostMapping("/brand")
	public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
		brandService.saveBrand(brand, cids);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 根据ID查询品牌
	 *   @PathVariable： 接收来自于URL的路径参数  例如： /brand/10
	 *   @RequestParam:  接收来自URL的普通参数  例如： /brand?id=10
	 */
	@GetMapping("/brand/{id}")
	public ResponseEntity<Brand> findBrandById(@PathVariable Long id){
		Brand brand = brandService.findBrandById(id);
		return ResponseEntity.ok(brand);
	}

	@GetMapping("/brand/of/category")
	public ResponseEntity<List<Brand>> findBrandsByCid(@RequestParam("id") Long id){
		List<Brand> brands = brandService.findBrandsByCid(id);
		return ResponseEntity.ok(brands);
	}

	/**
	 * 根据品牌ID集合 查询 商品对象集合（批量查询）
	 */
	@GetMapping("/brand/list")
	public ResponseEntity<List<Brand>> findBrandsByIds(@RequestParam("ids") List<Long> ids){
		List<Brand> categories = brandService.findCategoriesByIds(ids);
		return ResponseEntity.ok(categories);
	}
}
