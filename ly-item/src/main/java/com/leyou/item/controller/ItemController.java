package com.leyou.item.controller;

import com.leyou.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cbh
 * @PackageName:com.leyou.ItemController
 * @ClassName:ItemController
 * @Description:
 * @date 2021-01-26 19:15
 */
@RestController
public class ItemController {
	@Autowired
	private ItemService itemService;

	@PostMapping("/save")
	public ResponseEntity<Long> saveItem(@RequestParam("id") Long id) {
		return ResponseEntity.status(HttpStatus.CREATED).body(itemService.saveItem(id));
	}
}
