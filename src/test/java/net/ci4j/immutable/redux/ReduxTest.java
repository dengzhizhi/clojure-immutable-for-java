package net.ci4j.immutable.redux;

import net.ci4j.immutable.collections.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import static net.ci4j.immutable.redux.ReduxTest.CountingAction.ADD;
import static net.ci4j.immutable.redux.ReduxTest.CountingAction.DEC;
import static net.ci4j.immutable.redux.ReduxTest.CountingAction.INC;
import static net.ci4j.immutable.redux.ReduxTest.CountingAction.MINUS;

public class ReduxTest
{
	enum CountingAction implements ReduxAction
	{
		INC, DEC, ADD, MINUS
	}

	@Test
	public void testSimpleStore()
	{
		final ImmutableMap<Object, Object> initialState = ImmutableMap.create("count", 0);
		final ReduxReducer reducer = Redux.combineReducers((action, state, params) -> {
			if (action == INC)
			{
				return state.update("count", v -> (Integer) v + 1);
			}
			else if (action == DEC)
			{
				return state.update("count", v -> (Integer) v - 1);
			}
			else if (action == ADD)
			{
				return state.update("count", v -> (Integer) v + (Integer) params[0]);
			}
			else if (action == MINUS)
			{
				return state.update("count", v -> (Integer) v + (Integer) params[0]);
			}
			return state;
		});

		final ReduxStore store = Redux.createStore(StateCore.ATOM, initialState, reducer);

		store.dispatch(INC);
		Assert.assertEquals(1, store.getState().get("count"));
	}

}