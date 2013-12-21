package nl.nuggit.blanket.fixture;


public class ByteFixture extends BaseFixture {

	private Byte[] values = new Byte[] { 0, Byte.MAX_VALUE, Byte.MIN_VALUE };

	@Override
	Object[] values(Class clazz) {
		return values;
	}
	
	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Byte.TYPE) || clazz.equals(Byte.class);
	}
}
