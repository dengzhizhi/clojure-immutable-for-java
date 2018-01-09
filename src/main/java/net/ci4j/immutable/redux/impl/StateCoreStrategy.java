package net.ci4j.immutable.redux.impl;

import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxState;

public interface StateCoreStrategy
{
	ReduxState getState();

	void setState(ReduxState state);

	default void reduce(ReduxReducer reducer, ReduxAction action)
	{
		setState(reducer.apply(action, getState()));
	}
}
