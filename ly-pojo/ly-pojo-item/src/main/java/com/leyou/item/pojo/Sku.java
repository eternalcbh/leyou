package com.leyou.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.Date;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:Sku
 * @Description:
 * @date 2021-01-30 21:30
 */
@Data
@TableName("tb_sku")
public class Sku {
	@TableId(type = IdType.AUTO)
	private Long id;

	private Long spuId;

	private String title;

	private String images;

	private Long price;

	private Integer stock;

	/**
	 * 商品特殊规格的键值对
	 */
	private String ownSpec;

	/**
	 * 商品特殊规格的下标
	 */
	private String indexes;

	/**
	 * 是否有效，逻辑删除使用
	 */
	private Boolean enable;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 最后修改时间
	 */
	private Date updateTime;
}
