package com.leyou.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.mapper
 * @ClassName:BrandMapper
 * @Description:
 * @date 2021-01-28 12:06
 */
public interface BrandMapper extends BaseMapper<Brand> {
	/**
	 * 往中间表插入数据
	 * @param id
	 * @param cids
	 */
	void saveCategoryAndBrand(@Param("id") Long id,@Param("cids") List<Long> cids);

	/**
	 * 根据分类查询品牌
	 * @param id
	 * @return
	 */
	List<Brand> findBrandsByCid(@Param("cid") Long id);
}
