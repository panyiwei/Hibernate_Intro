package com.amaker.test;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.union.subclass.Person;
import com.atguigu.hibernate.union.subclass.Student;

public class UnionSubclassTest {

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
	 * union-subclass的优点：
	 * 1.不需要使用辨别者列。
	 * 2.子类独有的字段能添加非空约束。
	 * 
	 * union-subclass的缺点：
	 * 1.存在冗余的字段。
	 * 2.若更新父表的字段，则更新效率较低。
	 */
	
	
	@Test
	public void testUpdate(){
		String hql="UPDATE Person p SET p.age=20";
		session.createQuery(hql).executeUpdate();
	}
	
	/**
	 * 查询：
	 * 1.查询父类记录，需把父表和字表记录汇总到一起再做查询,性能稍差。
	 * 2.对于子类记录，做一个内连接查询。
	 */
	@Test
	public void testQuery(){
		List<Person> persons=session.createQuery("FROM Person").list();
		System.out.println(persons.size());
		
		List<Student> stus=session.createQuery("FROM Student").list();
		System.out.println(stus.size());
	}
	
	
	/**
	 * 插入操作：
	 * 1.对于子类对象至少需要插入到两张数据表中。
	 */
	@Test
	public void testSave(){
		Person person=new Person();
		person.setAge(11);
		person.setName("AA");
		session.save(person);
		
		Student stu=new Student();
		stu.setAge(22);
		stu.setName("BB");
		stu.setSchool("ATGUIGU");
		session.save(stu);
	}
	

}
