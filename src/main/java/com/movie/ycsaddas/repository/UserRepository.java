package com.movie.ycsaddas.repository;

import com.movie.ycsaddas.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author walker
 * @date 2018/05/20
 */
public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUsername(String username);
}
