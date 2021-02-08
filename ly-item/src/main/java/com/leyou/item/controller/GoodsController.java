package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.SpuDTO;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.controller
 * @ClassName:GoodsController
 * @Description:商品的控制层
 * @date 2021-01-30 21:41
 */
@RestController
public class GoodsController {
	@Autowired
	private GoodsService goodsService;

	/**
	 * 分页查询商品
	 *
	 * @param page
	 * @param rows
	 * @param key
	 * @param saleable
	 * @return
	 */
	@GetMapping("/spu/page")
	public ResponseEntity<PageResult<SpuDTO>> spuPageQuery(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "5") Integer rows,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "saleable", required = false) Boolean saleable
	) {
		PageResult<SpuDTO> pageResult = goodsService.spuPageQuery(page, rows, key, saleable);
		return ResponseEntity.ok(pageResult);
	}

	@PostMapping("/goods")
	public ResponseEntity<Void> saveGoods(@RequestBody SpuDTO spuDTO) {
		goodsService.saveGoods(spuDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 商品上下架
	 * @param id
	 * @return
	 */
	@PutMapping("/spu/saleable")
	public ResponseEntity<Void> updateSaleable(@RequestParam("id") Long id,@RequestParam("saleable") Boolean saleable){
		goodsService.updateSaleable(id, saleable);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}


	/**
	 * 根据spuId查询Sku集合
	 */
	@GetMapping("/sku/of/spu")
	public ResponseEntity<List<Sku>> findSkusBySpuId(@RequestParam("id")Long id){
		List<Sku> skus = goodsService.findSkusBySpuId(id);
		return ResponseEntity.ok(skus);
	}
	/**
	 * 根据spuId查询SpuDetail对象
	 */
	@GetMapping("/spu/detail")
	public ResponseEntity<SpuDetail> findSpuDetailBySpuId(@RequestParam("id") Long id){
		SpuDetail spuDetail = goodsService.findSpuDetailBySpuId(id);
		return ResponseEntity.ok(spuDetail);
	}
}
