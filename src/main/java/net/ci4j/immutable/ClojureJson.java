package net.ci4j.immutable;

import net.ci4j.immutable.object.ImmutableObject;
import clojure.lang.IFn;
import clojure.lang.IPersistentCollection;

public class ClojureJson
{
	static {
		ClojureRT.require("cheshire.core");
	}
	public static IFn GENERATE_STRING = ClojureRT.var("cheshire.core", "generate-string");
	public static IFn PARSE_STRING = ClojureRT.var("cheshire.core", "parse-string");

	/**
	 * Parse a json string strictly. The json string must follow the JSON standard, e.g. keys have to be double quoted.
	 * @param jsonString
	 * @return
	 */
	public static ImmutableObject parse(String jsonString)
	{
		return new ImmutableObject((IPersistentCollection) PARSE_STRING.invoke(jsonString));
	}

	public static String generateFromEDN(ImmutableObject source) {
		return (String) GENERATE_STRING.invoke(source.getRaw());
	}

	public static String generate(Object source) {
		return (String) GENERATE_STRING.invoke(source);
	}
}
