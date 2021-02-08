package com.leyou.search.dto;

import lombok.Data;

/**
 * @author cbh
 * @PackageName:com.leyou.search.dto
 * @ClassName:GoodsDTO
 * @Description: 传回给搜索的结果dto
 * @date 2021-02-05 15:41
 */
@Data
public class GoodsDTO {
	/**
	 * spuId
	 */
	private Long id;

	/**
	 * spu名称
	 */
	private String spuName;

	/**
	 * 卖点
	 */
	private String subTitle;

	/**
	 * sku信息的json结构
	 */
	private String skus;
}
