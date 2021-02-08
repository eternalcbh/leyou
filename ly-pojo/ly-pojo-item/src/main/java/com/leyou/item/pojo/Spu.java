package com.leyou.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:Spu
 * @Description:
 * @date 2021-01-30 21:23
 */
@Data
@TableName("tb_spu")
public class Spu {
	@TableId(type = IdType.AUTO)
	private Long id;
	private Long brandId;
	/**
	 * 1级分类
	 */
	private Long cid1;
	/**
	 * 2级分类
	 */
	private Long cid2;
	/**
	 * 3级分类
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
}
