/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable.object;

import clojure.lang.APersistentMap;
import clojure.lang.ArraySeq;
import clojure.lang.PersistentArrayMap;
import net.ci4j.immutable.ClojureRT;

import static net.ci4j.immutable.ClojureRT.DISSOC;
import static net.ci4j.immutable.ClojureRT.DROP_LAST;

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
