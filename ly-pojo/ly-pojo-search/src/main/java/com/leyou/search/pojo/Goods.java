package com.leyou.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;
import java.util.Set;

/**
 * @author cbh
 * @PackageName:com.leyou.search.pojo
 * @ClassName:Goods
 * @Description:一个spu对应一个Goods
 * @date 2021-02-03 14:38
 */
@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 1)
public class Goods {
	/**
	 * 索引不分词
	 */
	@Id
	@Field(type = FieldType.Keyword)
	private Long id;

	/**
	 * 副标题，不索引，不分词
	 */
	@Field(type = FieldType.Keyword, index = false)
	private String subTitle;

	/**
	 * spu的名称（因为用于高亮显示，所以 索引，分词）
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String spuName;

	/**
	 * 所有Sku对象信息， 存储json格式，  不索引，不分词
	 */
	@Field(type = FieldType.Keyword, index = false)
	private String skus;

	/**
	 * 所有需要被搜索的信息，包含三个分类名称+品牌名称+spuName+subTitle+所有Sku的title
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String all;

	/**
	 * 品牌id  索引，不分词
	 */
	@Field(type = FieldType.Long)
	private Long brandId;

	/**
	 * 商品3级分类id  索引，不分词
	 */
	@Field(type = FieldType.Long)
	private Long categoryId;

	/**
	 * 动态过滤条件，商品规格参数，key是参数名，值是参数值   索引，不分词
	 */
	@Field(type = FieldType.Object)
	private Map<String, Object> specs;

	/**
	 * 创建时间
	 */
	@Field(type = FieldType.Long)
	private Long createTime;

	/**
	 * 商品的价格，该字段用于排序条件
	 */
	@Field(type = FieldType.Long)
	private Set<Long> price;
}
