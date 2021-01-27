package com.leyou.item.service;

import com.leyou.common.exception.pojo.ExceptionEnum;
import com.leyou.common.exception.pojo.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author cbh
 * @PackageName:com.leyou.service
 * @ClassName:ItemService
 * @Description:
 * @date 2021-01-26 19:11
 */
@Service
@Slf4j
public class ItemService {
	public Long saveItem(Long id) {
		//模拟添加操作，如果id为异常
		if (id.equals(1L)) {
			throw new LyException(ExceptionEnum.UNAUTHORIZED);
		}
		return id;
	}
}
