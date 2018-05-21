package com.movie.ycsaddas.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.movie.ycsaddas.vo.MovieVO;
import com.movie.ycsaddas.vo.PersonVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author walker
 * @date 2018/05/21
 */
@Slf4j
public class MovieUtils {
	/**
	 * 查询电影缓存
	 */
	public static Map<String, List<MovieVO>> movieMap = new HashMap<>();

	public static void main(String[] args) {
		try {
			List<MovieVO> movieList = parseMovies(DoubanClient.getInTheaters());
			System.out.println(movieList.size());
			movieList.forEach(movie -> System.out.println(movie.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<MovieVO> parseMovies(String movies) {
		List<MovieVO> movieList = new ArrayList<>();
		JSONArray subjects = JSONObject.parseObject(movies).getJSONArray("subjects");
		subjects.forEach(obj -> {
			JSONObject detail = (JSONObject) obj;
			MovieVO movie = new MovieVO();
			movie.setId(detail.getString("id"));
			movie.setImg(detail.getJSONObject("images").getString("large"));
			movie.setAverage(detail.getJSONObject("rating").getFloat("average"));
			movie.setName(detail.getString("title"));
			movieList.add(movie);
		});
		return movieList;
	}

	private static MovieVO parseMovieDetail(String movies) {
		MovieVO movie = new MovieVO();
		JSONObject subject = JSONObject.parseObject(movies);
		movie.setId(subject.getString("id"));
		movie.setImg(subject.getJSONObject("images").getString("large"));
		movie.setAverage(subject.getJSONObject("rating").getFloat("average"));
		movie.setYear(subject.getIntValue("year"));
		movie.setName(subject.getString("title"));
		movie.setCountries(subject.getJSONArray("countries").toJavaList(String.class));
		movie.setGenres(subject.getJSONArray("genres").toJavaList(String.class));
		movie.setCount(subject.getIntValue("collect_count"));
		List<PersonVO> actors = new ArrayList<>();
		subject.getJSONArray("casts").forEach(obj -> {
			JSONObject cast = (JSONObject) obj;
			PersonVO actor = new PersonVO();
			actor.setId(cast.getString("id"));
			actor.setName(cast.getString("name"));
			actor.setImg(cast.getJSONObject("avatars").getString("large"));
			actors.add(actor);
		});
		movie.setActors(actors);
		movie.setOriginal_title(subject.getString("original_title"));
		movie.setSummary(subject.getString("summary"));
		return movie;
	}
}
