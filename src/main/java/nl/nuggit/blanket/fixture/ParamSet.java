package nl.nuggit.blanket.fixture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParamSet {
	private List<ParamValue> paramValues = new ArrayList<ParamValue>();
	private List<Object> values = new ArrayList<Object>();

	public void addParamValue(ParamValue paramValue) {
		paramValues.add(paramValue);
		values.add(paramValue.getValue());
	}

	public ParamValue[] getParamValues() {
		return paramValues.toArray(new ParamValue[paramValues.size()]);
	}

	public Object[] getValues() {
		return values.toArray(new Object[values.size()]);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(getValues());
	}
}
