package com.czr.web.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.czr.domain.Cart;
import com.czr.domain.CartItem;
import com.czr.domain.Order;
import com.czr.domain.OrderItem;
import com.czr.domain.User;
import com.czr.service.ProductService;
import com.czr.service.TransactService;

public class TransactionServlet extends BaseServlet{
	
	/*
	 * 加入购物车
	 */
	public void addToCart(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//获取表单数据
		String pid = request.getParameter("pid");
		String pimage = request.getParameter("pimage");
		String pname = request.getParameter("pname");
		double shop_price = Double.parseDouble(request.getParameter("shop_price"));
		int orderNum = Integer.parseInt(request.getParameter("orderNum"));
		double sum_price = shop_price * orderNum;
		//判断购物车是否存在，没有则创建
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart == null) {
			cart = new Cart();
		}
		/*
		 * 在加入购物车之前判断购物车中是否存在该商品
		 */
		//如果存在该商品，则在原商品基础上增加数量，如果不存在，封装该商品并加入购物车
		Map<String,CartItem> map = cart.getMap();
		if(map.containsKey(pid)) {
			map.get(pid).setOrderNum(map.get(pid).getOrderNum()+orderNum);
			cart.setSumMoney(cart.getSumMoney()+sum_price);
			map.get(pid).setSum_price(map.get(pid).getSum_price()+sum_price);
			request.getSession().setAttribute("cart", cart);
		}else {
			/*
			 * 封装cartItem对象
			 */
			CartItem item = new CartItem();
			item.setPid(pid);
			item.setPimage(pimage);
			item.setPname(pname);
			item.setShop_price(shop_price);
			item.setOrderNum(orderNum);
			item.setSum_price(sum_price);
			//加入购物车
			map.put(pid, item);
			cart.setSumMoney(sum_price+cart.getSumMoney());
			request.getSession().setAttribute("cart", cart);
		}
		response.sendRedirect("cart.jsp");
	}
	/*
	 * 从购物车中删除
	 */
	public void removeItem(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//获取购物车与要删除的商品id
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		String pid = request.getParameter("pid");
		//删除
		cart.setSumMoney(cart.getSumMoney()-cart.getMap().get(pid).getSum_price());
		cart.getMap().remove(pid);
		//重新放入session域中并重定向
		request.getSession().setAttribute("cart", cart);
		response.sendRedirect("cart.jsp");
	}
	/*
	 * 清空购物车
	 */
	public void clearCart(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		request.getSession().removeAttribute("cart");
		response.sendRedirect("cart.jsp");
	}
	/*
	 * 提交订单
	 */
	public void subOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//判断购物车中是否有商品
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart == null) {
			response.sendRedirect("cart.jsp");
		}else {
			/*
			 * 封装order对象
			 */
			Order order = new Order();
			String oid = UUID.randomUUID().toString();
			String ordertime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
			double total = cart.getSumMoney();
			int state = 0;
			User user = (User) request.getSession().getAttribute("user");
			order.setOid(oid);
			order.setOrdertime(ordertime);
			order.setTotal(total);
			order.setState(state);
			order.setUser(user);
			//封装orderItem对象并放入order中
			List<OrderItem> orderItems = order.getOrderItems();
			Map<String, CartItem> itemMap = cart.getMap();
			for(String key:itemMap.keySet()) {
				CartItem cartItem = itemMap.get(key);
				OrderItem orderItem = new OrderItem();
				orderItem.setItemid(UUID.randomUUID().toString());
				orderItem.setCount(cartItem.getOrderNum());
				orderItem.setSubtotal(cartItem.getSum_price());
				orderItem.setProduct(new ProductService().getProduct_info(cartItem.getPid()));
				orderItem.setOrder(order);
				orderItems.add(orderItem);
			}
			//order对象封装完毕，将数据保存到数据库中
			TransactService service = new TransactService();
			boolean flag = service.subOrder(order);
			//订单提交成功后清空购物车并跳转到订单页面，失败则返回购物车
			if(flag == true) {
				request.getSession().removeAttribute("cart");
				request.getSession().setAttribute("order", order);
				response.sendRedirect("order_info.jsp");
			}else {
				response.sendRedirect("cart.jsp");
			}
		}
	}
	/*
	 * 查找订单
	 */
	public void findOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		TransactService service = new TransactService();
		User user = (User) request.getSession().getAttribute("user");
		List<Order> orderList = service.findOrderList(user);
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("order_list.jsp").forward(request, response);
	}
}