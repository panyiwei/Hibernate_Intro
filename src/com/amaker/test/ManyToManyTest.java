package com.amaker.test;

import static org.junit.Assert.*;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.n2n.Category;
import com.atguigu.hibernate.n2n.Item;

public class ManyToManyTest {

	Session session=null;
	Transaction transaction=null;
	@Before
	public void setUp() throws Exception {
		session=new HibernateUtil().getSession5();
		transaction=session.beginTransaction();
	}

	@After
	public void tearDown() throws Exception {
		transaction.commit();//提交
		session.close();
	}
	
	@Test
	public void testGet(){
		Category category=(Category)session.get(Category.class, 1);
		System.out.println(category.getName());
		
		//需要连接中间表
		Set<Item> items=category.getItems();
		System.out.println(items.size());
	}

	@Test
	public void testSave(){
		Category category1=new Category();
		category1.setName("C-AA");
		Category category2=new Category();
		category2.setName("C-BB");
		
		Item item1=new Item();
		item1.setName("I-AA");
		Item item2=new Item();
		item2.setName("I-BB");
		
		//设定关联关系
		category1.getItems().add(item1);
		category1.getItems().add(item2);
		category2.getItems().add(item1);
		category2.getItems().add(item2);
		
		//执行保存操作
		session.save(category1);
		session.save(category2);
		session.save(item1);
		session.save(item2);
	}
	
	
	

}
