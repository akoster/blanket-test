package nl.nuggit.blanket.report;

import java.util.Arrays;

public class Error {

	private String signature;
	private Object[] values;
	private Throwable exception;
	private String className;
	private String methodName;
	private String fileName;
	private int lineNumber;
	private String error;
	private String description;

	public Error(String signature, Object[] values, Throwable exception) {
		this.signature = signature;
		this.values = values;
		this.exception = exception;
		StackTraceElement rootElement = null;
		if (exception.getCause() != null) {
			error = exception.getCause().getClass().getName();
			StackTraceElement[] elements = exception.getCause().getStackTrace();
			rootElement = getRootElement(exception, rootElement, elements);
		} else {
			error = exception.getClass().getName();
			StackTraceElement[] elements = exception.getStackTrace();
			rootElement = getRootElement(exception, rootElement, elements);
		}
		this.className = rootElement.getClassName();
		this.methodName = rootElement.getMethodName();
		this.fileName = rootElement.getFileName();
		this.lineNumber = rootElement.getLineNumber();
	}

	private static StackTraceElement getRootElement(Throwable e, StackTraceElement element, StackTraceElement[] elements) {
		if (elements != null && elements.length > 0) {
			element = elements[0];
		}
		return element;
	}

	public String getSignature() {
		return signature;
	}

	public Object[] getValues() {
		return values;
	}

	public Throwable getException() {
		return exception;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getFileName() {
		return fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getError() {
		return error;
	}

	public String getDescription() {
		if (description == null) {
			description = String.format("Encountered: %s invoking %s.%s%s with values %s (see %s:%s)", error,
					className, methodName, signature, Arrays.toString(values), fileName, lineNumber);
		}
		return description;
	}

	private String id() {
		return String.format("%s%s%s%s", error, className, methodName, signature);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Error)) {
			return false;
		}
		Error other = (Error) obj;
		return id().equals(other.id());
	}

	@Override
	public int hashCode() {
		return id().hashCode();
	}

}
