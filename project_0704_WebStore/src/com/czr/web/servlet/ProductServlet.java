package com.czr.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.czr.domain.Category;
import com.czr.domain.PageBean;
import com.czr.domain.Product;
import com.czr.service.CategoryService;
import com.czr.service.ProductService;
import com.google.gson.Gson;

public class ProductServlet extends BaseServlet {
	
	/*
	 * 导航栏中获取分类列表的方法
	 */
	public void getCategoryList(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//从数据库获得类别信息的集合
		CategoryService service = new CategoryService();
		List<Category> categoryList = service.fingAllCategory();
		//将集合封装成json格式字符串
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		//返回到前端页面
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(json);
	}
	/*
	 * 获取首页中显示的商品信息
	 */
	public void getIndexProduct(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		
		/*
		 * 获得热门商品数据
		 */
		ProductService service = new ProductService();
		List<Product> hotProductList = service.findHotProduct();
		/*
		 * 获得最新商品数据
		 */
		List<Product> newProductList = service.findNewProduct();
		request.setAttribute("hotProductList", hotProductList);
		request.setAttribute("newProductList", newProductList);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	/*
	 * 获取商品详细信息
	 */
	public void getProduct_info(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		
		String pid = request.getParameter("pid");
		ProductService service = new ProductService();
		Product product = service.getProduct_info(pid);
		request.setAttribute("product", product);
		request.getRequestDispatcher("product_info.jsp").forward(request, response);
	}
	/*
	 * 获得对应分类的商品列表
	 */
	public void getProductList(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		
		//从页面获取类别信息,并写入到request域中
		String cid = request.getParameter("cid");
		request.setAttribute("cid", cid);
		/*
		 * 封装分页信息pageBean
		 */
		PageBean pageBean = new PageBean();
		//每页显示个数
		int pageContent = 12;
		pageBean.setPageContent(pageContent);
		//当前页
		int pageIndex;
		if(request.getParameter("pageIndex") == null) {
			pageIndex = 1;
		}else {
			pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		}
		pageBean.setPageIndex(pageIndex);
		//总记录数
		ProductService service = new ProductService();
		int totalRecord = service.totalRecord(cid);
		pageBean.setTotalRecord(totalRecord);
		//总页数
		int totalPage = (int) Math.ceil(1.0*totalRecord/pageContent);
		pageBean.setTotalPage(totalPage);
		//将pageBean放入request域中
		request.setAttribute("pageBean", pageBean);
		/*
		 * 根据pageBean和类别信息去数据库中查找商品信息
		 */
		List<Product> productList = service.findCategoryProduct(pageBean,cid);
		request.setAttribute("productList", productList);
		//转发给页面显示
		request.getRequestDispatcher("product_list.jsp").forward(request, response);
	}
}
