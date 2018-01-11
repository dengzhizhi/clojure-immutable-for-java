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
package net.ci4j.immutable.redux.reducers;

import net.ci4j.immutable.collections.ImmutableMap;
import net.ci4j.immutable.redux.ReducerBundledAction;
import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxReducer;

/**
 * ActionBundlerReducer is the default handler for {@link ReducerBundledAction}. You must include this reducer
 * in the store to get {@link ReducerBundledAction} working.
 */
public class ActionBundleReducer implements ReduxReducer
{
	@Override
	public ImmutableMap<Object, Object> apply(ReduxAction action, ImmutableMap<Object, Object> state, Object... params)
	{
		if (action instanceof ReducerBundledAction)
		{
			final ReduxReducer reducer = ((ReducerBundledAction) action).getReducer();
			return reducer.apply(action, state, params);
		}
		else
		{
			return state;
		}
	}
}
