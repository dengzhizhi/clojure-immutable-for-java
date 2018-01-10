/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

package net.ci4j.immutable.redux.impl;

import net.ci4j.fn.Fn2;
import net.ci4j.immutable.redux.ReduxAction;
import net.ci4j.immutable.redux.ReduxState;

import java.io.Serializable;

@FunctionalInterface
public interface ReduxReducer extends Fn2<ReduxAction, ReduxState, ReduxState>, Serializable
{}
