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
package net.ci4j.immutable.redux.impl;

import net.ci4j.immutable.collections.ImmutableMap;
import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxReducer;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockingStateCoreStrategy implements StateCoreStrategy
{
	private ImmutableMap<Object, Object> state;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


	public LockingStateCoreStrategy(ImmutableMap<Object, Object> initialState)
	{
		this.state = initialState;
	}

	@Override
	public ImmutableMap<Object, Object> getState()
	{
		lock.readLock().lock();
		try
		{
			return state;
		} finally
		{
			lock.readLock().unlock();
		}
	}

	@Override
	public void reduce(ReduxReducer reducer, ReduxAction action, Object[] params)
	{
		lock.writeLock().lock();
		try
		{
			this.state = reducer.apply(action, this.state, params);
		} finally
		{
			lock.writeLock().unlock();
		}
	}
}
