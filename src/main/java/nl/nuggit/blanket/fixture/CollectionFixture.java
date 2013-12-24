package nl.nuggit.blanket.fixture;

import java.util.Collections;

public class CollectionFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue(null, "null") };

	@Override
	public boolean handles(Class<?> clazz) {
		return Collections.class.isAssignableFrom(clazz);
	}

	@Override
	ParamValue[] values(Class<?> type) {
		// TODO: create an empty collection
		return values;
	}
}
