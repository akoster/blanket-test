package nl.nuggit.blanket.fixture;

public class ShortFixture extends BaseFixture {

	private Short[] values = new Short[] { 0, Short.MAX_VALUE, Short.MIN_VALUE };

	@Override
	Object[] values(Class clazz) {
		return values;
	}

	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Short.TYPE) || clazz.equals(Short.class);
	}
}
