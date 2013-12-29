/**
 * 
 */
package nl.nuggit.blanket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unchecked")
class ClassCaller implements Callable<Throwable> {

	private static final int TIMEOUT_SECONDS = 1;

	private static ExecutorService executor = Executors.newCachedThreadPool();

	private Method method;
	private Constructor constructor;
	private Object instance;
	private Object[] values;
	private List<Object> instances;

	ClassCaller(Method method, Object instance, Object[] values) {
		this.method = method;
		this.instance = instance;
		this.values = values;
	}

	ClassCaller(Constructor constructor, List<Object> instances, Object[] values) {
		this.constructor = constructor;
		this.instances = instances;
		this.values = values;
	}

	@Override
	public Throwable call() {
		Throwable result = null;
		try {
			if (method != null) {
				method.invoke(instance, values);

			} else if (constructor != null) {
				instances.add(constructor.newInstance(values));
			}
		} catch (Throwable t) {
			result = t;
		}
		return result;
	}

	static Throwable callClass(Constructor constructor, List<Object> instances, Object[] values) {
		return callClass(new ClassCaller(constructor, instances, values));
	}
	
	static Throwable callClass(Method method, Object instance, Object[] values) {
		return callClass(new ClassCaller(method, instance, values));
	}
	
	private static Throwable callClass(Callable<Throwable> task) {
		Throwable result = null;
		Future<Throwable> future = executor.submit(task);
		try {
			result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
		} catch (TimeoutException ignore) {
		} catch (InterruptedException ignore) {
		} catch (ExecutionException ignore) {
		} finally {
			future.cancel(true);
		}
		return result;
	}
}