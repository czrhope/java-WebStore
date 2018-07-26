package com.czr.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.czr.domain.Order;
import com.czr.domain.OrderItem;
import com.czr.domain.Product;
import com.czr.domain.User;
import com.czr.utils.DataSourceUtils;

public class TransactDao {
	
	QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
	
	public void addOrder(Order order) throws SQLException {
		
		String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		//如果想要实现事务控制的话在执行更新操作的时候必须要带上从DataSourceUtils中获取的Connection
		runner.update(conn,sql, order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),
				order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
	}

	public void addOrderItem(Order order) throws SQLException {
		
		String sql = "insert into orderitem values(?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem item:orderItems) {
			runner.update(conn, sql, item.getItemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getOrder().getOid());
		}
	}

	public List<Order> findOrderList(User user) throws SQLException, IllegalAccessException, InvocationTargetException {
		
		String sql = "select * from orders where uid = ?;";
		List<Order> orderList = runner.query(sql, new BeanListHandler<Order>(Order.class),user.getUid());
		for(Order order:orderList) {
			String oid = order.getOid();
			String sql_1 = "select p.pimage,p.pname,p.shop_price,i.count,i.subtotal from orderitem i,product p where i.pid = p.pid and i.oid = ?;";
			List<Map<String, Object>> mapList = runner.query(sql_1, new MapListHandler(), oid);
			for(Map map:mapList) {
				//封装product对象
				Product product = new Product();
				BeanUtils.populate(product, map);
				//封装orderItem对象
				OrderItem orderItem = new OrderItem();
				BeanUtils.populate(orderItem, map);
				orderItem.setProduct(product);
				//将orderItem对象放到order中
				order.getOrderItems().add(orderItem);
			}
		}
		//封装完毕，返回
		return orderList;
	}

}
