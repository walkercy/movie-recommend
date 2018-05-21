package com.movie.ycsaddas.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.movie.ycsaddas.common.DoubanClient;
import com.movie.ycsaddas.common.JsonResult;
import com.movie.ycsaddas.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 电影资源API
 *
 * @author walker
 * @date 2018/05/18
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/movie")
public class MovieController {

	/**
	 * 获取最受欢迎电影列表
	 * @return
	 */
	@GetMapping(value = "/in_theaters")
	public JsonResult getUser() {
		try {
			return JsonResult.success(JSONObject.parse(DoubanClient.getInTheaters()));
		} catch (Exception e) {
			log.error("获取最受欢迎电影列表失败：{}", e);
			return JsonResult.failture(e.getMessage());
		}
	}
}
