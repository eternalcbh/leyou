package com.leyou.item.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leyou.common.exception.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cbh
 * @PackageName:com.leyou.item.service
 * @ClassName:SpecService
 * @Description:商品规格服务
 * @date 2021-01-30 19:32
 */
@Service
@Transactional
public class SpecService {
	@Autowired(required = false)
	private SpecGroupMapper specGroupMapper;

	@Autowired(required = false)
	private SpecParamMapper specParamMapper;

	public List<SpecGroup> findSpecGroupByCid(Long id) {
		//1.创建条件对象
		if (null != id) {
			SpecGroup specGroup = new SpecGroup();
			specGroup.setCid(id);

			//2.创建查询对象
			QueryWrapper<SpecGroup> query = Wrappers.query(specGroup);

			List<SpecGroup> specGroupList = specGroupMapper.selectList(query);

			if (CollectionUtils.isEmpty(specGroupList)) {
				throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
			}
			return specGroupList;
		}
		throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
	}

	public List<SpecParam> findSpecParams(Long gid, Long cid, Boolean searching) {
		//1.构建查找对象
		SpecParam specParam = new SpecParam();
		specParam.setCid(cid);
		specParam.setGroupId(gid);
		specParam.setSearching(searching);

		//2.创建查询对象
		QueryWrapper<SpecParam> query = Wrappers.query(specParam);

		List<SpecParam> specParams = specParamMapper.selectList(query);

		if (CollectionUtils.isEmpty(specParams)){
			throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
		}
		return specParams;
	}
}
