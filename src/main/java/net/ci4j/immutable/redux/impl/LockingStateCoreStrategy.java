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
	public void setState(ReduxState state)
	{
		lock.writeLock().lock();
		try
		{
			this.state = state;
		} finally
		{
			lock.writeLock().unlock();
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
