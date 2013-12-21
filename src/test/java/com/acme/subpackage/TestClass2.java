package com.acme.subpackage;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

public class TestClass2 {

    private static final Logger LOG =
        org.apache.log4j.Logger.getLogger(TestClass2.class);

    public TestClass2(Integer bla, Double bli) {
        LOG.info("bla=" + bla + " bli=" + bli);
    }

    public TestClass2(Double bla) {
        LOG.info("bla=" + bla);
    }

    public TestClass2() {
        LOG.info("invoked");
    }

    private TestClass2(String bla) {
        LOG.info("bla=" + bla);
    }

    public void testMethod1(String bla) {
        LOG.info("bla=" + bla);
    }

    public void testMethod2(FileOutputStream bla, int bli) {
        LOG.info("bla=" + bla + " bli=" + bli);
        try {
            bla.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testMethod3(Double bla, Integer bli)
            throws NumberFormatException
    {
        LOG.info("bla=" + bla + " bli=" + bli);
    }

    public static void testMethod4(File bla) {
        LOG.info("bla=" + bla);
    }

}
