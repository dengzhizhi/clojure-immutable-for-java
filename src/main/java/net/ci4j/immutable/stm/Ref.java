/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable.stm;

import static net.ci4j.immutable.fn.Fn.fn;

import java.io.Serializable;
import java.util.function.Function;

import net.ci4j.fn.Fn1;
import net.ci4j.fn.VoidFn4;
import net.ci4j.immutable.collections.ImmutableMap;
import net.ci4j.immutable.fn.MetaAlterFn;
import net.ci4j.immutable.fn.WatcherIFn;
import clojure.lang.APersistentMap;
import clojure.lang.IFn;
import clojure.lang.IPersistentMap;
import clojure.lang.IRef;
import clojure.lang.PersistentList;

/**
 * A clojure.lang.Ref wrapper that supports generic type
 *
 *
 * @param <T> type of internal state
 */
/*
 * Development Note: Some operation may require to re-wrapping the internal state, DO NOT store any other states in the wrapper.
 */
@SuppressWarnings("unchecked")
public class Ref<T> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final clojure.lang.Ref wrapped;

	public Ref(clojure.lang.Ref wrapped)
	{
		this.wrapped = wrapped;
	}

	public Ref()
	{
		this.wrapped = new clojure.lang.Ref(null);
	}

	public Ref(T state)
	{
		this.wrapped = new clojure.lang.Ref(state);
	}


	public int compareTo(Ref ref)
	{
		return wrapped.compareTo(ref.wrapped);
	}

	public int getMinHistory()
	{
		return wrapped.getMinHistory();
	}

	public Ref setMinHistory(int minHistory)
	{
		return new Ref(wrapped.setMinHistory(minHistory));
	}

	public int getMaxHistory()
	{
		return wrapped.getMaxHistory();
	}

	public Ref setMaxHistory(int maxHistory)
	{
		return new Ref(wrapped.setMaxHistory(maxHistory));
	}

	public T deref()
	{
		return (T) wrapped.deref();
	}

	public T set(T val)
	{
		return (T) wrapped.set(val);
	}

	/**
	 * Must be called in a transaction. Sets the in-transaction-value of ref to:
	 *
	 * (apply fun in-transaction-value-of-ref args)
	 *
	 * and returns the in-transaction-value of ref.
	 *
	 * At the commit point of the transaction, sets the value of ref to be:
	 *
	 * (apply fun most-recently-committed-value-of-ref args)
	 *
	 * Thus fun should be commutative, or, failing that, you must accept last-one-in-wins behavior.
	 * commute allows for more concurrency than ref-set.
	 * @param fn
	 * @return
	 */
	public T commute(Fn1<T, T> fn)
	{
		return (T) wrapped.commute(fn(fn), PersistentList.EMPTY);
	}

	public T alter(Fn1<T, T> fn)
	{
		return (T) wrapped.alter(fn(fn), PersistentList.EMPTY);
	}

	public void touch()
	{
		wrapped.touch();
	}

	public void trimHistory()
	{
		wrapped.trimHistory();
	}

	public int getHistoryCount()
	{
		return wrapped.getHistoryCount();
	}

	public void setValidator(Fn1<T, Boolean> vf)
	{
		wrapped.setValidator(fn(vf));
	}

	public IFn getValidator()
	{
		return wrapped.getValidator();
	}

	public IPersistentMap getWatches()
	{
		return wrapped.getWatches();
	}

	public IRef addWatch(Object key, VoidFn4<Object, Atom<T>, T, T> callback)
	{
		return wrapped.addWatch(key, new WatcherIFn<>(callback));
	}

	public IRef removeWatch(Object key)
	{
		return wrapped.removeWatch(key);
	}

	public void notifyWatches(T oldval, T newval)
	{
		wrapped.notifyWatches(oldval, newval);
	}

	public ImmutableMap<Object, Object> meta()
	{
		return new ImmutableMap((APersistentMap) wrapped.meta()) ;
	}

	public ImmutableMap<Object, Object> alterMeta(Function<ImmutableMap<Object, Object>, ImmutableMap<Object, Object>> alter)
	{
		return new ImmutableMap((APersistentMap) wrapped.alterMeta(new MetaAlterFn(alter), PersistentList.EMPTY)) ;
	}

	public IPersistentMap resetMeta(ImmutableMap<?, ?> m)
	{
		return wrapped.resetMeta(m.getRaw());
	}
}
