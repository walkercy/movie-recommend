package com.movie.ycsaddas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面调试
 *
 * @author walker
 * @date 2018/05/12
 */
@Controller
@RequestMapping(value = "/")
public class TestController {

	@RequestMapping(value = "/")
	public String index() {
		return "detail";
	}

	@RequestMapping(value = "/lay")
	public String lay() {
		return "lay";
	}

	

}
