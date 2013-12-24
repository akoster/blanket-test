package nl.nuggit.blanket.fixture;

public class BooleanFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue(false, "boolean") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class);
	}
}
