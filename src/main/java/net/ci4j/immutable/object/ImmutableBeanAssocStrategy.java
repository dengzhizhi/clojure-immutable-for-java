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
