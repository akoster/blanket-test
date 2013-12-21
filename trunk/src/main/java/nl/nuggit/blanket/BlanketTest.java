package nl.nuggit.blanket;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
 * TODO: add fixtures with several sets of test params. TODO: check for
 * collections and try with an empty collection
 * 
 * @author Adriaan Koster
 */
public class BlanketTest {

	private static final Logger LOG = Logger.getLogger(BlanketTest.class);

	public static void main(String[] args) throws ClassNotFoundException {

		if (args == null || args.length == 0) {
			LOG.error("Usage: pass one or more packages to analyse");
			return;
		}

		String logLevel = System.getProperty("loglevel");
		if (logLevel != null && logLevel.length() > 0) {
			LOG.setLevel(Level.toLevel(logLevel));
		}

		LOG.info("Starting");
		List<Class> classes = getClassesForPackage(args[0]);
		for (Class clazz : classes) {
			if (clazz.getName().equals(BlanketTest.class.getName())) {
				continue;
			}
			if (!Modifier.isPublic(clazz.getModifiers())) {
				continue;
			}
			LOG.debug("-------------------------------------------------------");
			LOG.debug("Inspecting class: " + clazz.getName());
			checkClass(clazz);
		}
		
		LOG.info("Done");
	}

	private static void checkClass(Class clazz) {

		Constructor[] constructors = clazz.getConstructors();
		List<Object> instances = new ArrayList<Object>();
		for (Constructor constructor : constructors) {

			if (!Modifier.isPublic(constructor.getModifiers())) {
				continue;
			}
			String paramTypes = Arrays.toString(constructor.getParameterTypes());
			Object[] params = null;
			try {
				LOG.debug("Invoking constructor: " + constructor.getName() + paramTypes);
				params = createParameters(constructor.getParameterTypes());
				instances.add(constructor.newInstance(params));
			} catch (Exception e) {
				LOG.error("Encountered: " + getCause(paramTypes, params, e));
			}
		}
		LOG.debug("Created " + instances.size() + " instances of " + clazz.getName());
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			for (Object instance : instances) {
				String paramTypes = Arrays.toString(method.getParameterTypes());
				Object[] params = null;
				try {
					LOG.debug("Invoking method: " + method.getName() + "(" + paramTypes + ")");
					params = createParameters(method.getParameterTypes());
					method.invoke(instance, params);
				} catch (Exception e) {
					LOG.error("Encountered: " + getCause(paramTypes, params, e));
				}
			}
		}
	}

	private static String getCause(String paramTypes, Object[] params, Exception e) {
		String cause = "";
		StackTraceElement rootElement = null;
		if (e.getCause() != null) {
			cause = e.getCause().getClass().getName();
			StackTraceElement[] elements = e.getCause().getStackTrace();
			rootElement = getRootElement(e, rootElement, elements);
		} else {
			cause = e.getClass().getName();
			StackTraceElement[] elements = e.getStackTrace();
			rootElement = getRootElement(e, rootElement, elements);
		}
		cause += " invoking " + rootElement.getClassName() + "." + rootElement.getMethodName() + paramTypes
				+ " with values " + Arrays.toString(params) + " (see " + rootElement.getFileName() + ":"
				+ rootElement.getLineNumber() + ")";
		return cause;
	}

	private static StackTraceElement getRootElement(Exception e, StackTraceElement element, StackTraceElement[] elements) {
		if (elements != null && elements.length > 0) {
			element = e.getCause().getStackTrace()[0];
		}
		return element;
	}

	private static List<Object[]> createFixtures(Class[] paramtypes) throws Exception {
		List<Object[]> fixtures = new ArrayList<Object[]>();
		return fixtures;
	}

	private static Object[] createParameters(Class[] paramtypes) {
		Object[] params = new Object[paramtypes.length];
		for (int i = 0; i < paramtypes.length; i++) {
			Class paramtype = paramtypes[i];
			if (paramtype.isPrimitive()) {
				if (paramtype.equals(Boolean.TYPE)) {
					params[i] = false;
				}
				if (paramtype.equals(Character.TYPE) || paramtype.equals(Byte.TYPE) || paramtype.equals(Short.TYPE)
						|| paramtype.equals(Integer.TYPE) || paramtype.equals(Long.TYPE)
						|| paramtype.equals(Float.TYPE) || paramtype.equals(Double.TYPE)) {
					params[i] = 0;
				}
				if (paramtype.equals(Void.TYPE)) {
					params[i] = null;
				}
			} else {
				params[i] = null;
			}
		}
		return params;
	}

	private static List<Class> getClassesForPackage(String packagename) throws ClassNotFoundException {
		// 'classes' will hold a list of directories matching the package name.
		// There may be more than one if a package is split over multiple
		// jars/paths
		List<Class> classes = new ArrayList<Class>();
		List<File> directories = new ArrayList<File>();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			// Ask for all resources for the path
			String path = packagename.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			while (resources.hasMoreElements()) {
				URL res = resources.nextElement();
				if (res.getProtocol().equalsIgnoreCase("jar")) {
					JarURLConnection conn = (JarURLConnection) res.openConnection();
					JarFile jar = conn.getJarFile();
					for (JarEntry e : Collections.list(jar.entries())) {
						if (e.getName().startsWith(path) && e.getName().endsWith(".class")
								&& !e.getName().contains("$")) {
							String className = e.getName().replace("/", ".").substring(0, e.getName().length() - 6);
							classes.add(Class.forName(className));
						}
					}
				} else
					directories.add(new File(URLDecoder.decode(res.getPath(), "UTF-8")));
			}
		} catch (NullPointerException e) {
			throw new ClassNotFoundException(String.format("%s does not appear to be a valid package", packagename), e);
		} catch (UnsupportedEncodingException e) {
			throw new ClassNotFoundException(String.format("%s does not appear to be a valid package", packagename), e);
		} catch (IOException e) {
			throw new ClassNotFoundException(String.format("Could not get all resources for %s", packagename), e);
		}
		List<String> subPackages = new ArrayList<String>();
		// For every directory identified capture all the .class files
		for (File directory : directories) {
			if (directory.exists()) {
				// Get the list of the files contained in the package
				File[] files = directory.listFiles();
				for (File file : files) {
					// add .class files to results
					String fileName = file.getName();
					if (file.isFile() && fileName.endsWith(".class")) {
						// removes the .class extension
						String className = packagename + '.' + fileName.substring(0, fileName.length() - 6);
						classes.add(Class.forName(className));
					}
					// keep track of subdirectories
					if (file.isDirectory()) {
						subPackages.add(packagename + "." + fileName);
					}
				}
			} else {
				throw new ClassNotFoundException(String.format("%s (%s) does not appear to be a valid package",
						packagename, directory.getPath()));
			}
		}
		// check all potential subpackages
		for (String subPackage : subPackages) {
			classes.addAll(getClassesForPackage(subPackage));
		}
		return classes;
	}

}
