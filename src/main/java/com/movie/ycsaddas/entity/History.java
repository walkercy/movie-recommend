package com.movie.ycsaddas.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author walker
 * @date 2018/06/08
 */
@Data
@Entity
@Table(name = "yc_history")
public class History {

	@Id
	@GeneratedValue
	private int id;

	private String username;

	@Column(name = "movie_id")
	private String movieId;

	private float rate;

	private String date;
}
