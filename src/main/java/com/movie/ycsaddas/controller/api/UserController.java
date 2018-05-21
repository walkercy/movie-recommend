package com.movie.ycsaddas.controller.api;

import com.movie.ycsaddas.common.JsonResult;
import com.movie.ycsaddas.common.UserLoginUtil;
import com.movie.ycsaddas.entity.User;
import com.movie.ycsaddas.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

	@PostMapping(value = "/login")
	public String login(User user) {
		if (user != null) {
			System.out.println("当前登录用户是 ：" + user.getUsername());
			System.out.println("当前登录用户密码是 ：" + user.getPassword());
		}
		User loginUser = userRepository.findByUsername(user.getUsername());
		if (loginUser != null) {
			System.out.println("当前登录用户ID是 : " + loginUser.getId());
		}
		UserLoginUtil.userLoginMap.put(loginUser.getId(), UserLoginUtil.login);
		UserLoginUtil.currentUser = loginUser;
		return "redirect:/index";
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
			response.sendRedirect("/index");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
