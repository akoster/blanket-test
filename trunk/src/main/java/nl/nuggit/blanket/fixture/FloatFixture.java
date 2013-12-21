package nl.nuggit.blanket.fixture;

public class FloatFixture extends BaseFixture {

	private Float[] values = new Float[] { 0.0f, Float.MAX_VALUE, Float.MIN_VALUE };

	@Override
	Object[] values(Class clazz) {
		return values;
	}

	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Float.TYPE) || clazz.equals(Float.class);
	}
}
