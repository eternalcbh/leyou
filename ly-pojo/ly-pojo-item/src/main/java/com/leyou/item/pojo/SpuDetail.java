package com.leyou.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:SpuDetail
 * @Description:
 * @date 2021-01-30 21:26
 */
@Data
@TableName("tb_spu_detail")
public class SpuDetail {
	/**
	 * 对应spu的id
	 */
	@TableId(type = IdType.INPUT)
	private Long spuId;
	/**
	 *商品描诉
	 */
	private String description;
	/**
	 * 商品的特殊规格的名称及可选值模板
	 */
	private String specialSpec;

	/**
	 * 商品的全局规格属性
	 */
	private String genericSpec;

	/**
	 * 包装清单
	 */
	private String packingList;

	/**
	 * 售后服务
	 */
	private String afterService;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 最后修改时间
	 */
	private Date updateTime;
}
