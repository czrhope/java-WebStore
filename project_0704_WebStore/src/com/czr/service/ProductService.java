package com.czr.service;

import java.sql.SQLException;
import java.util.List;

import com.czr.dao.ProductDao;
import com.czr.domain.Order;
import com.czr.domain.PageBean;
import com.czr.domain.Product;

public class ProductService {

	private ProductDao dao = new ProductDao();
	
	public int totalRecord(String cid) {
		
		long totalRecord;
		try {
			totalRecord = dao.totalRecord(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return new Long(totalRecord).intValue();
	}

	public List<Product> findCategoryProduct(PageBean pageBean, String cid) {
		
		//对分页业务进行处理
		int start = (pageBean.getPageIndex()-1)*pageBean.getPageContent();
		int end = pageBean.getPageContent();
		try {
			return dao.findCategoryProduct(start,end,cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public List<Product> findHotProduct() {
		
		try {
			return dao.findHotProduct();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Product> findNewProduct() {
		
		try {
			return dao.findNewProduct();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Product getProduct_info(String pid) {
		
		try {
			return dao.getProduct_info(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
