package nl.nuggit.blanket.fixture;

public class LongFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue((long) 0, "zero long"),
			new ParamValue(Long.MAX_VALUE, "maximum long"), new ParamValue(Long.MIN_VALUE, "minimum long") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Long.TYPE) || clazz.equals(Long.class);
	}
}
