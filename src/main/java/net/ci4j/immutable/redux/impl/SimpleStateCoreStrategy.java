package net.ci4j.immutable.redux.impl;

import net.ci4j.immutable.redux.ReduxState;

public class SimpleStateCoreStrategy implements StateCoreStrategy
{
	private ReduxState state;

	public SimpleStateCoreStrategy(ReduxState initialState)
	{
		this.state = initialState;
	}

	@Override
	public ReduxState getState()
	{
		return state;
	}

	@Override
	public void setState(ReduxState state)
	{
		this.state = state;
	}
}
