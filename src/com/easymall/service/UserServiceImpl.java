package com.easymall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easymall.domain.User;
import com.easymall.exception.MsgException;
import com.easymall.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper = null;

	@Transactional
	@Override
	public void registUser(User user) throws MsgException {
		//用户名是否存在
        User findUser =  userMapper.findUser(new User(0,user.getUsername(),null,null,null));

        if(findUser != null){
            //如果用户名存在，作出用户已存在的提示
            //用户名存在，向上抛出一个自定义异常，其中可以反馈错误信息。
            throw new MsgException("用户名已存在");
        }else{
            //如果用户名不存在，则完成注册。
        	userMapper.addUser(user);
        }		
	}

	@Override
	public boolean userNameIsOK(String username) {
		User user = userMapper.findUser(new User(0,username,null,null,null));
		return user==null;
	}

	@Override
	public User loginUser(String username, String password) {
		return userMapper.findUser(new User(0,username,password,null,null));
	}

}
