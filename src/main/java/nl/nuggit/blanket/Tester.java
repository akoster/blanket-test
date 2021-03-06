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
import nl.nuggit.blanket.fixture.ParamSet;
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
 * TODO: keep track of slow methods and reduce timeout for them when called again
 * 
 * @author Adriaan Koster
 */

public class Tester {

	private static final Logger LOG = Logger.getLogger(Tester.class);

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
	public static Report execute(String rootPackage, Class<?> caller) throws ClassNotFoundException {
		LOG.info("Starting");
		Report report = new Report(rootPackage);
		checkClasses(caller, Finder.findClassesForPackage(rootPackage, report), report);
		report.log();
		LOG.info("Done");
		return report;
	}

	private static void checkClasses(Class<?> caller, List<Class<?>> classes, Report report) {
		for (Class<?> clazz : classes) {
			if (clazz.getName().equals(Tester.class.getName())) {
				continue;
			}
			if (clazz.getName().equals(caller.getName())) {
				continue;
			}
			if (!Modifier.isPublic(clazz.getModifiers())) {
				continue;
			}
			LOG.debug("-------------------------------------------------------");
			LOG.debug("Inspecting class: " + clazz.getName());
			checkMethods(clazz, checkConstructors(clazz, report), report);
		}
	}

	private static List<Object> checkConstructors(Class<?> clazz, Report report) {
		Constructor<?>[] constructors = clazz.getConstructors();
		List<Object> instances = new ArrayList<Object>();
		for (Constructor<?> constructor : constructors) {
			if (!Modifier.isPublic(constructor.getModifiers())) {
				continue;
			}
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			String signature = Arrays.toString(parameterTypes);
			LOG.debug("Invoking constructor: " + constructor.getName() + signature);
			List<ParamSet> paramSets = createValueSets(parameterTypes);
			for (ParamSet paramSet : paramSets) {
				LOG.debug("params: " + paramSet);
				Throwable throwable = Caller.callConstructor(constructor, instances, paramSet.getValues());
				if (throwable != null) {
					report.addError(clazz, new Error(signature, paramSet, throwable));
				}
			}
		}
		LOG.debug("Created " + instances.size() + " instances of " + clazz.getName());
		report.setInstances(instances.size());
		return instances;
	}

	private static void checkMethods(Class<?> clazz, List<Object> instances, Report report) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			Class<?>[] parameterTypes = method.getParameterTypes();
			String signature = Arrays.toString(parameterTypes);
			for (Object instance : instances) {
				LOG.debug("Invoking method: " + method.getName() + "(" + signature + ")");
				List<ParamSet> paramSets = createValueSets(parameterTypes);
				for (ParamSet paramSet : paramSets) {
					LOG.debug("params: " + paramSet);
					Throwable throwable = Caller.callMethod(method, instance, paramSet.getValues());
					if (throwable != null) {
						report.addError(clazz, new Error(signature, paramSet, throwable));
					}
				}
			}
		}
	}

	private static List<ParamSet> createValueSets(Class<?>[] paramTypes) {
		List<ParamSet> paramSets = new ArrayList<ParamSet>();
		int paramCount = paramTypes.length;
		if (paramCount == 0) {
			paramSets.add(new ParamSet());
			return paramSets;
		}
		Fixture[] fixtures = new Fixture[paramCount];
		for (int i = 0; i < paramCount; i++) {
			Fixture fixture = findFixture(paramTypes[i]);
			fixtures[i] = fixture;
		}
		// let each fixture emit all its test values
		while (haveNotAllCycled(fixtures)) {
			ParamSet paramSet = new ParamSet();
			for (int i = 0; i < paramCount; i++) {
				paramSet.addParamValue(fixtures[i].nextValue());
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

	private static Fixture findFixture(Class<?> paramType) {
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

		execute(args[0], Tester.class);
	}
}
