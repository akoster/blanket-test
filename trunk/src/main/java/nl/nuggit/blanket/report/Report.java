package nl.nuggit.blanket.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

public class Report {

	private static final Logger LOG = Logger.getLogger(Report.class);

	private final String packageName;
	private int instances;
	private Map<String, Set<Error>> classErrors = new TreeMap<String, Set<Error>>();

	public Report(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

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

	public void addError(Class<?> clazz, Error error) {
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

	public void export() {
		export(null);
	}

	public void export(String outputDir) {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty("resource.loader", "class");
		engine.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.Log4JLogChute");
		engine.setProperty("runtime.log.logsystem.log4j.logger", LOG.getName());
		engine.init();

		if (outputDir == null) {
			outputDir = String.format("target/blanket-test-%1$tY-%1$tm-%1$te-%1$tH%1$tM/", new Date());
		}
		File dir = new File(outputDir);
		dir.mkdirs();

		VelocityContext context = new VelocityContext();
		context.put("report", this);
		context.put("classErrors", classErrors);

		writeFile(engine, context, "report.vm", outputDir + "report.html");
		writeFile(engine, context, "main.css", outputDir + "main.css");

		for (Entry<String, Set<Error>> entry : classErrors.entrySet()) {
			context = new VelocityContext();
			context.put("className", entry.getKey());
			context.put("errors", entry.getValue());
			writeFile(engine, context, "error.vm", outputDir + entry.getKey() + ".html");
		}

	}

	private void writeFile(VelocityEngine engine, VelocityContext context, String templateName, String outputFile) {
		FileWriter fw = null;
		try {
			StringWriter sw = new StringWriter();
			Template template = engine.getTemplate(templateName);
			template.merge(context, sw);
			fw = new FileWriter(outputFile);
			fw.write(sw.toString());
		} catch (IOException e) {
			LOG.error("Error writing file ", e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					LOG.error("Could not close file ", e);
				}
			}
		}
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
