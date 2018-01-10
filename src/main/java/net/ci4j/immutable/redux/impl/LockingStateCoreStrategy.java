/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

package net.ci4j.immutable.redux.impl;

import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxState;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockingStateCoreStrategy implements StateCoreStrategy
{
	private ReduxState state;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


	public LockingStateCoreStrategy(ReduxState initialState)
	{
		this.state = initialState;
	}

	@Override
	public ReduxState getState()
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
	public void reduce(ReduxReducer reducer, ReduxAction action)
	{
		lock.writeLock().lock();
		try
		{
			this.state = reducer.apply(action, this.state);
		} finally
		{
			lock.writeLock().unlock();
		}
	}
}
