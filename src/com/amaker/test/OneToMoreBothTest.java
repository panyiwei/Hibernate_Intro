package com.amaker.test;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.entities.n21.both.Customer;
import com.atguigu.hibernate.entities.n21.both.Order;

public class OneToMoreBothTest {

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
	public void testOneToManyBothGet(){
		//1.对n的一端的集合使用延迟加载, 可能会抛出懒加载异常 。
		Customer customer=(Customer)session.get(Customer.class, 1);
		System.out.println(customer.getCustomerName());
		//2.返回的n的一端的集合是Hibernate内置的集合类型，该类型具有延迟加载和存放代理对象的功能
		System.out.println(customer.getOrders().getClass());
	}
	
	@Test
	public void testManyToOneBothSave() {		
		Customer customer=new Customer();
		customer.setCustomerName("CC");
		Order order1=new Order();
		order1.setOrderName("ORDER-5");
		Order order2=new Order();
		order2.setOrderName("ORDER-6");
		//设定关联关系
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		
		//执行save操作:先插入Customer，再插入Order，3条INSERT，2条UPDATE
		//因为1的一端和n的一端都维护关联关系。所以会多出UPDATE
		//可以在1的一端的set节点指定inverse=true，来使1的一端放弃维护关联关系！
		session.save(customer);
		session.save(order1);
		session.save(order2);
		
//		//执行save操作:先插入Order，再插入Customer，3条INSERT，4条UPDATE
//		session.save(order1);
//		session.save(order2);
//		session.save(customer);
		
	}
	
	@Test
	public void testManyToOneBothUpdate(){
		Customer customer=(Customer)session.get(Customer.class, 1);
		customer.getOrders().iterator().next().setOrderName("GGG");
	}
	
	@Test
	public void testManyToOneBothDelete(){
		//在不设定级联关系cascade="delete" 的情况下，不能直接删除1这一端的对象
		Customer customer=(Customer)session.get(Customer.class, 3);
		session.delete(customer);
	}
	
	@Test
	public void testCascade(){
		//设定级联关系cascade="delete-orphan" 。如果一个子方对象不再和一个父方对象关联，应该把这个子方对象删除。
		Customer customer=(Customer)session.get(Customer.class, 2);
		customer.getOrders().clear();
	}

}
