package nl.nuggit.blanket.fixture;

public class DefaultFixture extends BaseFixture {

	@Override
	Object[] values(Class clazz) {
		return new Object[] { null };
	}

	@Override
	public boolean handles(Class clazz) {
		return true;
	}
}
