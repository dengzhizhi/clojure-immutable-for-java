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
package net.ci4j.immutable.object;

import net.ci4j.fn.Fn1;
import net.ci4j.immutable.fn.Fn;
import clojure.lang.APersistentMap;
import clojure.lang.IFn;
import clojure.lang.ILookup;

public interface ImmutableBeanAssocStrategy
{
	IFn TO_ENUM_NAME = Fn.fn((Fn1<Enum, String>) Enum::name);

	Object getRaw();

	APersistentMap getPersistRaw();

	default ILookup getLookupRaw()
	{
		return (ILookup) getRaw();
	}

	void resetInternalMap(APersistentMap internalMap);

	boolean isPersist();

	<CONCRETE extends ImmutableBean> CONCRETE assoc(ImmutableBean<CONCRETE> bean, Object key, Object value);

	<CONCRETE extends ImmutableBean> CONCRETE assocWithEnum(ImmutableBean<CONCRETE> bean, Enum key, Object value);

	<CONCRETE extends ImmutableBean> CONCRETE assocIn(ImmutableBean<CONCRETE> bean, Object value, Object... path);

	<CONCRETE extends ImmutableBean> CONCRETE assocInWithEnum(ImmutableBean<CONCRETE> bean, Object value, Enum... path);

	<CONCRETE extends ImmutableBean> CONCRETE dissoc(ImmutableBean<CONCRETE> bean, Object key);

	<CONCRETE extends ImmutableBean> CONCRETE dissocWithEnum(ImmutableBean<CONCRETE> bean, Enum key);

	<CONCRETE extends ImmutableBean> CONCRETE dissocIn(ImmutableBean<CONCRETE> bean, Object... path);

	<CONCRETE extends ImmutableBean> CONCRETE dissocInWithEnum(ImmutableBean<CONCRETE> bean, Enum... path);

	@SuppressWarnings("unchecked")
	<CONCRETE extends ImmutableBean> CONCRETE withState(ImmutableBean<CONCRETE> bean, APersistentMap state);
}
