package com.easymall.service;

import com.easymall.domain.User;
import com.easymall.exception.MsgException;

public interface UserService {

	/**
	 * 注册用户
	 * @param user 封装了用户数据的bean
	 */
	public void registUser(User user) throws MsgException;

	/**
	 * 检验用户名是否可用
	 * @param username 要检查的用户名
	 * @return 可用就返回true否则false
	 */
	public boolean userNameIsOK(String username);

	/**
	 * 登录用户
	 * @param username 要登录的用户名
	 * @param password 要登录的密码
	 * @return 如果登录成功返回用户bean，如果失败返回null
	 */
	public User loginUser(String username, String password);
}
