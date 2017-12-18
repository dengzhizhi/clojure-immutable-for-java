package net.ci4j.immutable.object;

import clojure.lang.APersistentMap;
import clojure.lang.ATransientMap;

public class TransientAssoc implements ImmutableBeanAssocStrategy
{
	private ATransientMap internalMap;

	public TransientAssoc(ATransientMap state)
	{
		this.internalMap = state;
	}

	@Override
	public <CONCRETE extends ImmutableBean> CONCRETE assoc(ImmutableBean<CONCRETE> bean, Object key, Object value)
	{
		internalMap.assoc(key, value);
		return (CONCRETE) bean;
	}

	@Override
	public <CONCRETE extends ImmutableBean> CONCRETE assocWithEnum(ImmutableBean<CONCRETE> bean, Enum key, Object value)
	{
		internalMap.assoc(key.name(), value);
		return (CONCRETE) bean;
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE assocIn(ImmutableBean<CONCRETE> bean, Object value, Object... path)
	{
		throw new UnsupportedOperationException("assocIn operation not supported in transient mode.");
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE assocInWithEnum(ImmutableBean<CONCRETE> bean, Object value, Enum... path)
	{
		throw new UnsupportedOperationException("assocIn operation not supported in transient mode.");
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissoc(ImmutableBean<CONCRETE> bean, Object key)
	{
		internalMap.without(key);
		return (CONCRETE) bean;
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissocWithEnum(ImmutableBean<CONCRETE> bean, Enum key)
	{
		internalMap.without(key.name());
		return (CONCRETE) bean;
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissocIn(ImmutableBean<CONCRETE> bean, Object... path)
	{
		throw new UnsupportedOperationException("dissocIn operation not supported in transient mode.");
	}

    @Override
	public <CONCRETE extends ImmutableBean> CONCRETE dissocInWithEnum(ImmutableBean<CONCRETE> bean, Enum... path)
	{
		throw new UnsupportedOperationException("dissocIn operation not supported in transient mode.");
	}

	@Override
	public Object getRaw()
	{
		return internalMap;
	}

	@Override
	public APersistentMap getPersistRaw()
	{
		return (APersistentMap) internalMap.persistent();
	}

	@Override
	public void resetInternalMap(APersistentMap internalMap)
	{
		throw new UnsupportedOperationException("Resetting internal state is not supported in transient mode.");
	}

	@Override
	public boolean isPersist()
	{
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <CONCRETE extends ImmutableBean> CONCRETE withState(ImmutableBean<CONCRETE> bean, APersistentMap state) {
		//** Calling withState in transient bean will result in a persist instance
		return ImmutableBean.create(bean.getConcreteClass(), state, bean.specKey);
	}
}
