package org.owfs.jowfsclient.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.ReentrantLock;
import org.owfs.jowfsclient.OwfsClient;

/**
 * OwfsClientFactory that executes all methods utilizing ReentrantLock.
 *
 * @author Tom Kucharski
 */
public class OwfsClientThreadSafeFactory {

	private ReentrantLock reentrantLock = new ReentrantLock();

	public void setLock(ReentrantLock lock) {
		this.reentrantLock = lock;
	}

	public OwfsClient decorate(OwfsClient client) {
		OwfsClientThreadSafeFactory.OwfsInvocationHandler owfsInvocationHandler = new OwfsClientThreadSafeFactory.OwfsInvocationHandler(client);
		return (OwfsClient) Proxy.newProxyInstance(client.getClass().getClassLoader(), new Class[]{OwfsClient.class}, owfsInvocationHandler);
	}

	private class OwfsInvocationHandler implements InvocationHandler {

		private final OwfsClient owfsClient;

		public OwfsInvocationHandler(OwfsClient client) {
			this.owfsClient = client;
		}

		@Override
		public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
			reentrantLock.lock();
			try {
				return method.invoke(owfsClient, objects);
			} catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			} finally {
				reentrantLock.unlock();
			}
		}
	}

}
