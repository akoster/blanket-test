package nl.nuggit.blanket;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility that finds classes within or under a given package
 * 
 * @author Adriaan
 */
public class ClassFinder {

	public static List<Class> findClassesForPackage(String packagename) throws ClassNotFoundException {
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
			classes.addAll(findClassesForPackage(subPackage));
		}
		return classes;
	}
}
