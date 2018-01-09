package net.ci4j.immutable.redux;

import clojure.lang.APersistentMap;
import net.ci4j.immutable.collections.ImmutableMap;

public class ReduxState extends ImmutableMap<Object, Object>
{
	public ReduxState(APersistentMap state)
	{
		super(state);
	}
}
