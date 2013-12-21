package nl.nuggit.blanket.fixture;

public class VoidFixture extends BaseFixture {

	private Object[] values = new Object[] { null };

	@Override
	Object[] values(Class clazz) {
		return values;
	}

	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Void.TYPE);
	}
}
