package com.acme;

import java.io.File;
import java.util.Set;

import org.apache.log4j.Logger;

public class TestClass3 {

	private static final Logger LOG = Logger.getLogger(TestClass3.class);

	public TestClass3(File bla) {
		LOG.info("bla=" + bla);
	}

	public void testMethod1(String bla) {
		LOG.info("bla=" + bla);
	}

	public void testMethod2(Set bla, int bli) {
		LOG.info("bla=" + bla + " bli=" + bli);
	}
}
