package nl.nuggit.blanket.fixture;

public class DefaultFixture extends BaseFixture {

	@Override
	ParamValue[] values(Class<?> clazz) {
		return new ParamValue[] { new ParamValue(null, "null value") };
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return true;
	}
}
