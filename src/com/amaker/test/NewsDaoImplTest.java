package com.amaker.test;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amaker.bean.News;
import com.amaker.dao.NewsDao;
import com.amaker.dao.impl.NewsDaoImpl;

public class NewsDaoImplTest {

	NewsDao dao=new NewsDaoImpl();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSave() {
		News n=new News("Java", "PanYiwei", new Date(new java.util.Date().getTime()));
		dao.save(n);
	}

}
