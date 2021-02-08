package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author cbh
 * @PackageName:com.leyou.search.repository
 * @ClassName:SearchRepository
 * @Description: es基本搜索注册
 * @date 2021-02-03 20:57
 */
public interface SearchRepository extends ElasticsearchRepository<Goods,Long> {
}
