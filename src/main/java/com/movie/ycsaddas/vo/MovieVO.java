package com.movie.ycsaddas.vo;

import lombok.Data;

import java.util.List;

/**
 * @author walker
 * @date 2018/05/19
 */
@Data
public class MovieVO {
	// 豆瓣电影id
	private String id;
	// 电影名称
	private String name;
	// 原名
	private String original_title;
	// 电影评分
	private float average;
	// 电影海报
	private String img;
	// 电影类型
	private List<String> genres;
	// 主演列表
	private List<PersonVO> actors;
	// 年代
	private int year;
	// 看过人数
	private int count;
	// 导演
	private List<PersonVO> director;
	// 又名
	private List<String> aka;
	// 国家
	private List<String> countries;
	// 简介
	private String summary;
	// 评分人数
	private int ratings_count;
}
