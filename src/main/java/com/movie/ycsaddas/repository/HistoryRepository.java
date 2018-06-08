package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.History;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author walker
 * @date 2018/06/08
 */
public interface HistoryRepository extends CrudRepository<History, Integer> {

	List<History> findByUsernameOrderByDateDesc(String username);

}
