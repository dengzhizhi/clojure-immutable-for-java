package net.ci4j.immutable.redux;

import net.ci4j.immutable.redux.impl.AtomStateCoreStrategy;
import net.ci4j.immutable.redux.impl.SimpleStateCoreStrategy;
import net.ci4j.immutable.redux.impl.StateCoreStrategy;

import java.util.function.Function;

public enum StateCore
{
	SINGLE_THREAD(SimpleStateCoreStrategy::new),
	LOCKING(SimpleStateCoreStrategy::new),
	ATOM(AtomStateCoreStrategy::new);

	private Function<ReduxState, StateCoreStrategy> strategyFactory;

	StateCore(Function<ReduxState, StateCoreStrategy> strategyFactory)
	{
		this.strategyFactory = strategyFactory;
	}

	public StateCoreStrategy createStrategy(ReduxState initialState)
	{
		return this.strategyFactory.apply(initialState);
	}
}
