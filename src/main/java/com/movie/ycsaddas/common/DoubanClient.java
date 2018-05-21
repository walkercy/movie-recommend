package com.movie.ycsaddas.common;

/**
 * 豆瓣API
 *
 * @author walker
 * @date 2018/05/19
 */
public class DoubanClient {

	private static final String BASE_MOVIE_URL = "https://api.douban.com/v2/movie/";
	private static final String IN_THEATERS = "in_theaters";
	private static final String COMMING_SOON = "coming_soon";
	private static final String TOP_250 = "top250";
	private static final String MOVIE_DETAIL = "subject/";
	private static final String ACTOR_DETAIL = "celebrity/";
	private static final String QUERY_BY_SUBJECT = "search?q=";
	private static final String QUERY_BY_TYPE = "search?tag=";

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

	public static String queryBySubject(String subject) throws Exception {
		return HttpClientUtils.get(getUrl(QUERY_BY_SUBJECT, subject));
	}

	public static String queryByType(String type) throws Exception {
		return HttpClientUtils.get(getUrl(QUERY_BY_TYPE, type));
	}

	private static String getUrl(String path) {
		return BASE_MOVIE_URL + path;
	}

	private static String getUrl(String path, String query) {
		return BASE_MOVIE_URL + path + query;
	}
}
