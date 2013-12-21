package nl.nuggit.blanket.fixture;


public class DoubleFixture extends BaseFixture {

	private Double[] values = new Double[] { 0.0, Double.MAX_VALUE, Double.MIN_VALUE };

	@Override
	Object[] values(Class clazz) {
		return values;
	}
	
	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Double.TYPE) || clazz.equals(Double.class);
	}
}
