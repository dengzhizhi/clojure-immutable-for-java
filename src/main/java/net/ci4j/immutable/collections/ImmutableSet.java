package net.ci4j.immutable.collections;

import net.ci4j.immutable.ClojureRT;
import clojure.lang.APersistentSet;
import clojure.lang.IEditableCollection;
import clojure.lang.ITransientCollection;
import clojure.lang.PersistentHashSet;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Immutable list backing by clojure vector
 *
 * @author Zhizhi Deng
 */
@SuppressWarnings("unchecked")
public class ImmutableSet<E> implements Set<E>, ImmutableCollection<APersistentSet>
{
	private APersistentSet aSet;

	public final static ImmutableSet EMPTY = new ImmutableSet(PersistentHashSet.EMPTY);

	public ImmutableSet(APersistentSet vector)
	{
		this.aSet = vector != null ? vector : PersistentHashSet.EMPTY;
	}

	public static <T> ImmutableSet<T> refEmpty() {
		return EMPTY;
	}

	public static <T> ImmutableSet<T> create(T... items) {
		if (items == null || items.length == 0) {
			return EMPTY;
		} else {
			return new ImmutableSet<>(PersistentHashSet.create((Object[]) items));
		}
	}

	public static <T> ImmutableSet<T> create(List<T> items) {
		if (items == null || items.isEmpty()) {
			return EMPTY;
		} else {
			return new ImmutableSet<>(PersistentHashSet.create(items));
		}
	}

	public static <T> ImmutableSet<T> create(Iterable<T> items) {
		return new ImmutableSet<>(PersistentHashSet.create(items));
	}

	public static <T> ImmutableSet<T> fromString(String ednString) {
		return ednString != null ? new ImmutableSet(ClojureRT.readString(ednString)) : EMPTY;
	}

	@Override
	public int size()
	{
		return aSet.size();
	}

	@Override
	public boolean isEmpty()
	{
		return aSet.count() == 0;
	}

	@Override
	public boolean contains(Object o)
	{
		return aSet.contains(o);
	}

	@Override
	public Iterator<E> iterator()
	{
		return aSet.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return aSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return (T[]) aSet.toArray(a);
	}

	@Override
	public boolean add(E e)
	{
		throw new UnsupportedOperationException("Use .cons instead");
	}

	@Override
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("Use .filterOut instead");
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return aSet.containsAll(c);
	}

	@Deprecated
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Use .consAll() instead.");
	}

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Use filterOut() instead.");
	}

	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Use filter() instead.");
	}

	@Override
	@Deprecated
	public void clear()
	{
		throw new UnsupportedOperationException("Use refEmpty() instead.");
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableSet<?> that = (ImmutableSet<?>) o;
		return Objects.equals(aSet, that.aSet);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(aSet);
	}

	public ImmutableSet<E> cons(E item) {
		return new ImmutableSet<>((APersistentSet) aSet.cons(item));
	}

	public ImmutableSet<E> consAll(E... items) {
		final ITransientCollection trans = ((IEditableCollection) aSet).asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		return new ImmutableSet<>((APersistentSet) trans.persistent());
	}

	public ImmutableSet<E> consAll(Collection<E> items) {
		final ITransientCollection trans = ((IEditableCollection) this.aSet).asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		return new ImmutableSet<>((APersistentSet) trans.persistent());
	}

	public ImmutableSet<E> filterOut(Predicate<E> filter) {
		final ITransientCollection trans = ((IEditableCollection) this.aSet).asTransient();
		for (Object item : aSet)
		{
			if (!filter.test((E) item)) {
				trans.conj(item);
			}
		}
		return new ImmutableSet<>((APersistentSet) trans.persistent());
	}

	public ImmutableSet<E> filter(Predicate<E> filter) {
		final ITransientCollection trans = ((IEditableCollection) this.aSet).asTransient();
		for (Object item : aSet)
		{
			if (filter.test((E) item)) {
				trans.conj(item);
			}
		}
		return new ImmutableSet<>((APersistentSet) trans.persistent());
	}

	@Override
	public Stream<E> stream() {
		return aSet.stream();
	}

	@Override
	public Stream<E> parallelStream()
	{
		return aSet.parallelStream();
	}

	@Override
	public void forEach(Consumer<? super E> action)
	{
		aSet.forEach(action);
	}

	@Override
	public String toString()
	{
		return "ImmutableSet{" + "vector=" + aSet + '}';
	}

	@Override
	public APersistentSet getRaw()
	{
		return aSet;
	}

}
