package nl.nuggit.blanket.fixture;

public class ParamValue {
	
	private Object value;
	private String description;

	public ParamValue(Object value, String description) {
		this.value = value;
		this.description = description;
	}

	public Object getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
}
