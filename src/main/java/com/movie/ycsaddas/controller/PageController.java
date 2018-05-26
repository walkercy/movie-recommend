package com.movie.ycsaddas.controller;

import com.movie.ycsaddas.common.DoubanClient;
import com.movie.ycsaddas.common.MovieUtils;
import com.movie.ycsaddas.common.UserLoginUtil;
import com.movie.ycsaddas.entity.User;
import com.movie.ycsaddas.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author walker
 * @date 2018/05/19
 */
@Slf4j
@Controller
public class PageController {

	@Autowired
	private MovieService movieService;

	@GetMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index");
		movieService.getMovies().forEach((k, v) -> mav.addObject(k, v));
		try {
			mav.addObject(MovieUtils.INTHEATERS, MovieUtils.getMovies(DoubanClient.IN_THEATERS, 0, 6));
			mav.addObject(MovieUtils.COMMINGS, MovieUtils.getMovies(DoubanClient.COMMING_SOON, 0, 6));

			User user = UserLoginUtil.currentUser;
			if (user != null) {
				log.info("登录用户是 ：" + user.getUsername());
				log.info("登录用户ID是 ：" + user.getId());
				mav.addObject("login", UserLoginUtil.userLoginMap.get(user.getId()));
				mav.addObject("name", user.getUsername());
				log.info("map size " + UserLoginUtil.userLoginMap.size());
				log.info("登录状态为：" + UserLoginUtil.userLoginMap.get(user.getId()));
			} else {
				mav.addObject("login", UserLoginUtil.logout);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping(value = "/list/{catagory}")
	public ModelAndView list(@PathVariable(value = "catagory") String catagory) {
		log.info("请求电影列表 ：" + catagory);
		ModelAndView mav = new ModelAndView("list");
		try {
			mav.addObject("movies", MovieUtils.getMovies(catagory, 0, 24));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping(value = "/detail/{id}")
	public ModelAndView detail(@PathVariable(value = "id") String id) {
		log.info("movie id = " + id);
		ModelAndView mav = new ModelAndView("detail");
		try {
			mav.addObject("movie", MovieUtils.getDetail(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
}
