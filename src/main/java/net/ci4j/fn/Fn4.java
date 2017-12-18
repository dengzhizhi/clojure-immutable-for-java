package net.ci4j.fn;

@FunctionalInterface
public interface Fn4<P1, P2, P3, P4, R>
{
	R apply(P1 v1, P2 v2, P3 v3, P4 v4);
}
