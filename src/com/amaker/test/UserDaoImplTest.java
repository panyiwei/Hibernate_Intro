package com.amaker.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.bean.User;
import com.amaker.dao.UserDao;
import com.amaker.dao.impl.UserDaoImpl;

public class UserDaoImplTest {

	UserDao dao=new UserDaoImpl();
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testSave() {
		User u=new User();
		u.setUsername("yy");
		u.setPassword("321");
		dao.save(u);
	}

	@Test
	public void testDelete() {
		dao.delete(1);
	}

	@Test
	public void testGet() {
		User u=dao.get(1);
		System.out.println("username:"+u.getUsername());
		System.out.println("password:"+u.getPassword());
	}

	@Test
	public void testUpdate() {
		User u=new User();
		u.setId(1);
		u.setUsername("lili");
		u.setPassword("999");
		
		dao.update(u);
	}

	@Test
	public void testList() {
		List<User> list=dao.list();
		System.out.println(list.size());
	}
	
	@Test
	public void testTrans(){
		dao.trans();
	}

}
