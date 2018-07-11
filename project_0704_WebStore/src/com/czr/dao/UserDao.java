package com.czr.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.czr.domain.User;
import com.czr.utils.DataSourceUtils;

public class UserDao {

	public int register(User user) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into user values (?,?,?,?,?,?,?,?,?,?)";
		int row = runner.update(sql, user.getUid(),user.getUsername(),user.getPassword(),user.getName()
				,user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode());
		DataSourceUtils.closeConnection();
		return row;
	}

	public long checkUsername(String username) throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from user where username = ?;";
		long row = (long) runner.query(sql, new ScalarHandler(), username);
		return row;
	}

	public User login(String username, String password) throws SQLException {
		
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from user where username = ? and password = ?;";
		User user = runner.query(sql, new BeanHandler<User>(User.class), username,password);
		return user;	
	}
}
