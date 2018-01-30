package com.amaker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.Transaction;

import com.amaker.bean.User;
import com.amaker.dao.UserDao;
import com.amaker.util.DBUtil;
import com.amaker.util.HibernateUtil;

public class UserDaoImpl implements UserDao {

	//转账(此方法为验证数据库事务回滚之用)
	public void trans(){
		String sql="update AccountTbl set balance=balance-1000 where username='tom'";
		String sql2="update AccountTbl set balance=balance+1000 where username='kite'";
		DBUtil util=new DBUtil();
		Connection conn=util.operConnection();
		try {
			conn.setAutoCommit(false);//禁止事务自动提交
			Statement stmt=conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);
			conn.commit();//手动提交事务
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}//回滚事务
		}finally{
			util.closeConnection(conn);
		}
	}
	
	
	@Override
	public void save(User u) {
		Session session=new HibernateUtil().getSession3();
		Transaction transaction=session.beginTransaction();//开始事务
		
		try {
			/**
			 * save()方法：
			 * 1.使一个临时对象变为持久化对象。
			 * 2.为对象分配ID。
			 * 3.在flush()缓存时会发送一条INSERT语句。
			 * 4.在save()方法之前设置的ID是无效的
			 * 5.持久化对象的ID是不能被修改的。
			 */
			session.save(u);//执行保存操作
			transaction.commit();//提交
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();//出错时回滚
		}finally{
			session.close();//关闭Hibernate的session
		}
		
//		String sql="insert into UserTbl(username,password) values(?,?)";
//		DBUtil util=new DBUtil();
//		Connection conn=util.operConnection();
//		try {
//			conn.setAutoCommit(false);//禁止事务自动提交
//			
//			PreparedStatement pstmt=conn.prepareStatement(sql);
//			pstmt.setString(1, u.getUsername());
//			pstmt.setString(2, u.getPassword());
//			pstmt.executeUpdate();
//			
//			conn.commit();//手动提交事务
//		} catch (SQLException e) {
//			e.printStackTrace();
//			try {
//				conn.rollback();//回滚事务
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		}finally{
//			util.closeConnection(conn);
//		}
	}

	@Override
	public void delete(int id) {
		Session session=new HibernateUtil().getSession3();
		Transaction transaction=session.beginTransaction();//开始事务
		try {
			User u=session.get(User.class,new Integer(id));
			session.delete(u);
			transaction.commit();//提交
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();//出错时回滚
		}finally{
			session.close();//关闭Hibernate的session
		}
		
//		String sql="delete from UserTbl where id=?";
//		DBUtil util=new DBUtil();
//		Connection conn=util.operConnection();
//		try {
//			PreparedStatement pstmt=conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			util.closeConnection(conn);
//		}
	}

	/**
	 * flush():使数据表中的记录和Session缓存中的对象的状态保持一致。为了保持一致，则可能会发送对应的SQL语句。
	 * 1.在Transaction的commit()的方法中:先调用session的flush()方法,再提交事务。
	 * 2.flush()方法可能会发送SQL语句，但不会提交事务
	 * 3.注意：在未提交事务或显式的调用session.flush()方法之前，也有可能会进行flush()操作。
	 * 1).执行HQL或QBC查询，会先进行flush()操作，以得到数据表的最新的记录
	 * 2).若记录的ID是由底层数据库使用自增的方式生成的，则在调用save()方法后，就会立即发送INSERT语句，
	 * 因为save()方法后，必须保证对象的ID 是存在的！
	 * 
	 * 
	 * reflush():会强制发送SELECT语句，以使session缓存中对象的状态和数据表中的一致。
	 * 
	 * clear():清理缓存
	 * 
	 */
	@Override
	public User get(int id) {
		Session session=new HibernateUtil().getSession3();
		try {
			//Transaction transaction=session.beginTransaction();//开始事务
			User u=(User)session.get(User.class,new Integer(id));
			//u.setPassword("666");
			//session.flush();
			//System.out.println("flush");
			//transaction.commit();
			return u;
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();//关闭Hibernate的session
		}
		return null;
		
//		String sql="select id, username, password from UserTbl where id=?";
//		DBUtil util=new DBUtil();
//		Connection conn=util.operConnection();
//		try {
//			PreparedStatement pstmt=conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//
//			ResultSet rs=pstmt.executeQuery();
//			
//			if(rs.next()){
//				User u=new User();
//				u.setId(rs.getInt(1));
//				u.setUsername(rs.getString(2));
//				u.setPassword(rs.getString(3));
//				return u;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			util.closeConnection(conn);
//		}
//		return null;
	}

	@Override
	public void update(User u) {
		Session session=new HibernateUtil().getSession3();
		Transaction transaction=session.beginTransaction();//开始事务
		
		try {
			session.update(u);//执行保存操作
			transaction.commit();//提交
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();//出错时回滚
		}finally{
			session.close();//关闭Hibernate的session
		}
		
//		String sql="update UserTbl set username=?, password=? where id=?";
//		DBUtil util=new DBUtil();
//		Connection conn=util.operConnection();
//		try {
//			PreparedStatement pstmt=conn.prepareStatement(sql);
//			pstmt.setString(1, u.getUsername());
//			pstmt.setString(2, u.getPassword());
//			
//			pstmt.setInt(3, u.getId());
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			util.closeConnection(conn);
//		}
	}

	@Override
	public List<User> list() {
		Session session=new HibernateUtil().getSession3();
		try {
			Query<User> q= session.createQuery("from User");//HQL查询
			List<User> list=q.list();
			return list;
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			session.close();//关闭Hibernate的session
		}
		return null;
		
//		String sql="select id, username, password from UserTbl";
//		DBUtil util=new DBUtil();
//		Connection conn=util.operConnection();
//		try {
//			Statement stmt=conn.createStatement();
//			ResultSet rs=stmt.executeQuery(sql);
//			
//			List<User> list =new ArrayList<User>();
//			while(rs.next()){
//				User u=new User();
//				
//				u.setId(rs.getInt(1));
//				u.setUsername(rs.getString(2));
//				u.setPassword(rs.getString(3));
//				list.add(u);
//			}
//			return list;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			util.closeConnection(conn);
//		}
//		return null;
	}
	
}
