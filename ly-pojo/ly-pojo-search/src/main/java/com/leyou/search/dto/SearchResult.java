package com.leyou.search.dto;

import com.leyou.common.pojo.PageResult;
import lombok.Data;

import java.util.Map;

/**
 * @author cbh
 * @PackageName:com.leyou.search.dto
 * @ClassName:SearchResult
 * @Description: 封装所有搜索结果的DTO
 * @date 2021-02-05 15:43
 */

@Data
public class SearchResult<T> extends PageResult<T> {
	/**
	 * 封装过滤条件
	 */
	private Map<String, Object> filterConditions;
}
