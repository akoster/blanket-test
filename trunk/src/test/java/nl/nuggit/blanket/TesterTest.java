package nl.nuggit.blanket;

import nl.nuggit.blanket.report.Report;

import org.junit.Test;

public class TesterTest {

	@Test
	public void testExecute() throws ClassNotFoundException {
		Report report = Tester.execute("com.acme.foo", this.getClass());
		report.export();
	}

}
