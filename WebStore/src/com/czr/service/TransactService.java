package com.czr.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.czr.dao.TransactDao;
import com.czr.domain.Order;
import com.czr.domain.User;
import com.czr.utils.DataSourceUtils;

public class TransactService {
	
	/*
	 * 由于往orders表和orderItem表存入数据时必须同时成功，所以需要开启事务控制
	 */

	public boolean subOrder(Order order) {
		
		boolean flag = false;
		
		TransactDao dao = new TransactDao();
		try {
			//开启事务
			DataSourceUtils.startTransaction();
			//存订单
			dao.addOrder(order);
			//存订单项
			dao.addOrderItem(order);
			//走到这里则表示提交成功
			flag = true;
			
		} catch (SQLException e) {
			//如果上面任何一条执行失败，则回滚事务
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			//始终提交事务
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public List<Order> findOrderList(User user) {
		
		TransactDao dao = new TransactDao();
		List<Order> orderList = null;
		try {
			orderList =  dao.findOrderList(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return orderList;
	}

}
