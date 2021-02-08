package com.leyou.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:PageResult
 * @Description:
 * @date 2021-01-28 11:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
	/**
	 * 总记录数
	 */
	private Long total;
	/**
	 * 总页数
	 */
	private Long totalPage;

	/**
	 * 当前页数据
	 */
	private List<T> items;
}
