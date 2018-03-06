package com.amaker.test;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.subclass.Person;
import com.atguigu.hibernate.subclass.Student;

public class SubclassTest {

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
	 * subclass继承的缺点：
	 * 1.使用了额外的一列——辨别者列。
	 * 2.子类独有的字段不能添加非空约束。
	 * 3.若继承层次较深，则数据表的字段也会较多。
	 */
	
	/**
	 * 查询：
	 * 1.查询父类记录，只需要查询一张数据表。
	 * 2.对于子类记录，也只需要查询一张数据表。
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
	 * 1.对于子类对象只需把记录插入到一张数据表中。
	 * 2.辨别者列由Hibernate自动维护。
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
