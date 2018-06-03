package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.RecommendType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author walker
 * @date 2018/05/28
 */
public interface TypeRepository extends CrudRepository<RecommendType, Integer> {
	RecommendType findByType(String type);

	List<RecommendType> findTop2ByUserIdOrderByFreqDesc(int userId);
}
