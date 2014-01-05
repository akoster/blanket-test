package nl.nuggit.blanket.fixture;



public abstract class BaseFixture implements Fixture {

	boolean hasCycled = false;
	private int index;
	private Class<?> clazz;

	abstract ParamValue[] values(Class<?> clazz);
	
	@Override
	public boolean hasCycled() {
		return hasCycled;
	}

	@Override
	public ParamValue nextValue() {
		if (index >= values(clazz).length) {
			index = 0;
			hasCycled = true;
		}
		return values(clazz)[index++];
	}

	@Override
	public void reset(Class<?> clazz) {
		this.clazz = clazz;
		index = 0;
		hasCycled = false;
	}

	@Override
	public int valueCount() {
		return values(clazz).length;
	}
	
	

}
