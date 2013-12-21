package nl.nuggit.blanket.fixture;

public class CharacterFixture extends BaseFixture {

	private Character[] values = new Character[] { 'a', Character.MAX_VALUE, Character.MIN_VALUE };

	@Override
	Object[] values(Class clazz) {
		return values;
	}

	@Override
	public boolean handles(Class clazz) {
		return clazz.equals(Character.TYPE) || clazz.equals(Character.class);
	}
}
