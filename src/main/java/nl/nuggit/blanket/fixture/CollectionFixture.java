package nl.nuggit.blanket.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionFixture extends BaseFixture {

	@Override
	public boolean handles(Class<?> clazz) {
		return Collections.class.isAssignableFrom(clazz);
	}

	@Override
	ParamValue[] values(Class<?> type) {
		List<ParamValue> values = new ArrayList<ParamValue>();
		values.add(new ParamValue(null, "null"));
		Constructor<?> defaultConstructor = findDefaultConstructor(type);
		Collection<?> value = callConstructor(defaultConstructor);
		if (value != null) {
			values.add(new ParamValue(value, "empty collection"));
		}
		value = callConstructor(defaultConstructor);
		if (value != null) {
			value.add(null);
			values.add(new ParamValue(value, "collection with single null"));
		}
		// TODO: create a collection with a single value
		return values.toArray(new ParamValue[values.size()]);
	}

	private Collection<?> callConstructor(Constructor<?> defaultConstructor) {
		Collection<?> collection;
		try {
			collection = (Collection<?>) defaultConstructor.newInstance(new Object[0]);
		} catch (Throwable e) {
			collection = null;
		}
		return collection;
	}

	private static Constructor<?> findDefaultConstructor(Class<?> type) {
		for (Constructor<?> constructor : type.getConstructors()) {
			if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterTypes().length == 0) {
				return constructor;
			}
		}
		return null;
	}
}
