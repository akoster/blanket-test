package nl.nuggit.blanket.fixture;

import java.util.Collections;

public class CollectionFixture extends BaseFixture {

	private Object[] values = new Object[] { null };

	@Override
	public boolean handles(Class clazz) {
		return Collections.class.isAssignableFrom(clazz);
	}

	@Override
	Object[] values(Class type) {
		// TODO: create an empty collection
		return null;
	}
}
