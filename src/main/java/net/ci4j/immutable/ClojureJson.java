/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable;

import clojure.lang.IFn;

public class ClojureJson
{
	static {
		ClojureRT.require("cheshire.core");
	}
	public static IFn GENERATE_STRING = ClojureRT.var("cheshire.core", "generate-string");
	public static IFn PARSE_STRING = ClojureRT.var("cheshire.core", "parse-string");
}
