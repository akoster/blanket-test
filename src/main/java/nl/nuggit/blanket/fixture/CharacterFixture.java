package nl.nuggit.blanket.fixture;

public class CharacterFixture extends BaseFixture {

	private ParamValue[] values = new ParamValue[] { new ParamValue('a', "character"),
			new ParamValue(Character.MAX_VALUE, "maximum character"),
			new ParamValue(Character.MIN_VALUE, "minimum character") };

	@Override
	ParamValue[] values(Class<?> clazz) {
		return values;
	}

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Character.TYPE) || clazz.equals(Character.class);
	}
}
