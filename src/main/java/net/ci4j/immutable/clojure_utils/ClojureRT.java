/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable.clojure_utils;

import net.ci4j.fn.Fn0;
import clojure.java.api.Clojure;
import clojure.lang.ArraySeq;
import clojure.lang.IFn;
import clojure.lang.ISeq;
import clojure.lang.Keyword;
import clojure.lang.Namespace;
import clojure.lang.RT;
import clojure.lang.Symbol;
import clojure.lang.Var;

/**
 * Utils for immutable collections
 */
@SuppressWarnings("unchecked")
public class ClojureRT
{
	private static final Var EVAL = var("clojure.core", "eval");

	public static final Var REQUIRE = var("clojure.core", "require");
	static {
		require("clojure.spec.alpha");
		require("net.ci4j.clj.spec");
	}
	private static final Namespace SPEC_NS = Namespace.findOrCreate(Symbol.intern("net.ci4j.clj.spec"));
	public static final Var LOAD = var("clojure.core", "load");
	public static final Var LOAD_FILE = var("clojure.core", "load-file");
	public static final Var READ_STRING = var("clojure.core", "read-string");
	public static final Var RESOLVE = var("clojure.core", "resolve");
	private static final IFn EVAL_IN = (IFn)eval("(fn [ns code] (binding [*ns* ns] (eval (read-string code))))");
	private static final IFn REQUIRE_IN = (IFn)eval("(fn [NS1 NS2 reload] (binding [*ns* NS1] (if reload (require NS2 :reload) (require NS2))))");

	public static final IFn ASSOC_IN = var("clojure.core", "assoc-in");
	public static final IFn ASSOC = var("clojure.core", "assoc");
	public static final IFn UPDATE_IN = var("clojure.core", "update-in");
	public static final IFn UPDATE = var("clojure.core", "update");
	public static final IFn DISSOC = var("clojure.core", "dissoc");
	public static final IFn DROP_LAST = var("clojure.core", "drop-last");
	public static final IFn GET_IN = var("clojure.core", "get-in");
	public static final IFn CONS = var("clojure.core", "cons");
	public static final IFn MERGE = var("clojure.core", "merge");
	public static final IFn MERGE_WITH = var("clojure.core", "merge-with");

	public static final IFn MAP = var("clojure.core", "map");

	public static final IFn SWAP_ATOM = var("clojure.core", "swap!");
	public static final IFn RESET_ATOM = var("clojure.core", "reset!");
	public static final IFn STR = var("clojure.core", "str");
	public static final IFn INC = var("clojure.core", "inc");
	public static final IFn DEC = var("clojure.core", "dec");
	public static final IFn REMOVE_NTH_IN_VEC = eval("(fn [v i] (vec (concat (subvec v 0 i) (subvec v (inc i)))))");
	public static final IFn INSERT_NTH_IN_VEC = eval("(fn [v i o] (let [[before after] (split-at i v)] (vec (concat before [o] after))))");

	public static final IFn SPEC_VALID = var("clojure.spec.alpha", "valid?");
	public static final IFn SPEC_EXPLAIN = var("clojure.spec.alpha", "explain-str");

	public static final IFn VEC = var("clojure.core", "vec");


	public static <T> T clj(String code) {
		return (T) Clojure.read(code);
	}

	public static <T> T requireIn(String ns, String name) {
		return requireIn(ns, name, false);
	}

	public static <T> T requireIn(String ns, String name, boolean reload) {
		return (T) (reload ? REQUIRE_IN.invoke(Namespace.findOrCreate(Symbol.intern(ns)), clj(name), clj(":reload")) : REQUIRE_IN.invoke(Namespace.findOrCreate(Symbol.intern(ns)), clj(name)));
	}

	public static <T> T require(String name) {
		return require(name, false);
	}

	public static <T> T require(String name, boolean reload) {
		return (T) (reload ? REQUIRE.invoke(clj(name), clj(":reload")) : REQUIRE.invoke(clj(name)));
	}

	public static <T> T readString(String code) {
		return (T) READ_STRING.invoke(code);
	}

	public static <T> T eval(String... codes) {
		return (T) EVAL.invoke(readString(String.join("\n", codes)));
	}

	public static <T> T evalIn(String ns, String... codes) {
		return evalIn(Namespace.findOrCreate(Symbol.intern(ns)), codes);
	}

	public static <T> T evalIn(Namespace ns, String... codes) {
		return (T) EVAL_IN.invoke(ns, String.join("\n", codes));
	}

	public static Var evalAsVar(String... codes) {
		return (Var)EVAL.invoke(readString(String.join("\n", codes)));
	}

	public static Object load(String name) {
		return LOAD.invoke(name);
	}

	public static Object loadFile(String name) {
		return LOAD_FILE.invoke(name);
	}

	public static IFn fn(String name) {
		return Clojure.var(name);
	}

	public static Var var(String name) {
		return (Var)Clojure.var(name);
	}

	public static Var var(String ns, String name) {
		return RT.var(ns, name);
	}

	public static Var loadFn(String ns, String name) {
		require(ns);
		return var(ns, name);
	}

	public static boolean specValid(Keyword specKey, Object data)
	{
		return (Boolean) SPEC_VALID.invoke(specKey, data);
	}

	public static String specExplain(Keyword specKey, Object data)
	{
		return (String) SPEC_EXPLAIN.invoke(specKey, data);
	}

	public static Object resolve(String name) {
		return RESOLVE.invoke(clj(name));
	}

	public static ISeq pathSeq(Object... path) {
		return ArraySeq.create(path);
	}
}
