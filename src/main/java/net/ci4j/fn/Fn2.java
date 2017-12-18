package net.ci4j.fn;

@FunctionalInterface
public interface Fn2<P1, P2, R>
{
	R apply(P1 v1, P2 v2);
}
