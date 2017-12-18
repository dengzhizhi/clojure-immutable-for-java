package net.ci4j.immutable.stm;

import org.junit.Assert;
import org.junit.Test;

public class STMTransactionTest
{
	@Test
	public void testDoSyncCommit() throws Exception {
		final Ref<Long> x = new Ref<>(3L);
		final Ref<Long> y = new Ref<>(4L);

		STMTransaction.doSync(() -> {
			x.alter(it -> it + 1);
			y.alter(it -> it - 1);
		});

		Assert.assertEquals(Long.valueOf(4), x.deref());
		Assert.assertEquals(Long.valueOf(3), y.deref());
	}

	@Test
	public void testDoSyncRollback() throws Exception {
		final Ref<Long> x = new Ref<>(3L);
		final Ref<Long> y = new Ref<>(4L);

		try {
			STMTransaction.doSync(() -> {
				x.set(8L);
				y.alter(it -> 10 / (it - 4)); //Dived by zero
			});
			Assert.fail("Should throw exception");
		} catch (STMTransactionInvocationException ignored) {
			//ignored
		}
		Assert.assertEquals(Long.valueOf(3), x.deref());
		Assert.assertEquals(Long.valueOf(4), y.deref());
	}

}