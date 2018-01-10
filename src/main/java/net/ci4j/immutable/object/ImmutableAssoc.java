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

import clojure.lang.APersistentMap;
import clojure.lang.ArraySeq;
import clojure.lang.PersistentArrayMap;
import net.ci4j.immutable.clojure_utils.ClojureRT;

import static net.ci4j.immutable.clojure_utils.ClojureRT.DISSOC;
import static net.ci4j.immutable.clojure_utils.ClojureRT.DROP_LAST;

class ImmutableAssoc implements ImmutableBeanAssocStrategy
{
	private APersistentMap internalMap = PersistentArrayMap.EMPTY;

	@Override
	public <CONCRETE extends ImmutableBean> CONCRETE assoc(ImmutableBean<CONCRETE> bean, Object key, Object value)
	{
		return withState(bean, (APersistentMap) ClojureRT.ASSOC.invoke(internalMap, key, value));
	}

	@Override
	public <CONCRETE extends ImmutableBean> CONCRETE assocWithEnum(ImmutableBean<CONCRETE> bean, Enum key, Object value)
	{
		return withState(bean, (APersistentMap) ClojureRT.ASSOC.invoke(internalMap, key.name(), value));
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE assocIn(ImmutableBean<CONCRETE> bean, Object value, Object... path)
	{
		return withState(bean, (APersistentMap) ClojureRT.ASSOC_IN.invoke(internalMap, ArraySeq.create(path), value));
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE assocInWithEnum(ImmutableBean<CONCRETE> bean, Object value, Enum... path)
	{
		return withState(bean, (APersistentMap) ClojureRT.ASSOC_IN.invoke(internalMap, ClojureRT.MAP.invoke(TO_ENUM_NAME, ArraySeq.create((Object[]) path)) , value));
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissoc(ImmutableBean<CONCRETE> bean, Object key)
	{
		return withState(bean, (APersistentMap) DISSOC.invoke(internalMap, key));
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissocWithEnum(ImmutableBean<CONCRETE> bean, Enum key)
	{
		return withState(bean, (APersistentMap) DISSOC.invoke(internalMap, key.name()));
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissocIn(ImmutableBean<CONCRETE> bean, Object... path)
	{
		final Object finalKey = path[path.length - 1];
		return withState(bean, (APersistentMap) ClojureRT.UPDATE_IN.invoke(internalMap, DROP_LAST.invoke(ArraySeq.create(path)), DISSOC, finalKey));
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissocInWithEnum(ImmutableBean<CONCRETE> bean, Enum... path)
	{
		final Object finalKey = path[path.length - 1];
		return withState(bean, (APersistentMap) ClojureRT.UPDATE_IN.invoke(internalMap,
			DROP_LAST.invoke(ClojureRT.MAP.invoke(TO_ENUM_NAME, ArraySeq.create((Object[]) path))), DISSOC, finalKey));
	}

	@Override
	public Object getRaw()
	{
		return internalMap;
	}

	@Override
	public APersistentMap getPersistRaw()
	{
		return internalMap;
	}

	@Override
	public void resetInternalMap(APersistentMap internalMap)
	{
		this.internalMap = internalMap != null ? internalMap : PersistentArrayMap.EMPTY;
	}

	@Override
	public boolean isPersist()
	{
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <CONCRETE extends ImmutableBean> CONCRETE withState(ImmutableBean<CONCRETE> bean, APersistentMap state) {
		final Object raw = internalMap;
		if (raw == state)
		{
			return (CONCRETE) bean;
		}
		return ImmutableBean.create(bean.getConcreteClass(), state, bean.specKey);
	}
}
