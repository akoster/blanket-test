package nl.nuggit.blanket.fixture;

public class FloatFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue((float) 0.0, "zero float"),
			new ParamValue(Float.MAX_VALUE, "maximum float"), new ParamValue(Float.MIN_VALUE, "minimum float") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Float.TYPE) || clazz.equals(Float.class);
	}
}
