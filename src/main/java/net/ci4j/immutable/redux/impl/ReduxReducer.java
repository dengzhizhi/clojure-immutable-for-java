package net.ci4j.immutable.redux.impl;

import net.ci4j.fn.Fn2;
import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxState;

import java.io.Serializable;

@FunctionalInterface
public interface ReduxReducer extends Fn2<ReduxAction, ReduxState, ReduxState>, Serializable
{}
