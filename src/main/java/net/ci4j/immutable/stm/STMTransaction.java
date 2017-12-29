/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable.stm;

import clojure.lang.LockingTransaction;
import net.ci4j.fn.Fn0;
import net.ci4j.fn.VoidFn0;
import net.ci4j.immutable.fn.Fn;

@SuppressWarnings("unchecked")
public class STMTransaction
{
	/**
	 * Runs the action in a transaction. Starts a transaction if none is already
	 * running on this thread. Any uncaught exception will abort the transaction and
	 * flow out of sync. The action may be run more than once, but any effects on
	 * Refs will be atomic.
	 * 
	 * @param action The action to be perform in STM transaction
	 * @return Pass through the return value from action
	 */
	public static <T> T doSync(Fn0<T> action) throws STMTransactionInvocationException
	{
		try
		{
			return (T) LockingTransaction.runInTransaction(Fn.fn(action));
		} catch (Exception e)
		{
			throw new STMTransactionInvocationException(e);
		}
	}

	/**
	 * Runs the action in a transaction. Starts a transaction if none is already
	 * running on this thread. Any uncaught exception will abort the transaction and
	 * flow out of sync. The action may be run more than once, but any effects on
	 * Refs will be atomic.
	 *
	 * @param action The action to be perform in STM transaction
	 */
	public static void doSync(VoidFn0 action) throws STMTransactionInvocationException
	{
		try
		{
			LockingTransaction.runInTransaction(Fn.fn(action));
		} catch (Exception e)
		{
			throw new STMTransactionInvocationException(e);
		}
	}
}
