package com.amaker.test;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.util.HibernateUtil;
import com.atguigu.hibernate.one2one.foreign.Department;
import com.atguigu.hibernate.one2one.foreign.Manager;

public class OneToOneForeignTest {

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
		
		//2.查询Manager对象的连接条件应该是department.MGR_ID=manager.MGR_ID，而不应该
		//是department.DEPT_ID=manager.MGR_ID。需要在Manager.hbm.xml中加上 property-ref="mgr"
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
		department.setDeptName("DEPT-BB");
		
		Manager manager=new Manager();
		manager.setMgrName("MGR-BB");
		
		//设定关联关系
		department.setMgr(manager);
		manager.setDepat(department);
		
//		//保存操作
//		//建议先保存没有外键列的那个对象，这样可以减少UPDATE语句。
//		session.save(manager);
//		session.save(department);
		//保存操作2
		session.save(department);
		session.save(manager);
		
	}
	

}
