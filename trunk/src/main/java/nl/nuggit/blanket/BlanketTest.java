package nl.nuggit.blanket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.nuggit.blanket.fixture.ArrayFixture;
import nl.nuggit.blanket.fixture.BooleanFixture;
import nl.nuggit.blanket.fixture.ByteFixture;
import nl.nuggit.blanket.fixture.CharacterFixture;
import nl.nuggit.blanket.fixture.DefaultFixture;
import nl.nuggit.blanket.fixture.DoubleFixture;
import nl.nuggit.blanket.fixture.Fixture;
import nl.nuggit.blanket.fixture.FloatFixture;
import nl.nuggit.blanket.fixture.IntegerFixture;
import nl.nuggit.blanket.fixture.LongFixture;
import nl.nuggit.blanket.fixture.ShortFixture;
import nl.nuggit.blanket.fixture.VoidFixture;
import nl.nuggit.blanket.report.Error;
import nl.nuggit.blanket.report.Report;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * BlanketTest is a test tool for inspecting all public constructors and methods
 * of a set of classes for common errors. A list of package names is specified
 * as command line arguments and all classes found in those packages, or their
 * subpackages, are inspected. By passing each constructor and method common
 * values like null, 0, -1, 1 and "" some common bugs may be detected.
 * 
 * The application logs to stderr. The level of logging can be determined by
 * setting the property '-Dloglevel=[TRACE|DEBUG|INFO|WARN|ERROR|FATAL|OFF]
 * 
 * TODO: check for collections and try with an empty collection
 * 
 * @author Adriaan Koster
 */

@SuppressWarnings("unchecked")
public class BlanketTest {

	private static final Logger LOG = Logger.getLogger(BlanketTest.class);

	private static Fixture[] FIXTURES = new Fixture[] { new BooleanFixture(), new ByteFixture(),
			new CharacterFixture(), new DoubleFixture(), new FloatFixture(), new IntegerFixture(), new LongFixture(),
			new ShortFixture(), new VoidFixture(), new ArrayFixture(), new DefaultFixture() };

	/**
	 * Executes a blanket test from the given root package
	 * 
	 * @param rootPackage
	 *            the package to search for classes in
	 * @throws ClassNotFoundException
	 *             if there was a problem
	 */
	public static Report execute(String rootPackage) throws ClassNotFoundException {
		LOG.info("Starting");
		List<Class> classes = ClassFinder.findClassesForPackage(rootPackage);
		Report report = new Report();
		for (Class clazz : classes) {
			if (clazz.getName().equals(BlanketTest.class.getName())) {
				continue;
			}
			if (!Modifier.isPublic(clazz.getModifiers())) {
				continue;
			}
			LOG.debug("-------------------------------------------------------");
			LOG.debug("Inspecting class: " + clazz.getName());
			checkClass(clazz, report);
		}
		report.log();
		LOG.info("Done");
		return report;
	}

	private static void checkClass(Class clazz, Report report) {
		report.addClass(clazz);
		List<Object> instances = checkConstructors(clazz, report);
		checkMethods(clazz, instances, report);
	}

	private static List<Object> checkConstructors(Class clazz, Report report) {
		Constructor[] constructors = clazz.getConstructors();
		List<Object> instances = new ArrayList<Object>();
		for (Constructor constructor : constructors) {
			if (!Modifier.isPublic(constructor.getModifiers())) {
				continue;
			}
			Class[] parameterTypes = constructor.getParameterTypes();
			String signature = Arrays.toString(parameterTypes);
			LOG.debug("Invoking constructor: " + constructor.getName() + signature);
			List<Object[]> valueSets = createValueSets(parameterTypes);
			for (Object[] values : valueSets) {
				LOG.debug("params: " + Arrays.toString(values));
				try {
					instances.add(constructor.newInstance(values));
				} catch (Exception e) {
					report.addError(clazz, new Error(signature, values, e));
				}
			}
		}
		LOG.debug("Created " + instances.size() + " instances of " + clazz.getName());
		return instances;
	}

	private static void checkMethods(Class clazz, List<Object> instances, Report report) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			Class[] parameterTypes = method.getParameterTypes();
			String signature = Arrays.toString(parameterTypes);
			for (Object instance : instances) {
				LOG.debug("Invoking method: " + method.getName() + "(" + signature + ")");
				List<Object[]> valueSets = createValueSets(parameterTypes);
				for (Object[] values : valueSets) {
					LOG.debug("params: " + Arrays.toString(values));
					try {
						method.invoke(instance, values);
					} catch (Exception e) {
						report.addError(clazz, new Error(signature, values, e));
					}
				}
			}
		}
	}

	private static List<Object[]> createValueSets(Class[] paramTypes) {
		List<Object[]> paramSets = new ArrayList<Object[]>();
		int paramCount = paramTypes.length;
		if (paramCount == 0) {
			paramSets.add(new Object[] {});
			return paramSets;
		}
		Fixture[] fixtures = new Fixture[paramCount];
		for (int i = 0; i < paramCount; i++) {
			Fixture fixture = findFixture(paramTypes[i]);
			fixtures[i] = fixture;
		}
		while (haveNotAllCycled(fixtures)) {
			Object[] paramSet = new Object[paramCount];
			for (int i = 0; i < paramCount; i++) {
				paramSet[i] = fixtures[i].nextValue();
			}
			paramSets.add(paramSet);
		}
		return paramSets;
	}

	private static boolean haveNotAllCycled(Fixture[] fixtures) {
		for (Fixture fixture : fixtures) {
			if (!fixture.hasCycled()) {
				return true;
			}
		}
		return false;
	}

	private static Fixture findFixture(Class paramType) {
		for (Fixture fixture : FIXTURES) {
			if (fixture.handles(paramType)) {
				fixture.reset(paramType);
				return fixture;
			}
		}
		throw new IllegalArgumentException("Could not find fixture for " + paramType);
	}

	public static void main(String[] args) throws ClassNotFoundException {

		if (args == null || args.length == 0) {
			LOG.error("Usage: pass one or more packages to analyse");
			return;
		}

		String logLevel = System.getProperty("loglevel");
		if (logLevel != null && logLevel.length() > 0) {
			LOG.setLevel(Level.toLevel(logLevel));
		}

		execute(args[0]);
	}
}
