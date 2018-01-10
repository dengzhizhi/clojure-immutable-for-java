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

import net.ci4j.immutable.collections.ImmutableList;
import net.ci4j.immutable.redux.impl.ReduxReducer;
import net.ci4j.immutable.stm.Atom;

public class CompositeReducer implements ReduxReducer
{
	private Atom<ImmutableList<ReduxReducer>> reducers;

	public CompositeReducer()
	{
		reducers = new Atom<>(ImmutableList.refEmpty());
	}

	public void addReducer(ReduxReducer reducer)
	{
		reducers.swap(list -> list.cons(reducer));
	}

	public void addReducers(ReduxReducer... reducers)
	{
		this.reducers.swap(list -> list.consAll(reducers));
	}

	@Override
	public ReduxState apply(ReduxAction action, ReduxState state)
	{
		ReduxState newState = this.reducers.deref().reduce(state, (s, r) -> r.apply(action, s));
		return newState;
	}
}
