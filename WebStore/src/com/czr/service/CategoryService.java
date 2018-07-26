package com.czr.service;

import java.sql.SQLException;
import java.util.List;

import com.czr.dao.CategoryDao;
import com.czr.domain.Category;

public class CategoryService {
	
	CategoryDao dao = new CategoryDao();

	public List<Category> fingAllCategory() {
		List<Category> list = null;
		try {
			list = dao.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
