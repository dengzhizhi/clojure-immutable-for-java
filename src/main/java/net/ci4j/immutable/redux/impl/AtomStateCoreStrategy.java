package net.ci4j.immutable.redux.impl;

import net.ci4j.immutable.redux.ReduxState;
import net.ci4j.immutable.stm.Atom;

public class AtomStateCoreStrategy implements StateCoreStrategy
{
	private Atom<ReduxState> state;

	public AtomStateCoreStrategy(ReduxState initialState)
	{
		state = new Atom<>(initialState);
	}

	@Override
	public ReduxState getState()
	{
		return state.deref();
	}

	@Override
	public void setState(ReduxState state)
	{
		this.state.reset(state);
	}
}
