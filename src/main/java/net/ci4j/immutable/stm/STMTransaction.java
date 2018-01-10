/**
 * Copyright (c) 2017 Zhizhi Deng
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
