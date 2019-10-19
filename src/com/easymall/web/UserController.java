package com.easymall.web;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easymall.domain.User;
import com.easymall.exception.MsgException;
import com.easymall.service.UserService;
import com.easymall.utils.MD5Utils;
import com.easymall.utils.VerifyCode;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService = null;
	
	/**
	 * 登出用户
	 * @return 
	 */
	@RequestMapping("/logout.action")
	public String logout(HttpSession session,HttpServletRequest request,HttpServletResponse response) {
		//--注销
		session.invalidate();
		//--删除30天内自动登录cookie
		Cookie autologinc = new Cookie("autologin","");
		autologinc.setPath(request.getContextPath()+"/");
		autologinc.setMaxAge(0);
		response.addCookie(autologinc);
		//--重定向回主页
		return "redirect:/index.jsp";
	}
	
	/**
	 * 登录用户
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping("/login.action")
	public String login(String username,String password,HttpSession session,String remname,String autologin,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//1.乱码解决
		//2.获取请求参数
		//3.记住用户名处理
		if("true".equals(remname)) {
			Cookie remnamec = new Cookie("remname",URLEncoder.encode(username,"utf-8"));
			remnamec.setPath(request.getContextPath()+"/");
			remnamec.setMaxAge(60 * 60 * 24 * 30);
			response.addCookie(remnamec);
		}
		//4.30天内自动登录处理
		if("true".equals(autologin)) {
			Cookie autologinc = new Cookie("autologin",URLEncoder.encode(username,"utf-8")+"#"+MD5Utils.md5(password));
			autologinc.setPath(request.getContextPath()+"/");
			autologinc.setMaxAge(60 * 60 * 24 * 30);
			response.addCookie(autologinc);
		}
		//5.调用Service进行登录
		User user = userService.loginUser(username,MD5Utils.md5(password));
		if(user!=null) {
			//6.登录成功回到主页
			session.setAttribute("user", user);
			return "redirect:/index.jsp";
		}else {
			//6.登录失败回登录页面提示
			model.addAttribute("msg","用户名密码不正确！");
			return "forward:/WEB-INF/jsp/login.jsp";
		}
	}
	
	/**
	 * 注册用户
	 */
	@RequestMapping("/regist.action")
	public String regist(User user,String password2,String valistr,Model model,HttpSession session) {
		//1.乱码解决
		//2.校验验证码
		String code = (String) session.getAttribute("code");
		if(code == null || !code.equals(valistr)) {
			model.addAttribute("msg","验证码不正确！");
			return "forward:/WEB-INF/jsp/regist.jsp";
		}
		//3.获取参数封装数据到bean
		//4.校验数据
		//5.调用Service完成注册
		try{
			user.setPassword(MD5Utils.md5(user.getPassword()));
			userService.registUser(user);
		}catch(MsgException e){
			model.addAttribute("msg", e.getMessage());
			return "forward:/WEB-INF/jsp/regist.jsp";
		}
		//6.注册完成后，跳转到网站首页
		return "redirect:/index.jsp";
	}

	/**
	 * Ajax校验用户名
	 * @param username
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="ajaxCheckUserName.action",produces="application/json;charset=utf-8")
	public String ajaxCheckUserName(String username) {
		//调用Service查询用户名是否可用
		if(userService.userNameIsOK(username)) {
			return "用户名可用";
		}else {
			return "用户名不可用";
		}
	}
	
	/**
	 * 获取验证码
	 */
	@RequestMapping("/valiImg.action")
	public void valiImg(HttpServletResponse response,HttpSession session) throws Exception {
		//1.控制浏览器不使用缓存
        response.setDateHeader("Expires",-1);
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("pragma","no-cache");
        //2.生成验证码图片。
        VerifyCode vc = new VerifyCode();
        vc.drawImage(response.getOutputStream());
        //3.获取验证码图片字符
        String code = vc.getCode();
        //4.将生成的验证码加入session域中
        session.setAttribute("code",code);
        System.out.println(code);
	} 
	@Test
	public void test01() {
		System.out.println(userService);
	}
}
