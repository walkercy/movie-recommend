package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.MovieComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author walker
 * @date 2018/06/03
 */
public interface CommentRepository extends CrudRepository<MovieComment, Integer> {

	List<MovieComment> findAllByMovieId(String movieId);

}
