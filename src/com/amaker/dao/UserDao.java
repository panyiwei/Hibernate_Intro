package com.amaker.dao;

import java.util.List;

import com.amaker.bean.User;

public interface UserDao {
	public void save(User u);
	public void delete(int id);
	public User get(int id);
	public void update(User u);
	public List<User> list();
	public void trans();
}
