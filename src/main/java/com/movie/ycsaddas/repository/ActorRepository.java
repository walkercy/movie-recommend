package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.RecommendActor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author walker
 * @date 2018/05/28
 */
public interface ActorRepository extends CrudRepository<RecommendActor, Integer> {
	RecommendActor findByActor(String actor);

	List<RecommendActor> findTop2ByUserIdOrderByFreqDesc(int userId);
}
