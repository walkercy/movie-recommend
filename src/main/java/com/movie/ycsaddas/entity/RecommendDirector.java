package com.movie.ycsaddas.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author walker
 * @date 2018/05/28
 */
@Data
@Entity
@Table(name = "yc_recommend_director")
public class RecommendDirector {
	@Id
	@GeneratedValue
	private int id;

	private String director;

	private int freq;

	@Column(name = "userId")
	private int userId;
}
