package org.owfs.jowfsclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tom Kucharski
 * @since 12/7/12 10:32 PM
 */
public class OwfsClientThreadSafeFactory {

    private ReentrantLock reentrantLock = new ReentrantLock();

    public void setLock(ReentrantLock lock) {
        this.reentrantLock = lock;
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

    public OwfsClient createOwfsClient(OwfsClient client) {
        OwfsClientThreadSafeFactory.OwfsInvocationHandler owfsInvocationHandler = new OwfsClientThreadSafeFactory.OwfsInvocationHandler(client);
        return (OwfsClient) Proxy.newProxyInstance(client.getClass().getClassLoader(), new Class[]{OwfsClient.class}, owfsInvocationHandler);
    }

}
