package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.RecommendDirector;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author walker
 * @date 2018/05/28
 */
public interface DirectorRepository extends CrudRepository<RecommendDirector, Integer> {
	RecommendDirector findByDirector(String director);

	List<RecommendDirector> findTop2ByUserIdOrderByFreqDesc(int userId);
}
