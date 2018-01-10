/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

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
