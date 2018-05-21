package com.movie.ycsaddas.service;

import com.movie.ycsaddas.vo.MovieVO;

import java.util.List;
import java.util.Map;

/**
 * 电影业务逻辑接口
 *
 * @author walker
 * @date 2018/05/19
 */
public interface MovieService {
	/**
	 * 获取到首页展示电影列表
	 * @return
	 */
	Map<String, List<MovieVO>> getMovies();
}
