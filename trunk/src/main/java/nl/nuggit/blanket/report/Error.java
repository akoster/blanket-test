package nl.nuggit.blanket.report;

import nl.nuggit.blanket.fixture.ParamSet;

public class Error {

	private static final String EOL = String.format("%n");
	private final String signature;
	private final ParamSet paramSet;
	private final Throwable throwable;
	private final Throwable cause;
	private StackTraceElement rootElement;
	private String error;
	private String description;

	public Error(String signature, ParamSet paramSet, Throwable throwable) {
		this.signature = signature;
		this.paramSet = paramSet;
		this.throwable = throwable;
		this.cause = getCause(throwable);
		parseStackTrace(throwable);
	}

	private Throwable getCause(Throwable throwable) {
		Throwable cause = throwable.getCause();
		if (cause != null) {
			return getCause(cause);
		} else {
			return throwable;
		}
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
		this.rootElement = rootElement;
	}

	private static StackTraceElement getRootElement(Throwable e, StackTraceElement element, StackTraceElement[] elements) {
		if (elements != null && elements.length > 0) {
			element = elements[0];
		}
		return element;
	}

	public String getStackTrace() {
		StringBuilder sb = new StringBuilder();
		sb.append(cause.toString()).append(EOL);
		for (StackTraceElement element : cause.getStackTrace()) {
			sb.append("at: ");
			sb.append(element).append(EOL);
		}
		return sb.toString();
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

	public Throwable getCause() {
		return cause;
	}

	public String getError() {
		return error;
	}

	public String getDescription() {
		if (description == null) {
			description = String.format("Encountered: %s invoking %s.%s%s with values %s (see %s:%s)", error,
					getClassName(), getMethodName(), signature, paramSet, getFileName(), getLineNumber());
		}
		return description;
	}

	public String getClassName() {
		if (rootElement != null) {
			return rootElement.getClassName();
		} else {
			return null;
		}
	}

	public String getMethodName() {
		if (rootElement != null) {
			return rootElement.getMethodName();
		} else {
			return null;
		}
	}

	public String getFileName() {
		if (rootElement != null) {
			return rootElement.getFileName();
		} else {
			return null;
		}
	}

	public String getLineNumber() {
		if (rootElement != null) {
			return String.valueOf(rootElement.getLineNumber());
		} else {
			return null;
		}
	}

	public StackTraceElement getRootElement() {
		return rootElement;
	}

	private String id() {
		return String.format("%s%s%s%s", error, getClassName(), getMethodName(), signature);
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
