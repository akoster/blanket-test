package nl.nuggit.blanket.fixture;

public class ShortFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue((short) 0, "zero short"),
			new ParamValue(Short.MAX_VALUE, "maximum short"), new ParamValue(Short.MIN_VALUE, "minimum short") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Short.TYPE) || clazz.equals(Short.class);
	}
}
