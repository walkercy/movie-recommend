package com.movie.ycsaddas.common;

import com.movie.ycsaddas.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author walker
 * @date 2018/05/20
 */
public class UserLoginUtil {
	public static Map<Integer, Integer> userLoginMap = new HashMap<>();

	public static User currentUser;
	public static final int login = 1;
	public static final int logout = 0;
}
