package com.movie.ycsaddas.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.movie.ycsaddas.vo.MovieVO;
import com.movie.ycsaddas.vo.PersonVO;
import lombok.extern.slf4j.Slf4j;
import sun.awt.ModalExclude;

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

	public static final String INTHEATERS = "threaters";
	public static final String COMMINGS = "commings";

	/**
	 * 查询电影缓存
	 */
	public static Map<String, List<MovieVO>> movieListMap = new HashMap<>();
	public static Map<String, MovieVO> movieMap = new HashMap<>();

	public static List<MovieVO> getInTheatersForIndex() throws Exception {
		List<MovieVO> allTheaters = getInTheaters();
		List<MovieVO> result = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			result.add(allTheaters.get(i));
		}
		return result;
	}

	public static List<MovieVO> getCommingSoonForIndex() throws Exception {
		List<MovieVO> allCommings = getCommingSoon();
		List<MovieVO> result = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			result.add(allCommings.get(i));
		}
		return result;
	}

	public static List<MovieVO> getMovies(String key) throws Exception {
		return getMovies(key, 0, 20);
	}

	public static List<MovieVO> getMovies(String key, int start, int count) throws Exception {
		return parseMovies(DoubanClient.getMovies(key, start, count));
	}

	public static List<MovieVO> getInTheaters() throws Exception {
		if (movieListMap.get(INTHEATERS) == null) {
			movieListMap.put(INTHEATERS, parseMovies(DoubanClient.getInTheaters()));
		}
		return movieListMap.get(INTHEATERS);
	}

	public static List<MovieVO> getCommingSoon() throws Exception {
		if (movieListMap.get(COMMINGS) == null) {
			movieListMap.put(COMMINGS, parseMovies(DoubanClient.getCommingSoon()));
		}
		return movieListMap.get(COMMINGS);
	}

	/**
	 * 获取电影详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static MovieVO getDetail(String id) throws Exception {
		if (movieMap.get(id) == null ) {
			movieMap.put(id, parseMovieDetail(DoubanClient.getMovieDetail(id)));
		}
		return movieMap.get(id);
	}

	/**
	 * 转化电影列表数据为ArrayList
	 *
	 * @param movies 豆瓣API返回数据
	 * @return
	 */
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

	/**
	 * 将单个电影详情转化为MovieVO对象
	 * @param movies
	 * @return
	 */
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
		List<PersonVO> directors = new ArrayList<>();
		subject.getJSONArray("directors").forEach(obj -> {
			JSONObject cast = (JSONObject) obj;
			PersonVO director = new PersonVO();
			director.setId(cast.getString("id"));
			director.setName(cast.getString("name"));
			director.setImg(cast.getJSONObject("avatars").getString("large"));
			directors.add(director);
		});
		movie.setDirectors(directors);
		movie.setRatings_count(subject.getIntValue("ratings_count"));
		movie.setAka(subject.getJSONArray("aka").toJavaList(String.class));
		return movie;
	}
}
