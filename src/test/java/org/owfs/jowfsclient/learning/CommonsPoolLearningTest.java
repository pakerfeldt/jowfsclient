package org.owfs.jowfsclient.learning;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.owfs.jowfsclient.TestNGGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Tom Kucharski
 */
@Test(groups = TestNGGroups.LEARNING)
public class CommonsPoolLearningTest {

	@Mock
	private PoolableObjectFactory<Integer> objectFactory;
	private GenericObjectPool<Integer> genericObjectPool;

	@BeforeMethod
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		genericObjectPool = new GenericObjectPool<Integer>(objectFactory);
	}

	public void shouldReturnTheSameValue() throws Exception {
		//given
		when(objectFactory.makeObject()).thenReturn(1);

		//when
		Integer v1 = genericObjectPool.borrowObject();
		genericObjectPool.returnObject(v1);
		genericObjectPool.borrowObject();

		//then
		verify(objectFactory, times(1)).makeObject();
		verify(objectFactory, times(0)).validateObject(any(Integer.class));
	}


	public void shouldValidateOnEveryRequest() throws Exception {
		//given
		when(objectFactory.makeObject()).thenReturn(1);
		when(objectFactory.validateObject(any(Integer.class))).thenReturn(true);
		genericObjectPool.setTestOnBorrow(true);
		GenericObjectPool<Integer> spy = spy(genericObjectPool);

		//when
		spy.borrowObject();

		//then
		InOrder inOrder = inOrder(objectFactory, spy);
		inOrder.verify(spy).borrowObject();
		inOrder.verify(objectFactory).makeObject();
		inOrder.verify(objectFactory).validateObject(any(Integer.class));
	}


}
