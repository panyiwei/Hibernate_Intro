package com.amaker.test;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.joined.subclass.Person;
import com.atguigu.hibernate.joined.subclass.Student;

public class JoinedSubclassTest {

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
	 * joined-subclass继承的优点：
	 * 1.不需要使用辨别者列。
	 * 2.子类独有的字段能添加非空约束。
	 * 3.没有冗余的字段。
	 */
	
	/**
	 * 查询：
	 * 1.查询父类记录，做一个左外连接查询。
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
