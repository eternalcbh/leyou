package com.leyou.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cbh
 * @PackageName:PACKAGE_NAME
 * @ClassName:Category
 * @Description:
 * @date 2021-01-26 23:08
 */
@Data
@TableName("tb_category")
public class Category {
	@TableId(type = IdType.AUTO)
	private Long id;
	private String name;
	private Long parentId;
	private Boolean isParent;
	private Integer sort;
	private Date createTime;
	private Date updateTime;
}
