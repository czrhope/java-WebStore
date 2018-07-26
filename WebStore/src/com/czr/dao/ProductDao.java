package com.czr.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.czr.domain.Product;
import com.czr.utils.DataSourceUtils;

public class ProductDao {

	public long totalRecord(String cid) throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid = ?;";
		return (long) runner.query(sql, new ScalarHandler(),cid);
	}

	public List<Product> findCategoryProduct(int start, int end, String cid) throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid = ? limit ?,?;";
		List<Product> productList = runner.query(sql, new BeanListHandler<Product>(Product.class),cid,start,end);
		return productList;
	}

	public List<Product> findHotProduct() throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot = 1 limit ?,?;";
		List<Product> hotProductList = runner.query(sql, new BeanListHandler<Product>(Product.class),0,9);
		return hotProductList;
	}

	public List<Product> findNewProduct() throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?";
		List<Product> newProductList = runner.query(sql, new BeanListHandler<Product>(Product.class),0,9);
		return newProductList;
	}

	public Product getProduct_info(String pid) throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid = ?;";
		Product product = runner.query(sql, new BeanHandler<Product>(Product.class),pid);
		return product;
	}

}
