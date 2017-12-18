package net.ci4j.immutable.collections;

import clojure.lang.APersistentVector;
import clojure.lang.IPersistentVector;
import clojure.lang.ITransientVector;
import clojure.lang.PersistentVector;
import net.ci4j.immutable.ClojureRT;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Immutable list backing by clojure vector
 *
 * @author Zhizhi Deng
 */
@SuppressWarnings("unchecked")
public class ImmutableList<E> implements List<E>, ImmutableCollection<IPersistentVector>
{
	private IPersistentVector vector;

	public final static ImmutableList EMPTY = new ImmutableList(PersistentVector.EMPTY);

	public ImmutableList(IPersistentVector vector)
	{
		this.vector = vector != null ? vector : PersistentVector.EMPTY;
	}

	public static <T> ImmutableList<T> refEmpty() {
		return EMPTY;
	}

	public static <T> ImmutableList<T> create(T... items) {
		if (items == null || items.length == 0) {
			return EMPTY;
		} else {
			return new ImmutableList<>(PersistentVector.create((Object[]) items));
		}
	}

	public static <T> ImmutableList<T> create(List<T> items) {
		if (items == null || items.isEmpty()) {
			return EMPTY;
		} else {
			return new ImmutableList<>(PersistentVector.create(items));
		}
	}

	public static <T> ImmutableList<T> create(Iterable<T> items) {
		final PersistentVector vector = PersistentVector.create(items);
		return vector.isEmpty() ? EMPTY : new ImmutableList<>(vector);
	}

	public static <T> ImmutableList<T> fromString(String ednString)
	{
		if (ednString == null) return EMPTY;
		final IPersistentVector vector = ClojureRT.readString(ednString);
		if (vector.length() == 0) {
			return EMPTY;
		} else {
			return new ImmutableList<>(vector);
		}
	}

	@Override
	public int size()
	{
		return vector.length();
	}

	@Override
	public boolean isEmpty()
	{
		return vector.count() == 0;
	}

	@Override
	public boolean contains(Object o)
	{
		return vector.containsKey(o);
	}

	private APersistentVector castToAPersistentVector() {
		if (vector instanceof APersistentVector) {
			return (APersistentVector) vector;
		} else {
			throw new UnsupportedOperationException("Not supported for vector type: " + vector.getClass().getName());
		}
	}

	private PersistentVector castToPersistentVector() {
		if (vector instanceof PersistentVector) {
			return (PersistentVector) vector;
		} else {
			throw new UnsupportedOperationException("Not supported for vector type: " + vector.getClass().getName());
		}
	}

	@Override
	public Iterator<E> iterator()
	{
		return castToAPersistentVector().iterator();
	}

	@Override
	public Object[] toArray()
	{
		return castToAPersistentVector().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return (T[]) castToAPersistentVector().toArray(a);
	}

	@Override
	@Deprecated
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
		return castToAPersistentVector().containsAll(c);
	}

	@Deprecated
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Use .consAll() instead.");
	}

	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnsupportedOperationException("Use .consAll() instead.");
	}

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Use dissocN() or filterOut() instead.");
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
		throw new UnsupportedOperationException("Use createEmpty() instead.");
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableList<?> that = (ImmutableList<?>) o;
		return Objects.equals(vector, that.vector);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(vector);
	}

	@Override
	public E get(int index)
	{
		return (E) vector.nth(index);
	}

	@Override
	@Deprecated
	public E set(int index, E element)
	{
		throw new UnsupportedOperationException("Use .assocN() instead.");
	}

	@Override
	@Deprecated
	public void add(int index, E element)
	{
		throw new UnsupportedOperationException("Use .insert() instead.");
	}

	@Override
	@Deprecated
	public E remove(int index)
	{
		throw new UnsupportedOperationException("Use .filterOut() instead.");
	}

	@Override
	public int indexOf(Object o)
	{
		return castToAPersistentVector().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return castToAPersistentVector().lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return castToAPersistentVector().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index)
	{
		return castToAPersistentVector().listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return castToAPersistentVector().subList(fromIndex, toIndex);
	}

	public ImmutableList<E> cons(E item) {
		return new ImmutableList<>(vector.cons(item));
	}

	public ImmutableList<E> consAll(E... items) {
		final ITransientVector trans = castToPersistentVector().asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		return new ImmutableList<>((IPersistentVector) trans.persistent());
	}

	public ImmutableList<E> consAll(Collection<E> items) {
		final ITransientVector trans = castToPersistentVector().asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		return new ImmutableList<>((IPersistentVector) trans.persistent());
	}

	public ImmutableList<E> filterOut(Predicate<E> filter) {
		final ITransientVector transientVector = PersistentVector.EMPTY.asTransient();
		for (Object item : castToAPersistentVector())
		{
			if (!filter.test((E) item)) {
				transientVector.conj(item);
			}
		}
		return new ImmutableList<>((IPersistentVector) transientVector.persistent());
	}

	public ImmutableList<E> filter(Predicate<E> filter) {
		final ITransientVector transientVector = PersistentVector.EMPTY.asTransient();
		for (Object item : castToAPersistentVector())
		{
			if (filter.test((E) item)) {
				transientVector.conj(item);
			}
		}
		return new ImmutableList<>((IPersistentVector) transientVector.persistent());
	}

	@Override
	public Stream<E> stream() {
		return castToAPersistentVector().stream();
	}

	@Override
	public Stream<E> parallelStream()
	{
		return castToAPersistentVector().parallelStream();
	}

	@Override
	public void forEach(Consumer<? super E> action)
	{
		castToAPersistentVector().forEach(action);
	}

	@Override
	public String toString()
	{
		return "ImmutableList{" + "vector=" + vector + '}';
	}

	public ImmutableList<E> assocN(int i, E val) {
		return new ImmutableList<>(vector.assocN(i, val));
	}

	public ImmutableList<E> dissocN(int i) {
		return new ImmutableList<>((IPersistentVector) ClojureRT.REMOVE_NTH_IN_VEC.invoke(vector, i));
	}

	public ImmutableList<E> insert(int i, E val) {
		return new ImmutableList<>((IPersistentVector) ClojureRT.INSERT_NTH_IN_VEC.invoke(vector, i, val));
	}

	@Override
	public IPersistentVector getRaw()
	{
		return vector;
	}

}
