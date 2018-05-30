package com.movie.ycsaddas.controller;

import com.movie.ycsaddas.common.DoubanClient;
import com.movie.ycsaddas.common.MovieUtils;
import com.movie.ycsaddas.common.UserLoginUtil;
import com.movie.ycsaddas.entity.*;
import com.movie.ycsaddas.repository.ActorRepository;
import com.movie.ycsaddas.repository.DirectorRepository;
import com.movie.ycsaddas.repository.TypeRepository;
import com.movie.ycsaddas.service.MovieService;
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

/**
 * @author walker
 * @date 2018/05/19
 */
@Slf4j
@Controller
public class PageController {

	@Autowired
	private MovieService movieService;

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private DirectorRepository directorRepository;

	@Autowired
	private TypeRepository typeRepository;

	@GetMapping(value = "/")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index");
		movieService.getMovies().forEach((k, v) -> mav.addObject(k, v));
		try {
			mav.addObject(MovieUtils.INTHEATERS, MovieUtils.getMovies(DoubanClient.IN_THEATERS, 0, 6));
			mav.addObject(MovieUtils.COMMINGS, MovieUtils.getMovies(DoubanClient.COMMING_SOON, 0, 6));
			checkUserLogin(mav);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

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

	@GetMapping(value = "/detail/{id}")
	public ModelAndView detail(@PathVariable(value = "id") String id) {
		log.info("movie id = " + id);
		ModelAndView mav = new ModelAndView("detail");
		checkUserLogin(mav);
		try {
			MovieVO movie = MovieUtils.getDetail(id);
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
			}
			mav.addObject("movie", movie);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@GetMapping(value = "/search")
	public ModelAndView search(@RequestParam(value = "key") String key) {
		log.info("search for " + key);
		ModelAndView mav = new ModelAndView("list");
		checkUserLogin(mav);
		try {
			if (typeRepository.findByType(key) != null) {

			} else {
				RecommendType type = new RecommendType();
				type.setFreq(1);
				type.setType(key);
				type.setUserId(UserLoginUtil.currentUser.getId());
				typeRepository.save(type);
			}
			mav.addObject("movies", MovieUtils.searchForMovies(key, 0, 24));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	private void checkUserLogin(ModelAndView mav) {
		User user = UserLoginUtil.currentUser;
		if (user != null) {
			log.info("登录用户是 ：" + user.getUsername());
			log.info("登录用户ID是 ：" + user.getId());
			mav.addObject("login", UserLoginUtil.userLoginMap.get(user.getId()));
			mav.addObject("name", user.getUsername());
			log.info("map size " + UserLoginUtil.userLoginMap.size());
			log.info("登录状态为：" + UserLoginUtil.userLoginMap.get(user.getId()));
		} else {
			log.info("用户还未登录");
			mav.addObject("login", UserLoginUtil.logout);
		}
	}
}
