package com.amaker.test;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.one2one.primary.Department;
import com.atguigu.hibernate.one2one.primary.Manager;

public class OneToOnePrimaryTest {

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
		//1.默认情况下对关联属性使用懒加载；所以可能会出现懒加载异常的问题。
		Department department=(Department)session.get(Department.class, 1);
		System.out.println(department.getDeptName());
		
		Manager manager=department.getMgr();
		System.out.println(manager.getMgrName());
	}
	
	@Test
	public void testGet2(){
		//在查询没有外键的实体对象时，使用的是左外连接查询，一并查询出其关联的对象，并已经进行初始化。
		Manager manager=(Manager)session.get(Manager.class, 1);
		System.out.println(manager.getMgrName());
		System.out.println(manager.getDepat().getDeptName());
	}
	
	@Test
	public void testSave(){
		Department department=new Department();
		department.setDeptName("DEPT-CC");
		
		Manager manager=new Manager();
		manager.setMgrName("MGR-CC");
		
		//设定关联关系
		department.setMgr(manager);
		manager.setDepat(department);
		
//		//保存操作
//		//先保存哪一个都不会有多余的UPDATE语句。
//		session.save(manager);
//		session.save(department);
		//保存操作2
		session.save(department);
		session.save(manager);
		
	}
	

}
