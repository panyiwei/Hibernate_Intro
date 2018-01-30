package com.amaker.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.bean.News;
import com.amaker.bean.User;
import com.amaker.dao.UserDao;
import com.amaker.dao.impl.UserDaoImpl;
import com.amaker.util.HibernateUtil;

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
	
	/**
	 * get()和load()比较：
	 * 1.执行get()方法会立即加载对象；
	 *    执行load()方法，若不使用该对象，则不会立即执行查询操作，而返回一个代理对象。
	 *    get()是立即检索，load()是延迟检索。
	 *    
	 * 2.load()方法可能会抛出LazyInitializationException异：在需要初始化代理对象之前已经关闭了Session
	 *    
	 * 3.若数据表中没有对应的记录，且Session也没有被关闭：
	 *    get()返回null；
	 *    load()需要使用对象时抛出异常。
	 */
	@Test
	public void testLoad(){
		
		
		Session session=new HibernateUtil().getSession5();
		News news=(News)session.load(News.class, 1);
		System.out.println(news.getClass().getName());
		session.close();
	}
	
	@Test
	public void testGet2(){
		Session session=new HibernateUtil().getSession5();
		News news=(News)session.get(News.class, 1);
		System.out.println(news.getClass().getName());
		session.close();
	}

	@Test
	public void testGet() {
		User u=dao.get(1);
		System.out.println("username:"+u.getUsername());
		System.out.println("password:"+u.getPassword());
	}
	
	/**
	 * update():
	 * 1.更新一个持久化对象，不需要显示的调用update()，因为在调用Transaction的commit()方法时，会先执行session的flush()方法。
	 * 2.更新一个游离对象，需要显示的调用session的update()方法，可以把游离对象变成持久化对象。
	 * 
	 * 需要注意的：
	 * 1.更新一个游离对象,无论游离对象和数据表的记录是否一致，都会发送UPDATE语句。
	 *    如何能让update()方法不盲目的触发UPDATE语句呢？在.hbm.xml文件的class节点设置select-before-update="true"(默认是false)
	 *    
	 * 2.在数据表中没有对应的记录时调用update()方法会抛出异常。
	 * 
	 * 3.当update()方法关联一个游离对象时，如果在session缓存中已经存在相同OID的持久化对象，会抛出异常，因为在session缓存中不能
	 * 由两个OID相同的对象。
	 * 
	 */
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
	
	/**
	 * persist()：也会执行INSERT操作
	 * 在persist()方法之前若对象已经有ID，则不会执行INSERT，而是抛出异常。
	 */
	@Test
	public void testPersist(){
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		News news=new News();
		news.setTitle("EE");
		news.setAuthor("ee");
		news.setDate(new Date());
		//news.setId(77);
		
		System.out.println(news);
		
		
		session.persist(news);
		
		transaction.commit();//提交
		session.close();
	}

}
