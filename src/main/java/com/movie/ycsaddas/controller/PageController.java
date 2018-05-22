package com.movie.ycsaddas.controller;

import com.movie.ycsaddas.common.MovieUtils;
import com.movie.ycsaddas.common.UserLoginUtil;
import com.movie.ycsaddas.entity.User;
import com.movie.ycsaddas.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
			mav.addObject(MovieUtils.INTHEATERS, MovieUtils.getInTheatersForIndex());
			mav.addObject(MovieUtils.COMMINGS, MovieUtils.getCommingSoonForIndex());

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

	@GetMapping(value = "/detail")
	public ModelAndView detail(@RequestParam String id) {
		ModelAndView mav = new ModelAndView("detail");
		try {
			mav.addObject("movie", MovieUtils.getDetail(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
}
