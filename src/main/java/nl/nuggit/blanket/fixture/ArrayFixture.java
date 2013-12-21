package nl.nuggit.blanket.fixture;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ArrayFixture extends BaseFixture {

	private Object[] values = new Object[] { null };

	@Override
	public boolean handles(Class clazz) {
		return clazz.isArray();
	}
	
	@Override
	Object[] values(Class type) {
		List<Object> values = new ArrayList<Object>();
		Class arrayType = type.getComponentType();
		// empty array
		values.add(Array.newInstance(arrayType, 0));
		// array of length 1 with a null value
		values.add(Array.newInstance(arrayType, 1));
		// array of length 1 with a non-null value
		Object filledArray = Array.newInstance(arrayType, 1);
		if (arrayType.equals(Boolean.TYPE)) {
			Array.setBoolean(filledArray, 0, true);
		} else if (arrayType.equals(Character.TYPE)) {
			Array.setChar(filledArray, 0, 'a');
		} else if (arrayType.equals(Byte.TYPE)) {
			Array.setByte(filledArray, 0, (byte) 1);
		} else if (arrayType.equals(Short.TYPE)) {
			Array.setShort(filledArray, 0, (short) 1);
		} else if (arrayType.equals(Integer.TYPE)) {
			Array.setInt(filledArray, 0, 1);
		} else if (arrayType.equals(Long.TYPE)) {
			Array.setLong(filledArray, 0, 1);
		} else if (arrayType.equals(Float.TYPE)) {
			Array.setFloat(filledArray, 0, 1);
		} else if (arrayType.equals(Double.TYPE)) {
			Array.setDouble(filledArray, 0, 1);
		} else if (!arrayType.isPrimitive()) {
			Array.set(filledArray, 0, createComplexInstance(arrayType));
		}
		values.add(filledArray);
		
		return values.toArray();
	}
	
	private static Object createComplexInstance(Class arrayType) {
		Object value = null;
		// try to create an instance
		for (Constructor constructor : arrayType.getConstructors()) {
			Class[] parameterTypes = constructor.getParameterTypes();
			Object[] parameters = createParameters(parameterTypes);
			try {
				value = constructor.newInstance(parameters);
				break;
			} catch (Exception ignore) {
				// skip this constructor
			}
		}
		return value;
	}

	private static Object[] createParameters(Class[] paramTypes) {
		Object[] params = new Object[paramTypes.length];
		for (int i = 0; i < paramTypes.length; i++) {
			Class paramType = paramTypes[i];
			if (paramType.isPrimitive()) {
				if (paramType.equals(Boolean.TYPE)) {
					params[i] = false;
				}
				if (paramType.equals(Character.TYPE) || paramType.equals(Byte.TYPE) || paramType.equals(Short.TYPE)
						|| paramType.equals(Integer.TYPE) || paramType.equals(Long.TYPE)
						|| paramType.equals(Float.TYPE) || paramType.equals(Double.TYPE)) {
					params[i] = 0;
				}
				if (paramType.equals(Void.TYPE)) {
					params[i] = null;
				}
			} else {
				params[i] = null;
			}
		}
		return params;
	}
}
