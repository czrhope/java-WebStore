package com.czr.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		try {
			/*
			 * 利用反射通过方法名称执行相应方法
			 */
			//获得方法名
			String methodName= req.getParameter("method");
			//获得当前执行对象的class对象
			Class clazz = this.getClass();
			//通过class对象获取相应的方法
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			//通过方法对象执行方法
			method.invoke(this, req,resp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}