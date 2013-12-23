package nl.nuggit.blanket;

import org.junit.Test;

public class TesterTest {

	@Test
	public void testExecute() throws ClassNotFoundException {
		Tester.execute("com.acme.foo", this.getClass());
	}

}
