package com.movie.ycsaddas.common;

/**
 * 豆瓣API
 *
 * @author walker
 * @date 2018/05/19
 */
public class DoubanClient {

	private static final String BASE_MOVIE_URL = "https://api.douban.com/v2/movie/";
	public static final String IN_THEATERS = "in_theaters";
	public static final String COMMING_SOON = "coming_soon";
	public static final String TOP_250 = "top250";
	private static final String MOVIE_DETAIL = "subject/";
	private static final String ACTOR_DETAIL = "celebrity/";
	private static final String QUERY_BY_TYPE = "search?tag=";

	public static String getMovies(String uri) throws Exception {
		return getMovies(uri, 0, 20);
	}

	public static String getMovies(String uri, int start, int count) throws Exception {
		return HttpClientUtils.get(getUrl(uri + "?start=" + start + "&count=" + count));
	}

	public static String getInTheaters() throws Exception {
		return HttpClientUtils.get(getUrl(IN_THEATERS));
	}

	public static String getCommingSoon() throws Exception {
		return HttpClientUtils.get(getUrl(COMMING_SOON));
	}

	public static String getTop250() throws Exception {
		return HttpClientUtils.get(getUrl(TOP_250));
	}

	public static String getMovieDetail(String movieId) throws Exception {
		return HttpClientUtils.get(getUrl(MOVIE_DETAIL, movieId));
	}

	public static String getActorDetail(String actorId) throws Exception {
		return HttpClientUtils.get(getUrl(ACTOR_DETAIL, actorId));
	}

	public static String queryByType(String type, int start, int count) throws Exception {
		return HttpClientUtils.get(getUrl(QUERY_BY_TYPE + type) + "&start=" + start + "&count=" + count);
	}

	private static String getUrl(String path) {
		return BASE_MOVIE_URL + path;
	}

	private static String getUrl(String path, String query) {
		return BASE_MOVIE_URL + path + query;
	}
}
