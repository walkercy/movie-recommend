package com.movie.ycsaddas.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 电影评分
 *
 * @author walker
 * @date 2018/06/03
 */
@Data
@Entity
@Table(name = "yc_rate")
public class MovieRate {

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "movie_id")
	private String movieId;

	@Column(name = "user_id")
	private int userId;

	private int rate;
}
