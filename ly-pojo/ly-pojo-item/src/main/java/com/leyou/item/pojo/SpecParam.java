package com.leyou.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:SpecParam
 * @Description:参数实体类
 * @date 2021-01-30 19:20
 */
@Data
@TableName("tb_spec_param")
public class SpecParam {
	@TableId(type = IdType.AUTO)
	private Long id;
	private Long cid;
	private Long groupId;
	private String name;
	@TableField("`numeric`")
	private Boolean numeric;
	private String unit;
	private Boolean generic;
	private Boolean searching;
	private String segments;
	private Date createTime;
	private Date updateTime;
}
