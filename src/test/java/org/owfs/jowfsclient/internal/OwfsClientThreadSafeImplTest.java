package org.owfs.jowfsclient.internal;

import org.mockito.InOrder;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientThreadSafeFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Tom Kucharski
 * @since 12/7/12 10:30 PM
 */
@Test(groups = TestNGGroups.UNIT)
public class OwfsClientThreadSafeImplTest {

    private OwfsClientThreadSafeFactory factory;
    private ReentrantLock lock;
    private OwfsClient mockOwfsClient;
    private OwfsClient threadSafeOwfsClient;

    @BeforeMethod
    public void initFactory() {
        factory = new OwfsClientThreadSafeFactory();
        lock = spy(new ReentrantLock());
        factory.setLock(lock);
        mockOwfsClient = mock(OwfsClient.class);
        threadSafeOwfsClient = factory.decorate(mockOwfsClient);
    }

    @Test
    public void shouldInheritFromOwfsClient() {
        assertThat(threadSafeOwfsClient).isInstanceOf(OwfsClient.class);
    }

    @Test
    public void shouldDelegateToRealMethod() throws IOException, OwfsException {
        //when
        threadSafeOwfsClient.read(null);
        threadSafeOwfsClient.disconnect();

        //then
        verify(mockOwfsClient, times(1)).read(null);
        verify(mockOwfsClient, times(1)).disconnect();

    }

    @Test
    public void shouldLockAndUnlockDuringInvocation() throws IOException {
        //when
        threadSafeOwfsClient.disconnect();

        //then
        InOrder inorder = inOrder(lock, mockOwfsClient);

        inorder.verify(lock, times(1)).lock();
        inorder.verify(mockOwfsClient, times(1)).disconnect();
        inorder.verify(lock, times(1)).unlock();
    }

    @Test
    public void shouldAlwaysUnlockAfterExceptionIsThrown() throws IOException, OwfsException {
        //given
        doThrow(new RuntimeException()).when(mockOwfsClient).disconnect();

        //when
        try {
            threadSafeOwfsClient.disconnect();
        } catch (Exception e) {

            //then
            verify(lock, times(1)).unlock();
        }

    }

}
