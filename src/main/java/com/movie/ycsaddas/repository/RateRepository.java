package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.MovieRate;
import org.springframework.data.repository.CrudRepository;

/**
 * @author walker
 * @date 2018/06/03
 */
public interface RateRepository extends CrudRepository<MovieRate, Integer> {

}
