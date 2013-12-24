package nl.nuggit.blanket.fixture;

public class ByteFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue((byte) 0, "zero byte"),
			new ParamValue(Byte.MAX_VALUE, "maximum byte"), new ParamValue(Byte.MIN_VALUE, "minimum byte") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Byte.TYPE) || clazz.equals(Byte.class);
	}
}
