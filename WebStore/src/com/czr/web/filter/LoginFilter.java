package com.czr.web.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.czr.domain.User;
import com.czr.service.UserService;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//强转
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		//获取request中的cookie
		Cookie[] cookies = httpRequest.getCookies();
		if(cookies != null) {
			String username = null;
			String password = null;
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("username")) {
					username = URLDecoder.decode(cookie.getValue(), "utf-8");
				}
				if(cookie.getName().equals("password")) {
					password = cookie.getValue();
				}
			}
			//如果拿到用户名和密码则进行自动登录
			if(username != null && password != null) {
				
				User user = null;
				try {
					user= new UserService().login(username, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//如果登陆成功
				if(user != null) {
					//登陆成功则将user存入sessino域中
					HttpSession session = httpRequest.getSession();
					session.setAttribute("user", user);
				}
			}
		}
		//最后filter放行
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
