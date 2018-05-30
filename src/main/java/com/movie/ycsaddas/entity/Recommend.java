package com.movie.ycsaddas.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author walker
 * @date 2018/05/28
 */
@Entity
@Data
public class Recommend {
	@Id
	@GeneratedValue
	private int id;

	private String type;

	private String actors;

	private String directors;

	private int user_id;

	private int freq;
}
