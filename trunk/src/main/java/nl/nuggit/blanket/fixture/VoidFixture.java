package nl.nuggit.blanket.fixture;

public class VoidFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue(null, "null") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Void.TYPE);
	}
}
