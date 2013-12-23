package nl.nuggit.blanket.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class Report {

	private static final Logger LOG = Logger.getLogger(Report.class);

	private int instances;
	private Map<String, Set<Error>> classErrors = new HashMap<String, Set<Error>>();

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public void addClass(String clazz) {
		if (!classErrors.containsKey(clazz)) {
			classErrors.put(clazz, null);
		}
	}

	public void addError(Class clazz, Error error) {
		addError(String.valueOf(clazz), error);
	}

	public void addError(String clazz, Error error) {
		Set<Error> errors = classErrors.get(clazz);
		if (errors == null) {
			errors = new HashSet<Error>();
			classErrors.put(clazz, errors);
		}
		errors.add(error);
	}

	public Map<String, Set<Error>> getClassErrors() {
		return classErrors;
	}

	public void log() {
		for (String clazz : classErrors.keySet()) {
			Set<Error> errors = classErrors.get(clazz);
			if (errors == null || errors.size() == 0) {
				LOG.info(clazz + " has no errors");
			} else {
				LOG.error(clazz + " errors:");
				for (Error error : errors) {
					LOG.error(error.getDescription());
				}
			}

		}
	}
}
