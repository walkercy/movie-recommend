package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.RecommendActor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author walker
 * @date 2018/05/28
 */
public interface ActorRepository extends CrudRepository<RecommendActor, Integer> {

	@Query(value = "select id,actor,user_id,freq from yc_recommend_actor where actor = ?1", nativeQuery = true)
	RecommendActor findByActor(String actor);

	List<RecommendActor> findTop2ByUserIdOrderByFreqDesc(int userId);
}
