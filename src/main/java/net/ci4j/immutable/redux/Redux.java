package net.ci4j.immutable.redux;

import net.ci4j.immutable.redux.impl.ReduxReducer;
import net.ci4j.immutable.redux.impl.Store;

public class Redux
{
	public static ReduxStore createStore(StateCore stateCoreType, ReduxState initialState, ReduxReducer reduxReducer, Middleware... middlewares)
	{
		return new Store(stateCoreType, initialState, reduxReducer, middlewares);
	}

	public static ReduxReducer combineReducers(ReduxReducer... reduxReducers)
	{
		return (action, state) -> {
			ReduxState nextState = state;
			for (ReduxReducer reducer : reduxReducers)
			{
				nextState = reducer.apply(action, nextState);
			}
			return nextState;
		};
	}
}
