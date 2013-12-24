package nl.nuggit.blanket.report;

import nl.nuggit.blanket.fixture.ParamSet;

public class Error {

	private String signature;
	private ParamSet paramSet;
	private Throwable throwable;
	private String className;
	private String methodName;
	private String fileName;
	private int lineNumber;
	private String error;
	private String description;

	public Error(String signature, ParamSet paramSet, Throwable throwable) {
		this.signature = signature;
		this.paramSet = paramSet;
		this.throwable = throwable;
		parseStackTrace(throwable);
	}

	private void parseStackTrace(Throwable throwable) {
		StackTraceElement rootElement = null;
		if (throwable.getCause() != null) {
			error = throwable.getCause().getClass().getName();
			StackTraceElement[] elements = throwable.getCause().getStackTrace();
			rootElement = getRootElement(throwable, rootElement, elements);
		} else {
			error = throwable.getClass().getName();
			StackTraceElement[] elements = throwable.getStackTrace();
			rootElement = getRootElement(throwable, rootElement, elements);
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

	public ParamSet getParamSet() {
		return paramSet;
	}

	public Throwable getThrowable() {
		return throwable;
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
					className, methodName, signature, paramSet, fileName, lineNumber);
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
