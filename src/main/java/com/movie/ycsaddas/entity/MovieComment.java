package com.movie.ycsaddas.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 电影评分,评价表
 *
 * @author walker
 * @date 2018/06/03
 */
@Data
@Entity
@Table(name = "yc_comment")
public class MovieComment {

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "movie_id")
	private String movieId;

	private String username;

	private int rate;

	private String content;

	@Column(name = "comm_date")
	private Date date;
}
