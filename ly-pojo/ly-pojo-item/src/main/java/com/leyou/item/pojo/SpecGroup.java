package com.leyou.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cbh
 * @PackageName:com.leyou.item.pojo
 * @ClassName:SpecGroup
 * @Description:规格组实体类
 * @date 2021-01-30 19:20
 */
@Data
@TableName("tb_spec_group")
public class SpecGroup {
	@TableId(type = IdType.AUTO)
	private Long id;
	private Long cid;
	private String name;
	private Date createTime;
	private Date updateTime;
}
