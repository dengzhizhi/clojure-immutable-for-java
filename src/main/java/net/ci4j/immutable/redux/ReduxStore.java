package net.ci4j.immutable.redux;

import net.ci4j.immutable.collections.ImmutableMap;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Consumer;

public interface ReduxStore extends Serializable
{
	ImmutableMap getState();

	void dispatch(ReduxAction action);

	UUID subscribe(Consumer<ReduxState> subscriber);

	void unsubscribe(UUID subscribeId);
}
