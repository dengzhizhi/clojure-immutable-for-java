/**
 * Copyright (c) 2017 Zhizhi Deng
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
