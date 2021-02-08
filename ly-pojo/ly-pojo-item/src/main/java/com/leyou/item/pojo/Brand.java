package com.leyou.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:Brand
 * @Description:品牌
 * @date 2021-01-28 12:03
 */
@Data
@TableName("tb_brand")
public class Brand {
	@TableId(type = IdType.AUTO)
	private Long id;
	private String name;
	private String image;
	private String letter;
	private Date createTime;
	private Date updateTime;
}
