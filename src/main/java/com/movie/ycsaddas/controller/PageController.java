package com.movie.ycsaddas.controller;

import com.movie.ycsaddas.common.DoubanClient;
import com.movie.ycsaddas.common.MovieUtils;
import com.movie.ycsaddas.common.UserLoginUtil;
import com.movie.ycsaddas.entity.*;
import com.movie.ycsaddas.repository.*;
import com.movie.ycsaddas.vo.MovieVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author walker
 * @date 2018/05/19
 */
@Slf4j
@Controller
public class PageController {

	// 记录演员频次的增删查改对象
	@Autowired
	private ActorRepository actorRepository;

	// 记录导演频次的增删查改对象
	@Autowired
	private DirectorRepository directorRepository;

	// 记录电影类型频次的增删查改对象
	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@GetMapping(value = "/")
	public ModelAndView index() {
		System.out.println("index 方法");
		// 映射到index.html页面
		ModelAndView mav = new ModelAndView("index");
		List<MovieVO> recommends = new ArrayList<>();
		List<MovieVO> defaultRecommends = null;
		try {
			defaultRecommends = MovieUtils.getTop250();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (UserLoginUtil.currentUser != null) {
			// 根据主演推荐电影
			List<RecommendActor> actorList = actorRepository.findTop2ByUserIdOrderByFreqDesc(UserLoginUtil.currentUser.getId());
			if (actorList == null || actorList.size() == 0) {
				System.out.println("推荐演员列表为空");
			} else {
				System.out.println("推荐演员列表内容如下");
				for (RecommendActor actor : actorList) {
					System.out.println(actor.toString());
					try {
						List<MovieVO> movies = MovieUtils.searchForMovie(actor.getActor());
						if (movies.size() == 0) {
							continue;
						}
						Random random = new Random();
						int index = random.nextInt(movies.size()) + 0;
						recommends.add(movies.get(index));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// 根据导演推荐电影
			List<RecommendDirector> directors = directorRepository.findTop2ByUserIdOrderByFreqDesc(UserLoginUtil.currentUser.getId());
			if (directors == null || directors.size() == 0) {
				System.out.println("推荐导演列表为空");
			} else {
				System.out.println("推荐导演列表内容如下");
				for(RecommendDirector director : directors) {
					System.out.println(director.toString());
					try {
						List<MovieVO> movies = MovieUtils.searchForMovie(director.getDirector());
						if (movies.size() == 0) {
							continue;
						}
						Random random = new Random();
						int index = random.nextInt(movies.size()) + 0;
						recommends.add(movies.get(index));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// 根据类型推荐电影
			List<RecommendType> types = typeRepository.findTop2ByUserIdOrderByFreqDesc(UserLoginUtil.currentUser.getId());
			if (types == null || types.size() == 0) {
				System.out.println("推荐类型列表为空");
			} else {
				System.out.println("推荐类型列表内容如下");
				for (RecommendType type : types) {
					System.out.println(type.toString());
					try {
						List<MovieVO> movies = MovieUtils.searchForMovie(type.getType());
						if (movies.size() == 0) {
							continue;
						}
						Random random = new Random();
						int index = random.nextInt(movies.size()) + 0;
						recommends.add(movies.get(index));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			// 用户没有登录就推荐TOP250
			try {
				for (int i = 0; i < 6; i++) {
					recommends.add(defaultRecommends.get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (recommends.size() < 6) {
			for (int i = 0; i < 6 - recommends.size(); i++) {
				Random random = new Random();
				int index = random.nextInt(defaultRecommends.size()) + 0;
				recommends.add(defaultRecommends.get(index));
			}
		}
		try {
			mav.addObject(MovieUtils.INTHEATERS, MovieUtils.getMovies(DoubanClient.IN_THEATERS, 0, 6));
			mav.addObject(MovieUtils.COMMINGS, MovieUtils.getMovies(DoubanClient.COMMING_SOON, 0, 6));
			mav.addObject("recommends", recommends);
			checkUserLogin(mav);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	/**
	 * 展示电影列表页面
	 * @param catagory
	 * @return
	 */
	@GetMapping(value = "/list/{catagory}")
	public ModelAndView list(@PathVariable(value = "catagory") String catagory) {
		log.info("请求电影列表 ：" + catagory);
		ModelAndView mav = new ModelAndView("list");
		checkUserLogin(mav);
		try {
			mav.addObject("movies", MovieUtils.getMovies(catagory, 0, 24));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	/**
	 * 电影详情页面
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/detail/{id}")
	public ModelAndView detail(@PathVariable(value = "id") String id) {
		log.info("movie id = " + id);
		ModelAndView mav = new ModelAndView("detail");
		checkUserLogin(mav);
		try {
			MovieVO movie = MovieUtils.getDetail(id);
			List<MovieComment> comments = commentRepository.findAllByMovieId(id);
			if (comments == null || comments.size() == 0) {
				log.info("comments is null");
			}
			if (UserLoginUtil.currentUser != null) {
				List<RecommendActor> actors = new ArrayList<>();
				movie.getActors().forEach(actor -> {
					RecommendActor dbactor = actorRepository.findByActor(actor.getName());
					if (dbactor != null) {
						int freq = dbactor.getFreq();
						freq++;
						dbactor.setFreq(freq);
						actorRepository.save(dbactor);
					} else {
						RecommendActor ractor = new RecommendActor();
						ractor.setFreq(1);
						System.out.println("userid = " + UserLoginUtil.currentUser.getId());
						ractor.setUserId(UserLoginUtil.currentUser.getId());
						ractor.setActor(actor.getName());
						actors.add(ractor);
					}
				});
				if (actors.size() > 0) {
					System.out.println("actors num = " + actors.size());
					actorRepository.saveAll(actors);
				}

				List<RecommendDirector> directors = new ArrayList<>();
				movie.getDirectors().forEach(director -> {
					RecommendDirector dbdir = directorRepository.findByDirector(director.getName());
					if (dbdir != null) {
						int freq = dbdir.getFreq();
						freq++;
						dbdir.setFreq(freq);
						directorRepository.save(dbdir);
					} else {
						RecommendDirector rdir = new RecommendDirector();
						rdir.setFreq(1);
						rdir.setUserId(UserLoginUtil.currentUser.getId());
						rdir.setDirector(director.getName());
						directors.add(rdir);
					}
				});
				if (directors.size() > 0) {
					directorRepository.saveAll(directors);
				}

				List<RecommendType> types = new ArrayList<>();
				movie.getGenres().forEach(type -> {
					RecommendType dbtype = typeRepository.findByType(type);
					if (dbtype != null) {
						int freq = dbtype.getFreq();
						freq++;
						dbtype.setFreq(freq);
						typeRepository.save(dbtype);
					} else {
						RecommendType rtype = new RecommendType();
						rtype.setFreq(1);
						rtype.setUserId(UserLoginUtil.currentUser.getId());
						rtype.setType(type);
						types.add(rtype);
					}
				});
				if (types.size() > 0) {
					typeRepository.saveAll(types);
				}
			}
			mav.addObject("movie", movie);
			mav.addObject("comments", comments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	/**
	 * 电影搜索逻辑
	 * @param key
	 * @return
	 */
	@GetMapping(value = "/search")
	public ModelAndView search(@RequestParam(value = "key") String key) {
		log.info("search for " + key);
		ModelAndView mav = new ModelAndView("list");
		checkUserLogin(mav);
		try {
			if (UserLoginUtil.currentUser != null) {
				RecommendType obj = typeRepository.findByType(key);
				if (obj != null) {
					int freq = obj.getFreq();
					freq++;
					obj.setFreq(freq);
					typeRepository.save(obj);
				} else {
					RecommendType type = new RecommendType();
					type.setFreq(1);
					type.setType(key);
					type.setUserId(UserLoginUtil.currentUser.getId());
					typeRepository.save(type);
				}
			}
			mav.addObject("movies", MovieUtils.searchForMovies(key, 0, 24));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping(value = "/user/{username}")
	public ModelAndView userDetail(@PathVariable(value = "username") String username) {
		ModelAndView mav = new ModelAndView("user");
		log.info("用户详情页，username = " + username);
		checkUserLogin(mav);
		List<History> histories = historyRepository.findByUsernameOrderByDateDesc(username);
		List<MovieVO> movies = new ArrayList<>();
		histories.forEach(history -> {
			try {
				MovieVO movie = MovieUtils.getDetail(history.getMovieId());
				movie.setRateDate(history.getDate());
				movies.add(movie);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		mav.addObject("movies", movies);
		return mav;
	}

	/**
	 * 检查用户登录状态
	 * @param mav
	 */
	private void checkUserLogin(ModelAndView mav) {
		User user = UserLoginUtil.currentUser;
		if (user != null) {
			log.info("登录用户是 ：" + user.getUsername());
			log.info("登录用户ID是 ：" + user.getId());
			mav.addObject("login", UserLoginUtil.userLoginMap.get(user.getId()));
			mav.addObject("user", user);
			log.info("map size " + UserLoginUtil.userLoginMap.size());
			log.info("登录状态为：" + UserLoginUtil.userLoginMap.get(user.getId()));
		} else {
			log.info("用户还未登录");
			mav.addObject("login", UserLoginUtil.logout);
		}
	}

}
