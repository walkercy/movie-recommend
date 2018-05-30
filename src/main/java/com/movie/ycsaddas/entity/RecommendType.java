package com.movie.ycsaddas.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author walker
 * @date 2018/05/28
 */
@Data
@Entity
@Table(name = "yc_recommend_type")
public class RecommendType {
	@Id
	@GeneratedValue
	private int id;

	private String type;

	private int freq;

	private int userId;
}
