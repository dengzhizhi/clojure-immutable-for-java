/**
 * Copyright (c) 2017 Zhizhi Deng
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.ci4j.immutable.redux.impl;

import com.fasterxml.jackson.core.JsonParseException;
import net.ci4j.fn.VoidFn2;
import net.ci4j.immutable.clojure_utils.ClojureJson;
import net.ci4j.immutable.collections.ImmutableList;
import net.ci4j.immutable.collections.ImmutableMap;
import net.ci4j.immutable.redux.Middleware;
import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxReducer;
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

	private HashMap<UUID, Consumer<ImmutableMap<Object, Object>>> subscribers = new HashMap<>();

	public Store(StateCore coreType, ImmutableMap<Object, Object> initialState, ReduxReducer reducer)
	{
		this(coreType.createStrategy(initialState), reducer);
	}

	public Store(StateCoreStrategy coreStrategy, ReduxReducer reducer)
	{
		this.coreStrategy = coreStrategy;
		this.reducer = reducer;
	}

	public Store(StateCore coreType, ImmutableMap<Object, Object> initialState, ReduxReducer reducer, Middleware... middlewares)
	{
		this(coreType.createStrategy(initialState), reducer, middlewares);
	}

	public Store(StateCoreStrategy coreStrategy, ReduxReducer reducer, Middleware... middlewares)
	{
		this(coreStrategy, reducer);
		this.middlewares = ImmutableList.create(middlewares);
	}

	@Override
	public ImmutableMap<Object, Object> getState()
	{
		return coreStrategy.getState();
	}

	@Override
	public void dispatch(ReduxAction action, Object... params)
	{
		if (this.middlewares == null || middlewares.isEmpty())
		{
			internalDispatch(action, params);
		}
		else
		{
			for (VoidFn2<ReduxAction, ImmutableMap<Object, Object>> middleware : this.middlewares)
			{
				internalDispatch(action, params);
				middleware.apply(action, this.getState());
			}
		}
	}

	private void internalDispatch(ReduxAction action, Object... params)
	{
		if (DISPATCHER_LOGGER.isTraceEnabled())
		{
			DISPATCHER_LOGGER.trace("Dispatching: " + action);
		}
		reduce(action, params);
		notifySubscribers();
	}

	private void notifySubscribers()
	{
		if (NOTIFIER_LOGGER.isTraceEnabled())
		{
			NOTIFIER_LOGGER.trace("Notifying {} subscribers.", this.subscribers.size());
		}

		for (Consumer<ImmutableMap<Object, Object>> e : subscribers.values())
		{
			e.accept(this.getState());
		}
	}

	@Override
	public UUID subscribe(Consumer<ImmutableMap<Object, Object>> subscriber)
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

	private void reduce(ReduxAction action, Object... params)
	{
		if (REDUCER_LOGGER.isTraceEnabled())
		{
			REDUCER_LOGGER.trace("Will reduce: " + this.getState());
		}

		this.coreStrategy.reduce(this.reducer, action, params);

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
