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
