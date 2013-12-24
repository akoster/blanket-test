package nl.nuggit.blanket.fixture;

public class IntegerFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue(0, "zero integer"),
			new ParamValue(Integer.MAX_VALUE, "maximum integer"), new ParamValue(Integer.MIN_VALUE, "minimum integer") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Integer.TYPE) || clazz.equals(Integer.class);
	}
}
