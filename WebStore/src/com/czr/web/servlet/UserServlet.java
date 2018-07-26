package com.czr.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.czr.domain.User;
import com.czr.service.UserService;

public class UserServlet extends BaseServlet{
	
	/*
	 * 校验用户名是否存在
	 */
	public void checkUsername(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		request.setCharacterEncoding("utf-8");
		String username = request.getParameter("username");
		UserService service = new UserService();
		boolean flag = service.checkUsername(username);
		String json = "{\"isExit\":"+flag+"}";
		response.getWriter().write(json);
	}
	/*
	 * 用户注册
	 */
	public void register(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		
		//首先进行验证码的校验
		String code1 = request.getParameter("checkCode");
		String code2 = (String) request.getSession().getAttribute("checkcode_session");
		request.getSession().removeAttribute("checkcode_session");
		//校验不成功，返回错误信息，成功，则进行注册
		if(!code1.equals(code2)) {
			request.getSession().setAttribute("register_info", "验证码错误");
			response.sendRedirect("register.jsp");
		}else {	
			try {
				//封装客户端用户数据
				Map<String, String[]> parameterMap = request.getParameterMap();
				User user = new User();
				BeanUtils.populate(user, parameterMap);
				user.setUid(UUID.randomUUID().toString());
				user.setCode(UUID.randomUUID().toString());
				user.setState(0);
				user.setTelephone(null);
				//调用service层方法进行注册
				UserService service = new UserService();
				boolean flag = service.register(user);
				//注册成功则跳转到等待登录页面，否则返回注册页面
				if(flag == true) {
					request.getRequestDispatcher("/register_success_info.jsp").forward(request, response);
				}else {
					request.getRequestDispatcher("/register.jsp").forward(request, response);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * 用户登录
	 */
	public void login(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		
		//首先进行验证码的校验
		String code1 = request.getParameter("checkcode");
		String code2 = (String) request.getSession().getAttribute("checkcode_session");
		request.getSession().removeAttribute("checkcode_session");
		//校验不成功，返回错误信息，成功，则进行登录
		if(!code1.equals(code2)) {
			request.getSession().setAttribute("login_info", "验证码错误");
			response.sendRedirect("login.jsp");
		}else {	
			//获取用户登录信息
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String autoLogin = request.getParameter("autoLogin");//获取是否勾选自动登录
			UserService service = new UserService();
			User user = null;
			try {
				user = service.login(username,password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//登陆成功
			if(user != null) {
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				//判断是否勾选自动登录
				if(autoLogin != null) {
					//创建cookie
					Cookie cookie_username = new Cookie("username", URLEncoder.encode(username, "utf-8"));
					Cookie cookie_password = new Cookie("password",password);
					//设置cookie的持久化时间
					cookie_username.setMaxAge(2*60*60);
					cookie_password.setMaxAge(2*60*60);
					//设置cookie的生效路径
					cookie_username.setPath(request.getContextPath());
					cookie_username.setPath(request.getContextPath());
					//返回cookie
					response.addCookie(cookie_username);
					response.addCookie(cookie_password);
				}
				response.sendRedirect(request.getContextPath());
			//登录失败
			}else {
				request.setAttribute("log_info", "用户名或密码错误");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
	}
	/*
	 * 退出登录
	 */
	public void logOut(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		/*
		 * 核心是删除客户端携带的cookie以及删除session域中的user
		 */
		Cookie cookie1 = new Cookie("username",null);
		cookie1.setMaxAge(0);
		Cookie cookie2 = new Cookie("password",null);
		cookie2.setMaxAge(0);
		//设置完的cookie一定要回写到客户端
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		request.getSession().removeAttribute("user");
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}
}
