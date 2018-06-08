package com.movie.ycsaddas.controller.api;

import com.movie.ycsaddas.common.JsonResult;
import com.movie.ycsaddas.common.UserLoginUtil;
import com.movie.ycsaddas.entity.History;
import com.movie.ycsaddas.entity.MovieComment;
import com.movie.ycsaddas.entity.User;
import com.movie.ycsaddas.repository.CommentRepository;
import com.movie.ycsaddas.repository.HistoryRepository;
import com.movie.ycsaddas.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author walker
 * @date 2018/05/20
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@PostMapping(value = "/login")
	public String login(User user) {
		if (user != null) {
			System.out.println("当前登录用户是 ：" + user.getUsername());
			System.out.println("当前登录用户密码是 ：" + user.getPassword());
		}
		User loginUser = userRepository.findByUsername(user.getUsername());
		if (loginUser != null) {
			System.out.println("当前登录用户ID是 : " + loginUser.getId());
			UserLoginUtil.userLoginMap.put(loginUser.getId(), UserLoginUtil.login);
			UserLoginUtil.currentUser = loginUser;
		} else {
			System.out.println("用户还未注册");
			userRepository.save(user);
			User newUser = userRepository.findByUsername(user.getUsername());
			UserLoginUtil.currentUser = newUser;
			UserLoginUtil.userLoginMap.put(newUser.getId(), UserLoginUtil.login);
		}
		return "redirect:/";
	}

	@PostMapping(value = "/sign_up")
	public JsonResult sign_up(@RequestParam String username, @RequestParam String password) {
		System.out.println("username = " + username);
		System.out.println("password = " + password);
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		userRepository.save(user);
		return JsonResult.success();
	}

	@GetMapping(value = "/logout")
	public void logout(HttpServletResponse response) {
		UserLoginUtil.currentUser = null;
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/detail")
	public JsonResult test(@RequestParam String content, @RequestParam String movieId) {
		System.out.println("content = " + content);
		System.out.println("movieId = " + movieId);
		MovieComment comment = new MovieComment();
		comment.setContent(content);
		comment.setUsername(UserLoginUtil.currentUser.getUsername());
		comment.setDate(new Date());
		comment.setMovieId(movieId);
		commentRepository.save(comment);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		History history = new History();
		history.setDate(sdf.format(new Date()));
		history.setUsername(UserLoginUtil.currentUser.getUsername());
		history.setMovieId(movieId);
		history.setRate(1);
		historyRepository.save(history);

		return JsonResult.success(UserLoginUtil.currentUser.getUsername());
	}


}
