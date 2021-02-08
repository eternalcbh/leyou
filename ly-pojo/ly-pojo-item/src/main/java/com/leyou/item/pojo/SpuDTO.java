package com.leyou.item.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:SpuDTO
 * @Description:封装Spu列表数据的对象
 * @date 2021-01-30 21:34
 */
@Data
public class SpuDTO {
	private Long id;
	private Long brandId;
	/**
	 * 1级类目
	 */
	private Long cid1;
	/**
	 * 2级类目
	 */
	private Long cid2;
	/**
	 * 3级类目
	 */
	private Long cid3;
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * 子标题
	 */
	private String subTitle;
	/**
	 * 是否上架
	 */
	private Boolean saleable;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后修改时间
	 */
	private Date updateTime;
	/**
	 * 分类名称，格式： 手机通讯/手机/手机
	 */
	private String categoryName;
	/**
	 * 品牌名称 格式：华为
	 */
	private String brandName;

	/**
	 * 用于接收添加商品的SpuDetail数据
	 */
	private SpuDetail spuDetail;

	/**
	 * 用于接收添加商品的SpuDetail数据
	 */
	private List<Sku> skus;
}
