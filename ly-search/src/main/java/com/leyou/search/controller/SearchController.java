package com.leyou.search.controller;

import com.leyou.search.dto.GoodsDTO;
import com.leyou.search.dto.SearchRequest;
import com.leyou.search.dto.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cbh
 * @PackageName:com.leyou.search.controller
 * @ClassName:SearchController
 * @Description: 搜索Controller
 * @date 2021-02-03 21:02
 */
@RestController
public class SearchController {
	@Autowired
	private SearchService searchService;

	/**
	 * 商品搜索
	 */
	@PostMapping("/page")
	public ResponseEntity<SearchResult<GoodsDTO>> goodsSearchPage(@RequestBody SearchRequest searchRequest){
		SearchResult<GoodsDTO> searchResult = searchService.goodsSearchPage(searchRequest);
		return ResponseEntity.ok(searchResult);
	}
}
