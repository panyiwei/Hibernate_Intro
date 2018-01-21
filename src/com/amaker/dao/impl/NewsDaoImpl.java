package com.amaker.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.amaker.bean.News;
import com.amaker.dao.NewsDao;
import com.amaker.util.HibernateUtil;

public class NewsDaoImpl implements NewsDao {

	@Override
	public void save(News n) {
		Session session=new HibernateUtil().getSession5();
		Transaction transaction=session.beginTransaction();//开启事务
		try{
			session.save(n);//执行保存操作
			transaction.commit();//提交
		}catch(HibernateException e){
			e.printStackTrace();
			transaction.rollback();//出错时回滚
		}finally{
			session.close();//关闭Hibernate的session
		}
		
	}
	
}
