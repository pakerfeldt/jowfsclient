package org.owfs.jowfsclient.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.ReentrantLock;
import org.owfs.jowfsclient.OwfsConnection;

/**
 * OwfsConnectionFactory that executes all methods utilizing ReentrantLock.
 *
 * @author Tom Kucharski
 */
public class OwfsConnectionThreadSafeProxy {

	private ReentrantLock reentrantLock = new ReentrantLock();

	public void setLock(ReentrantLock lock) {
		this.reentrantLock = lock;
	}

	public OwfsConnection decorate(OwfsConnection client) {
		OwfsConnectionThreadSafeProxy.OwfsInvocationHandler owfsInvocationHandler = new OwfsConnectionThreadSafeProxy.OwfsInvocationHandler(client);
		return (OwfsConnection) Proxy.newProxyInstance(client.getClass().getClassLoader(), new Class[]{OwfsConnection.class}, owfsInvocationHandler);
	}

	private class OwfsInvocationHandler implements InvocationHandler {

		private final OwfsConnection owfsConnection;

		public OwfsInvocationHandler(OwfsConnection client) {
			this.owfsConnection = client;
		}

		@Override
		public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
			reentrantLock.lock();
			try {
				return method.invoke(owfsConnection, objects);
			} catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			} finally {
				reentrantLock.unlock();
			}
		}
	}

}
