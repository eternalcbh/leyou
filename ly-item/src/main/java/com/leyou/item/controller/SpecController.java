package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.controller
 * @ClassName:SpecController
 * @Description:规格参数
 * @date 2021-01-30 19:32
 */
@RestController
public class SpecController {
	@Autowired
	private SpecService specService;

	/**
	 * 根据分类id查询规格组
	 *
	 * @return
	 */
	@GetMapping("/spec/groups/of/category")
	public ResponseEntity<List<SpecGroup>> findSpecGroupByCid(@RequestParam("id") Long id) {
		List<SpecGroup> specGroupList = specService.findSpecGroupByCid(id);
		return ResponseEntity.ok(specGroupList);
	}

	/**
	 * 查询分类下可以用来搜索的规格参数
	 * @param gid
	 * @param cid
	 * @param searching
	 * @return
	 */
	@GetMapping("/spec/params")
	public ResponseEntity<List<SpecParam>> findSpecParams(
			@RequestParam(value = "gid", required = false) Long gid,
			@RequestParam(value = "cid", required = false) Long cid,
			@RequestParam(value = "searching", required = false) Boolean searching
	) {
		List<SpecParam> specParams = specService.findSpecParams(gid, cid, searching);
		return ResponseEntity.ok(specParams);
	}


}
