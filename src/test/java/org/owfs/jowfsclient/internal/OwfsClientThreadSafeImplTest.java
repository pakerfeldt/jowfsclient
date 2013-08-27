package org.owfs.jowfsclient.internal;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import org.mockito.InOrder;
import org.owfs.jowfsclient.OwfsConnection;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
@Test(groups = TestNGGroups.UNIT)
public class OwfsClientThreadSafeImplTest {

	private OwfsConnectionThreadSafeProxy factory;
	private ReentrantLock lock;
	private OwfsConnection mockOwfsConnection;
	private OwfsConnection threadSafeOwfsConnection;

	@BeforeMethod
	public void initFactory() {
		factory = new OwfsConnectionThreadSafeProxy();
		lock = spy(new ReentrantLock());
		factory.setLock(lock);
		mockOwfsConnection = mock(OwfsConnection.class);
		threadSafeOwfsConnection = factory.decorate(mockOwfsConnection);
	}

	@Test
	public void shouldInheritFromOwfsClient() {
		assertThat(threadSafeOwfsConnection).isInstanceOf(OwfsConnection.class);
	}

	@Test
	public void shouldDelegateToRealMethod() throws IOException, OwfsException {
		//when
		threadSafeOwfsConnection.read(null);
		threadSafeOwfsConnection.disconnect();

		//then
		verify(mockOwfsConnection, times(1)).read(null);
		verify(mockOwfsConnection, times(1)).disconnect();

	}

	@Test
	public void shouldLockAndUnlockDuringInvocation() throws IOException {
		//when
		threadSafeOwfsConnection.disconnect();

		//then
		InOrder inorder = inOrder(lock, mockOwfsConnection);

		inorder.verify(lock, times(1)).lock();
		inorder.verify(mockOwfsConnection, times(1)).disconnect();
		inorder.verify(lock, times(1)).unlock();
	}

	@Test
	public void shouldAlwaysUnlockAfterExceptionIsThrown() throws IOException, OwfsException {
		//given
		doThrow(new RuntimeException()).when(mockOwfsConnection).disconnect();

		//when
		try {
			threadSafeOwfsConnection.disconnect();
		} catch (Exception e) {
			//then
			verify(lock, times(1)).unlock();
		}

	}

}
