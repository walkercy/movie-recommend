package com.movie.ycsaddas.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.movie.ycsaddas.common.DoubanClient;
import com.movie.ycsaddas.service.MovieService;
import com.movie.ycsaddas.vo.MovieVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author walker
 * @date 2018/05/19
 */
@Slf4j
@Service
public class MovieServiceImpl implements MovieService {
	/**
	 * 获取到首页展示电影列表
	 *
	 * @return
	 */
	@Override
	public Map<String, List<MovieVO>> getMovies() {
		Map<String, List<MovieVO>> movieList = new HashMap<>();
		List<MovieVO> inThreaters = new ArrayList<>();
		List<MovieVO> commingSoon = new ArrayList<>();
		try {
			JSONArray threaterArray = JSONObject.parseObject(DoubanClient.getInTheaters()).getJSONArray("subjects");
			JSONArray commingArray = JSONObject.parseObject(DoubanClient.getCommingSoon()).getJSONArray("subjects");
			for (int i = 0; i < 6; i++) {
				MovieVO threater = new MovieVO();
				MovieVO comming = new MovieVO();
				threater.setName(threaterArray.getJSONObject(i).getString("title"));
				comming.setName(commingArray.getJSONObject(i).getString("title"));
				threater.setAverage(threaterArray.getJSONObject(i).getJSONObject("rating").getFloat("average"));
				comming.setAverage(commingArray.getJSONObject(i).getJSONObject("rating").getFloat("average"));
				threater.setImg(threaterArray.getJSONObject(i).getJSONObject("images").getString("large"));
				comming.setImg(commingArray.getJSONObject(i).getJSONObject("images").getString("large"));
				inThreaters.add(threater);
				commingSoon.add(comming);
			}
			movieList.put("threaters", inThreaters);
			movieList.put("commings", commingSoon);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取电影数据错误 {}", e.getMessage());
		}
		return movieList;
	}
}
