package net.ci4j.fn;

@FunctionalInterface
public interface Fn3<P1, P2, P3, R>
{
	R apply(P1 v1, P2 v2, P3 v3);
}
