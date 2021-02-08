package com.leyou.search.dto;

/**
 * @author cbh
 * @PackageName:com.leyou.search.dto
 * @ClassName:SearchRequest
 * @Description: 用于接收搜索页面传递的参数
 * @date 2021-02-05 15:35
 */
public class SearchRequest {
	/**
	 * 搜索条件
	 */
	private String key;

	/**
	 * 当前页
	 */
	private Integer page;

	/**
	 * 每页大小，不从页面接受，而是固定大小
	 */
	private static final Integer DEFAULT_SIZE = 20;

	/**
	 * 默认页
	 */
	private static final Integer DEFAULT_PAGE = 1;

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public Integer getPage(){
		if (null == page){
			return DEFAULT_PAGE;
		}
		return Math.max(DEFAULT_PAGE, page);
	}

	public void setPage(Integer page){
		this.page = page;
	}

	public Integer getSize(){
		return DEFAULT_SIZE;
	}
}
