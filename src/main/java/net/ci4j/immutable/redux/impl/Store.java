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

import com.fasterxml.jackson.core.JsonParseException;
import net.ci4j.fn.VoidFn2;
import net.ci4j.immutable.clojure_utils.ClojureJson;
import net.ci4j.immutable.collections.ImmutableList;
import net.ci4j.immutable.redux.Middleware;
import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxState;
import net.ci4j.immutable.redux.ReduxStore;
import net.ci4j.immutable.redux.StateCore;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import static net.ci4j.immutable.redux.impl.Loggers.DISPATCHER_LOGGER;
import static net.ci4j.immutable.redux.impl.Loggers.NOTIFIER_LOGGER;
import static net.ci4j.immutable.redux.impl.Loggers.REDUCER_LOGGER;
import static net.ci4j.immutable.redux.impl.Loggers.SUBSCRIBING_LOGGER;

public class Store implements ReduxStore
{
	protected final StateCoreStrategy coreStrategy;

	private final ReduxReducer reducer;

	private ImmutableList<Middleware> middlewares;

	private HashMap<UUID, Consumer<ReduxState>> subscribers = new HashMap<>();

	public Store(StateCore coreType, ReduxState initialState, ReduxReducer reducer)
	{
		this(coreType.createStrategy(initialState), reducer);
	}

	public Store(StateCoreStrategy coreStrategy, ReduxReducer reducer)
	{
		this.coreStrategy = coreStrategy;
		this.reducer = reducer;
	}

	public Store(StateCore coreType, ReduxState initialState, ReduxReducer reducer, Middleware... middlewares)
	{
		this(coreType.createStrategy(initialState), reducer, middlewares);
	}

	public Store(StateCoreStrategy coreStrategy, ReduxReducer reducer, Middleware... middlewares)
	{
		this(coreStrategy, reducer);
		this.middlewares = ImmutableList.create(middlewares);
	}

	@Override
	public ReduxState getState()
	{
		return coreStrategy.getState();
	}

	@Override
	public void dispatch(ReduxAction action)
	{
		if (this.middlewares == null)
		{
			internalDispatch(action);
		}
		else
		{
			for (VoidFn2<ReduxAction, ReduxState> middleware : this.middlewares)
			{
				internalDispatch(action);
				middleware.apply(action, this.getState());
			}
		}
	}

	private void internalDispatch(ReduxAction action)
	{
		if (DISPATCHER_LOGGER.isTraceEnabled())
		{
			DISPATCHER_LOGGER.trace("Dispatching: " + action);
		}
		reduce(action);
		notifySubscribers();
	}

	private void notifySubscribers()
	{
		if (NOTIFIER_LOGGER.isTraceEnabled())
		{
			NOTIFIER_LOGGER.trace("Notifying {} subscribers.", this.subscribers.size());
		}

		for (Consumer<ReduxState> e : subscribers.values())
		{
			e.accept(this.getState());
		}
	}

	@Override
	public UUID subscribe(Consumer<ReduxState> subscriber)
	{
		UUID uuid = UUID.randomUUID();
		this.subscribers.put(uuid, subscriber);

		if (SUBSCRIBING_LOGGER.isTraceEnabled())
		{
			SUBSCRIBING_LOGGER.trace("Subscribing {}. ID: {}.", subscriber, uuid);
		}

		return uuid;
	}

	@Override
	public void unsubscribe(UUID subscriberId)
	{
		if (SUBSCRIBING_LOGGER.isTraceEnabled())
		{
			SUBSCRIBING_LOGGER.trace("Un-subscribing {}.", subscriberId);
		}

		this.subscribers.remove(subscriberId);
	}

	private void reduce(ReduxAction action)
	{
		if (REDUCER_LOGGER.isTraceEnabled())
		{
			REDUCER_LOGGER.trace("Will reduce: " + this.getState());
		}

		this.coreStrategy.reduce(this.reducer, action);

		if (REDUCER_LOGGER.isTraceEnabled())
		{
			REDUCER_LOGGER.trace("Reduced state to: " + this.getState());
		}
	}

	public String getStateJson() throws JsonParseException
	{
		return (String) ClojureJson.GENERATE_STRING.invoke(this.coreStrategy.getState().getRaw());
	}
}
