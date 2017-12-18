package net.ci4j.immutable.fn;

import net.ci4j.fn.Fn0;
import net.ci4j.fn.Fn1;
import net.ci4j.fn.Fn2;
import net.ci4j.fn.Fn3;
import net.ci4j.fn.Fn4;
import net.ci4j.fn.Fn5;
import net.ci4j.fn.Fn6;
import net.ci4j.fn.Fn7;
import net.ci4j.fn.Fn8;
import net.ci4j.fn.Fn9;
import net.ci4j.fn.VoidFn0;
import net.ci4j.fn.VoidFn1;
import net.ci4j.fn.VoidFn2;
import net.ci4j.fn.VoidFn3;
import net.ci4j.fn.VoidFn4;
import net.ci4j.fn.VoidFn5;
import net.ci4j.fn.VoidFn6;
import net.ci4j.fn.VoidFn7;
import net.ci4j.fn.VoidFn8;
import net.ci4j.fn.VoidFn9;
import clojure.lang.IFn;

public class Fn
{
	public static <R> IFn fn(Fn0<R> fn) {
		return new IFn0(fn);
	}

	public static <P1, R> IFn fn(Fn1<P1, R> fn) {
		return new IFn1(fn);
	}

	public static <P1, P2, R> IFn fn(Fn2<P1, P2, R> fn) {
		return new IFn2(fn);
	}

	public static <P1, P2, P3, R> IFn fn(Fn3<P1, P2, P3, R> fn) {
		return new IFn3(fn);
	}

	public static <P1, P2, P3, P4, R> IFn fn(Fn4<P1, P2, P3, P4, R> fn) {
		return new IFn4(fn);
	}

	public static <P1, P2, P3, P4, P5, R> IFn fn(Fn5<P1, P2, P3, P4, P5, R> fn) {
		return new IFn5(fn);
	}

	public static <P1, P2, P3, P4, P5, P6, R> IFn fn(Fn6<P1, P2, P3, P4, P5, P6, R> fn) {
		return new IFn6(fn);
	}

	public static <P1, P2, P3, P4, P5, P6, P7, R> IFn fn(Fn7<P1, P2, P3, P4, P5, P6, P7, R> fn) {
		return new IFn7(fn);
	}

	public static <P1, P2, P3, P4, P5, P6, P7, P8, R> IFn fn(Fn8<P1, P2, P3, P4, P5, P6, P7, P8, R> fn) {
		return new IFn8(fn);
	}

	public static <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> IFn fn(Fn9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> fn) {
		return new IFn9(fn);
	}

	public static IFn fn(VoidFn0 fn) {
		return new VoidIFn0(fn);
	}

	public static <P1> IFn fn(VoidFn1<P1> fn) {
		return new VoidIFn1(fn);
	}

	public static <P1, P2> IFn fn(VoidFn2<P1, P2> fn) {
		return new VoidIFn2(fn);
	}

	public static <P1, P2, P3> IFn fn(VoidFn3<P1, P2, P3> fn) {
		return new VoidIFn3(fn);
	}

	public static <P1, P2, P3, P4> IFn fn(VoidFn4<P1, P2, P3, P4> fn) {
		return new VoidIFn4(fn);
	}

	public static <P1, P2, P3, P4, P5> IFn fn(VoidFn5<P1, P2, P3, P4, P5> fn) {
		return new VoidIFn5(fn);
	}

	public static <P1, P2, P3, P4, P5, P6> IFn fn(VoidFn6<P1, P2, P3, P4, P5, P6> fn) {
		return new VoidIFn6(fn);
	}

	public static <P1, P2, P3, P4, P5, P6, P7> IFn fn(VoidFn7<P1, P2, P3, P4, P5, P6, P7> fn) {
		return new VoidIFn7(fn);
	}

	public static <P1, P2, P3, P4, P5, P6, P7, P8> IFn fn(VoidFn8<P1, P2, P3, P4, P5, P6, P7, P8> fn) {
		return new VoidIFn8(fn);
	}

	public static <P1, P2, P3, P4, P5, P6, P7, P8, P9> IFn fn(VoidFn9<P1, P2, P3, P4, P5, P6, P7, P8, P9> fn) {
		return new VoidIFn9(fn);
	}
}
