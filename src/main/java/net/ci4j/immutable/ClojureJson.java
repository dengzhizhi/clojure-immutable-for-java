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
