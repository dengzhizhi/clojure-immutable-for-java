package net.ci4j.immutable.redux;

import net.ci4j.fn.VoidFn2;

import java.io.Serializable;

@FunctionalInterface
public interface Middleware extends VoidFn2<ReduxAction, ReduxState>, Serializable
{}
