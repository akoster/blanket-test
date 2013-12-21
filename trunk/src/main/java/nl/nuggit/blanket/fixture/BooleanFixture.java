package nl.nuggit.blanket.fixture;


public class BooleanFixture extends BaseFixture {

	private Boolean[] values = new Boolean[] { false };

	@Override
	Object[] values(Class clazz) {
		return values;
	}
	
	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class);
	}
}
