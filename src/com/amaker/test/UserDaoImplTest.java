package com.amaker.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
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
	 * delete()方法执行删除操作，只要OID和数据表中一条记录对应，就会准备执行delete操作；
	 * 若OID在数据表中没有对应的记录，则抛出异常。
	 * 
	 * 可以通过设置hibernate配置文件的属性 hibernate.use_indentifier_rollback 为 true,可以使得删除对象后，其OID置为null
	 */
	@Test
	public void testDelete2() {
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		News news=new News();
		news.setId(1);
		session.delete(news);
		
		transaction.commit();//提交
		session.close();
	}
	
	/**
	 * get()和load()比较：
	 * 1.执行get()方法会立即加载对象；
	 *    执行load()方法，若不使用该对象，则不会立即执行查询操作，而返回一个代理对象。
	 *    get()是立即检索，load()是延迟检索。
	 *    
	 * 2.load()方法可能会抛出LazyInitializationException异：在需要初始化代理对象之前已经关闭了Session。
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
	 * 有两个OID相同的对象。
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
	
	/**
	 * saveOrUpdate()方法对于“临时对象”执行save()，对于“游离对象”执行update()。
	 * 判断对象为临时对象的标准：
	 * 1.Java对象的OID为null
	 * 2.映射文件中为<id>设置了unsaved-value属性，并且Java对象的OID取值与这个unsaved-value属性匹配
	 * 
	 * 注意：
	 * 若OID不为null，但数据表中还没有和其对应的记录，则抛出异常。
	 */
	@Test
	public void testSaveOrUpdate(){
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		News news=new News("FFF","fff",new Date());
		news.setId(121);
		session.saveOrUpdate(news);
		
		transaction.commit();//提交
		session.close();
	}
	
	/**
	 * evict()方法：从session缓存中把指定的持久化对象移除。
	 */
	@Test
	public void testEvict(){
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		News news1=session.get(News.class, 1);
		News news2=session.get(News.class, 2);
		news1.setTitle("AA");
		news2.setTitle("BB");
		
		session.evict(news1);
		
		transaction.commit();//提交
		session.close();
	}
	
	/**
	 * 调用存储过程
	 */
	@Test
	public void testDoWork(){
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection connection) throws SQLException {
				System.out.println(connection);
				
				//调用存储过程
				
			}
		});
		
		transaction.commit();//提交
		session.close();
	}
	
	/**
	 * .hbm.xml文件的<class>属性dynamic-update设为true表示当更新一个对象时，会动态生成update语句，
	 * update语句中仅包含所有取值不为null的字。默认值为false 
	 */
	@Test
	public void testDynamicUpdate(){
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		News news=(News)session.get(News.class, 1);
		news.setAuthor("ABCD");
		
		transaction.commit();//提交
		session.close();
	}
	
	@Test
	public void testSave2(){
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		News news=new News("ZZX","zzx",new Date());
		session.save(news);
		
		transaction.commit();//提交
		session.close();
	}
	
	@Test
	public void testPropertyUpdate(){
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();
		
		News news=session.get(News.class, 1);
		//news.setTitle("PanYiWei");
		System.out.println(news.getDesc());
		
		transaction.commit();//提交
		session.close();
	}
}
