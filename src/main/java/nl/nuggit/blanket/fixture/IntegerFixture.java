package nl.nuggit.blanket.fixture;

public class IntegerFixture extends BaseFixture {

	private Integer[] values = new Integer[] { 0, Integer.MAX_VALUE, Integer.MIN_VALUE };

	@Override
	Object[] values(Class clazz) {
		return values;
	}

	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Integer.TYPE) || clazz.equals(Integer.class);
	}
}
