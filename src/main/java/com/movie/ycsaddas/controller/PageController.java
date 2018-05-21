package com.movie.ycsaddas.controller;

import com.movie.ycsaddas.common.UserLoginUtil;
import com.movie.ycsaddas.entity.User;
import com.movie.ycsaddas.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author walker
 * @date 2018/05/19
 */
@Controller
public class PageController {

	@Autowired
	private MovieService movieService;

	@GetMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index");
		movieService.getMovies().forEach((k, v) -> mav.addObject(k, v));
		User user = UserLoginUtil.currentUser;
		if (user != null) {
			System.out.println("登录用户是 ：" + user.getUsername());
			System.out.println("登录用户ID是 ：" + user.getId());
			mav.addObject("login", UserLoginUtil.userLoginMap.get(user.getId()));
			mav.addObject("name", user.getUsername());
			System.out.println("map size " + UserLoginUtil.userLoginMap.size());
			System.out.println("登录状态为：" + UserLoginUtil.userLoginMap.get(user.getId()));
		} else {
			mav.addObject("login", UserLoginUtil.logout);
		}
		return mav;
	}
}
