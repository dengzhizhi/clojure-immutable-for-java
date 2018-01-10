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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Loggers
{
	public static final Logger MIDDLEWARE_LOGGER = LoggerFactory.getLogger("redux4jpublic .middleware");
	public static final Logger STATE_LOGGER = LoggerFactory.getLogger("redux4j.state");
	public static final Logger DISPATCHER_LOGGER = LoggerFactory.getLogger("redux4jpublic .dispatcher");
	public static final Logger REDUCER_LOGGER = LoggerFactory.getLogger("redux4j.reducer");
	static final Logger NOTIFIER_LOGGER = LoggerFactory.getLogger("redux4j.notifier");
	static final Logger SUBSCRIBING_LOGGER = LoggerFactory.getLogger("redux4j.subscribing");
}
