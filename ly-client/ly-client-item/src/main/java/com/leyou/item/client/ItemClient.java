package com.leyou.item.client;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.client
 * @ClassName:ItemClient
 * @Description:
 * @date 2021-02-03 21:17
 */
@FeignClient("item-service")
public interface ItemClient {
	/**
	 * 商品分页查询
	 * @param page
	 * @param rows
	 * @param key
	 * @param saleable
	 * @return
	 */
	@GetMapping("/spu/page")
	public PageResult<SpuDTO> spuPageQuery(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "5") Integer rows,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "saleable", required = false) Boolean saleable
	);

	/**
	 * 根据spuId查找sku集合
	 * @param id
	 * @return
	 */
	@GetMapping("/sku/of/spu")
	public List<Sku> findSkusBySpuId(@RequestParam("id")Long id);

	/**
	 * 查询分类下可以用来搜索的规格参数
	 * @param gid
	 * @param cid
	 * @param searching
	 * @return
	 */
	@GetMapping("/spec/params")
	public List<SpecParam> findSpecParams(
			@RequestParam(value = "gid", required = false) Long gid,
			@RequestParam(value = "cid", required = false) Long cid,
			@RequestParam(value = "searching", required = false) Boolean searching
	);

	/**
	 * 根据spuId查询SpuDetail对象
	 * @param id
	 * @return
	 */
	@GetMapping("/spu/detail")
	public SpuDetail findSpuDetailBySpuId(@RequestParam("id") Long id);

	/**
	 * 根据分类ID集合 查询 分类对象集合（批量查询）
	 * @param ids
	 * @return
	 */
	@GetMapping("/category/list")
	public List<Category> findCategoriesByIds(@RequestParam("ids") List<Long> ids);

	/**
	 * 根据商品ID集合 查询 品牌对象集合（批量查询）
	 * @param ids
	 * @return
	 */
	@GetMapping("/brand/list")
	public List<Brand> findBrandsByIds(@RequestParam("ids") List<Long> ids);
}
