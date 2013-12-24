package nl.nuggit.blanket.fixture;

public class DoubleFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue((double) 0.0, "zero double"),
			new ParamValue(Double.MAX_VALUE, "maximum double"), new ParamValue(Double.MIN_VALUE, "minimum double") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Double.TYPE) || clazz.equals(Double.class);
	}
}
