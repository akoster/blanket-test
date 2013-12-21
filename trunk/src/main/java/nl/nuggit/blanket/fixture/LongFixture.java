package nl.nuggit.blanket.fixture;

public class LongFixture extends BaseFixture {

	private Long[] values = new Long[] { 0L, Long.MAX_VALUE, Long.MIN_VALUE };

	@Override
	Object[] values(Class clazz) {
		return values;
	}

	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Long.TYPE) || clazz.equals(Long.class);
	}
}
