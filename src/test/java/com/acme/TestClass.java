package com.acme;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.log4j.Logger;

public class TestClass {

	private static final Logger LOG = Logger.getLogger(TestClass.class);

	public TestClass(Integer bla, Double bli) {

		LOG.info("bla=" + bla + " bli=" + bli);
		bli.intValue();
	}

	public TestClass(Double bla) {
		LOG.info("bla=" + bla);
	}
	
	public TestClass(int[] bla) {
		LOG.info("bla=" + bla);
	}

	public TestClass() {
		LOG.info("invoked");
	}

	private TestClass(String bla) {
		LOG.info("bla=" + bla);
	}

	public void testMethod1(String bla) {
		LOG.info("bla=" + bla);
	}

	public void testMethod2(FileOutputStream bla, int bli) {
		LOG.info("bla=" + bla + " bli=" + bli);
	}

	public void testMethod3(Double bla, Integer bli) throws NumberFormatException {
		LOG.info("bla=" + bla + " bli=" + bli);
		int blu = 100 / bli;
	}

	public static void testMethod4(File bla) {
		LOG.info("bla=" + bla);
	}

	public void testMethod5(List bla) {
		LOG.info("bla=" + bla);
		if (bla == null) {
			return;
		}
		bla.get(1);
	}

	public void testMethod6(Void bla) {
		LOG.info("bla=" + bla);
	}

	public void testMethod7(int bli) {
		LOG.info("bli=" + bli);
		int bla = 100 / bli;
	}
	
	public void testMethod8(String[] bla) {
		LOG.info("bla=" + bla);
	}
}
