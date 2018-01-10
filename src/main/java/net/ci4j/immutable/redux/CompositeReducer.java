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
