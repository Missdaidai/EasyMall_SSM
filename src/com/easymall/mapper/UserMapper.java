package com.easymall.mapper;

import com.easymall.domain.User;

public interface UserMapper {

	public User findUser(User user);
	
	public void addUser(User user);
}