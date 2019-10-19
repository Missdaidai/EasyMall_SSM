package com.easymall.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.easymall.domain.User;
import com.easymall.service.UserService;

public class AutoLoginFilter implements Filter{

	//Spring容器
	private WebApplicationContext context = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//获取Spring容器
		ServletContext sc = filterConfig.getServletContext();
		context = WebApplicationContextUtils.getWebApplicationContext(sc);
	}

	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		//未登录
		User user = (User) req.getSession().getAttribute("user");
		if(user==null) {
			//有自动登录cookie
			Cookie [] cs = req.getCookies();
			Cookie findC = null;
			if(cs!=null) {
				for(Cookie c : cs) {
					if("autologin".equals(c.getName())) {
						findC = c;
						break;
					}
				}
			}
			if(findC!=null) {
				//自动登录cookie中用户名密码正确
				String username = URLDecoder.decode(findC.getValue().split("#")[0],"utf-8");
				String password = findC.getValue().split("#")[1];
				UserService userService = context.getBean(UserService.class);
				User finser = userService.loginUser(username, password);
				if(finser!=null) {
					//自动登录
					req.getSession().setAttribute("user", finser);
				}
			}
		}
		
		//放行访问
		chain.doFilter(request, response);
	}

}
