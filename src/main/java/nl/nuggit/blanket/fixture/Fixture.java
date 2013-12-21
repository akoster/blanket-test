package nl.nuggit.blanket.fixture;

public interface Fixture {

	/**
	 * @return true if this fixture can handle the class
	 */
	boolean handles(Class clazz);

	/**
	 * @return the number of possible values in this fixture
	 */
	int valueCount();
	
	/**
	 * Cycles through all the possible values endlessly
	 * 
	 * @return an iterator
	 */
	Object nextValue();
	
	/**
	 * @return true if all possible values have been returned
	 */
	boolean hasCycled();
	
	/**
	 * Resets this fixture with the given class
	 */
	void reset(Class clazz);
}
