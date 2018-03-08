package com.amaker.test;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.strategy.Customer;
import com.atguigu.hibernate.strategy.Order;

public class StrategyTest {
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
	
	
	/**
	 * 类级别的检索策略
	 */
	@Test
	public void testClassLevelStrategy(){
		//.hbm.xml中class的lazy属性可控制是“立即检索加载”还是“懒加载”。
		//只对load()方法有效，get()方法和Query的list()方法在类级别总是使用立即检索
		Customer customer=(Customer)session.load(Customer.class, 1);
		System.out.println(customer.getClass());
		
		System.out.println(customer.getCustomerId());
		System.out.println(customer.getCustomerName());
	}
	
	//----------------------------------------分割线---------------------------------------------------------------
	
	/**
	 * 一对多、多对多中的检索策略
	 */
	@Test
	public void testOneToManyLevelStrategy(){
		Customer customer=(Customer)session.get(Customer.class, 1);
		System.out.println(customer.getCustomerName());
		
		System.out.println(customer.getOrders().size());
		Order order=new Order();
		order.setOrderId(1);
		System.out.println(customer.getOrders().contains(order));
		
		//-----------------------------set的lazy属性------------------------------
		//1. 1-n或n-n的集合属性默认使用懒加载检索策略。
		//2. 可以通过设置.hbm.xml中set的lazy属性来修改默认的检索策略。默认为true，但建议使用默认值。
		//3. lazy还可以设置为extra 增强的延迟检索,该取值会尽可能的延迟集合初始化的时机。
	}
	
	/**
	 * 批量初始化
	 */
	@Test
	public void testSetBatchSize(){
		List<Customer> customers=session.createQuery("FROM Customer").list();
		System.out.println(customers.size());
		
		for(Customer customer :customers){
			if(customer.getOrders()!=null){
				System.out.println(customer.getOrders().size());
			}
		}
		//set元素的batch-size属性，设定一次初始化set集合的数量。
	}
	
	@Test
	public void testSetFetch(){
		List<Customer> customers=session.createQuery("FROM Customer").list();
		System.out.println(customers.size());
		
		for(Customer customer :customers){
			if(customer.getOrders()!=null){
				System.out.println(customer.getOrders().size());
			}
		}
		
		//set集合的fetch属性：其作用  是确定初始化set集合的方式。
		//1. 默认值为select。通过正常的方式来初始化set集合。
		//2. 当取值为subselect时，通过子查询的方式来初始所有的set集合。子查询作为where子句的 in 的条件出现,
		//子查询查询所有1的一端的ID；当取值为subselect时，batch-size属性失效。
		//3. 当取值为join时，则：
		//3_1.在加载 1 的一端的对象时，使用迫切左外连接(使用左外连接进行查询，且把集合属性进行初始化)方式检索 n 的一端的集合属性。
		//3_2. 忽略lazy属性
		//3_3. HQL 查询忽略fetch=join的取值
	}
	
	@Test
	public void testSetFetch2(){
		Customer customer=(Customer)session.get(Customer.class, 1);
		System.out.println(customer.getOrders().size());
	}
	
	
	//----------------------------------------分割线---------------------------------------------------------------
	
	/**
	 * 多对一中的检索策略
	 */
	@Test
	public void testManyToOneStrategy(){
		Order order=(Order)session.get(Order.class, 1);
		System.out.println(order.getCustomer().getCustomerName());
	}
	
	@Test
	public void testClassBatchSize(){
		List<Order> orders=session.createQuery("FROM Order o").list();
		for(Order order:orders){
			if(order.getCustomer()!=null){
				System.out.println(order.getCustomer().getCustomerName());
			}
		}
		//batch-size这个属性需要设置在 1 那一端的class元素中
		//<class name="Customer" table="CustomersTbl" lazy="true" batch-size="5">
		//作用：一次初始化 1 的这一端代理对象的个数。
	}
}
