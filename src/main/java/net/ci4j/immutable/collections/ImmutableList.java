/**
 * Copyright (c) Zhizhi Deng. All rights reserved.
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 **/

package net.ci4j.immutable.collections;

import clojure.lang.APersistentVector;
import clojure.lang.IPersistentVector;
import clojure.lang.ISeq;
import clojure.lang.ITransientVector;
import clojure.lang.PersistentVector;
import net.ci4j.fn.Fn2;
import net.ci4j.immutable.clojure_utils.ClojureJson;
import net.ci4j.immutable.clojure_utils.ClojureRT;
import net.ci4j.immutable.clojure_utils.JsonParseException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.ci4j.immutable.fn.Fn.fn;

/**
 * A typed immutable list backing by clojure vector.
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

	public static <T> ImmutableList<T> refEmpty()
	{
		return EMPTY;
	}

	/**
	 * Obtain an ImmutableList instance of the given items
	 *
	 * @param items in the result immutable list
	 * @return An ImmutableList instance
	 */
	public static <T> ImmutableList<T> create(T... items)
	{
		if (items == null || items.length == 0)
		{
			return EMPTY;
		}
		else
		{
			return new ImmutableList<>(PersistentVector.create((Object[]) items));
		}
	}

	/**
	 * Obtain an ImmutableList instance of the given items
	 *
	 * @param items in the result immutable list
	 * @return An ImmutableList instance
	 */
	public static <T> ImmutableList<T> create(List<T> items)
	{
		if (items == null || items.isEmpty())
		{
			return EMPTY;
		}
		else if (items instanceof IPersistentVector)
		{
			return new ImmutableList<>(((IPersistentVector) items));
		}
		else
		{
			return new ImmutableList<>(PersistentVector.create(items));
		}
	}

	/**
	 * Obtain an ImmutableList instance of the given items
	 *
	 * @param items in the result immutable list
	 * @return An ImmutableList instance
	 */
	public static <T> ImmutableList<T> create(Iterable<T> items)
	{
		final PersistentVector vector = PersistentVector.create(items);
		return vector.isEmpty() ? EMPTY : new ImmutableList<>(vector);
	}

	/**
	 * Obtain an ImmutableList object represented by the EDN (https://github.com/edn-format/edn) string.
	 *
	 * @param ednString The edn format string representing a clojure vector
	 * @return An ImmutableList instance
	 * @throws ClassCastException if the ednString does not represent a valid clojure vector
	 */
	public static <T> ImmutableList<T> fromString(String ednString)
	{
		if (ednString == null) return EMPTY;
		final IPersistentVector vector = ClojureRT.readString(ednString);
		if (vector.length() == 0)
		{
			return EMPTY;
		}
		else
		{
			return new ImmutableList<>(vector);
		}
	}

	/**
	 * Obtain an ImmutableList object represented by the JSON string.
	 *
	 * @param jsonString The json format string representing a javascript array
	 * @return An ImmutableList instance
	 * @throws JsonParseException if the jsonString is not a valid json string
	 * @throws ClassCastException if the jsonString does not represent a js array
	 */
	public static <T> ImmutableList<T> fromJSON(String jsonString)
	{
		if (jsonString == null) return EMPTY;
		try
		{
			final Object parsed = ClojureJson.PARSE_STRING.invoke(jsonString);
			APersistentVector vector = parsed instanceof ISeq ? ((APersistentVector) ClojureRT.VEC.invoke(parsed)) : (APersistentVector) parsed;
			if (vector.length() == 0)
			{
				return EMPTY;
			}
			else
			{
				return new ImmutableList<>(vector);
			}
		} catch (Exception e)
		{
			//I have to do it this way because Clojure does not declare checked exception on its API
			if (e instanceof com.fasterxml.jackson.core.JsonParseException)
			{
				throw new JsonParseException(e);
			}
			else
			{
				throw e;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size()
	{
		return vector.length();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty()
	{
		return vector.count() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o)
	{
		return vector.containsKey(o);
	}

	private APersistentVector castToAPersistentVector()
	{
		if (vector instanceof APersistentVector)
		{
			return (APersistentVector) vector;
		}
		else
		{
			throw new UnsupportedOperationException("Not supported for vector type: " + vector.getClass().getName());
		}
	}

	private PersistentVector castToPersistentVector()
	{
		if (vector instanceof PersistentVector)
		{
			return (PersistentVector) vector;
		}
		else
		{
			throw new UnsupportedOperationException("Not supported for vector type: " + vector.getClass().getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator()
	{
		return castToAPersistentVector().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray()
	{
		return castToAPersistentVector().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
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
	@Deprecated
	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("Use .filterOut instead");
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableList<?> that = (ImmutableList<?>) o;
		return Objects.equals(vector, that.vector);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(vector);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(Object o)
	{
		return castToAPersistentVector().indexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object o)
	{
		return castToAPersistentVector().lastIndexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return castToAPersistentVector().listIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return castToAPersistentVector().listIterator(index);
	}

	/**
	 * Similar to normal {@link List#subList(int, int)} but returns the original instance when the range covers the full list.
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex   high endpoint (exclusive) of the subList
	 * @return a view of the specified range within this list
	 * @throws IndexOutOfBoundsException for an illegal endpoint index value
	 *                                   (<tt>fromIndex &lt; 0 || toIndex &gt; size ||
	 *                                   fromIndex &gt; toIndex</tt>)
	 * @see {@link List#subList(int, int)}
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		if (fromIndex < 1 && toIndex >= this.size()) return this;
		final APersistentVector newState = (APersistentVector) castToAPersistentVector().subList(fromIndex, toIndex);
		return new ImmutableList<>(newState);
	}

	/**
	 * Create a new Immutable list with the given item appended at the end of the original list
	 *
	 * @param item The item to be appended
	 * @return a new Immutable list with the given item appended at the end of the original list
	 */
	public ImmutableList<E> cons(E item)
	{
		return new ImmutableList<>(vector.cons(item));
	}

	/**
	 * Create a new Immutable list with the given items appended at the end of the original list
	 *
	 * @param items The items to be appended
	 * @return a new Immutable list with the given item appended at the end of the original list
	 */
	public ImmutableList<E> consAll(E... items)
	{
		final ITransientVector trans = castToPersistentVector().asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		return new ImmutableList<>((IPersistentVector) trans.persistent());
	}

	/**
	 * Create a new Immutable list with the given items appended at the end of the original list
	 *
	 * @param items The items to be appended
	 * @return a new Immutable list with the given item appended at the end of the original list
	 */
	public ImmutableList<E> consAll(Collection<E> items)
	{
		final ITransientVector trans = castToPersistentVector().asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		return new ImmutableList<>((IPersistentVector) trans.persistent());
	}

	/**
	 * Create a new ImmutableList WITHOUT the items matches the filter.
	 * <p>
	 * This operation is NOT lazy. A new ImmutableList result will be fully created on every
	 * invocation, unless there is no item to be filtered out, in this case the original
	 * ImmutableList instance shall be returned.
	 * <p>
	 * For better performance in chained collection transformation, consider using {@link List#stream()} instead.
	 *
	 * @param filter A lambda to select the items to be excluded.
	 * @return A new ImmutableList without the items match the filter. If no item has been filtered out, the original list instance shall be
	 * returned.
	 */
	public ImmutableList<E> filterOut(Predicate<E> filter)
	{
		final ITransientVector transientVector = PersistentVector.EMPTY.asTransient();
		for (Object item : castToAPersistentVector())
		{
			if (!filter.test((E) item))
			{
				transientVector.conj(item);
			}
		}

		return getRaw().length() != transientVector.count() ? new ImmutableList<>((IPersistentVector) transientVector.persistent()) : this;
	}

	/**
	 * Create a new ImmutableList contains only the items match the filter.
	 * <p>
	 * This operation is NOT lazy. A new ImmutableList result will be fully created on every
	 * invocation, unless all items are included in the result, in this case the original
	 * ImmutableList instance shall be returned.
	 * <p>
	 * For better performance in chained collection transformation, consider using {@link List#stream()} instead.
	 *
	 * @param filter A lambda to select the items to be included.
	 * @return A new ImmutableList contains the items match the filter. If no item has been filtered out, the original list instance shall be
	 * returned.
	 */
	public ImmutableList<E> filter(Predicate<E> filter)
	{
		final ITransientVector transientVector = PersistentVector.EMPTY.asTransient();
		for (Object item : castToAPersistentVector())
		{
			if (filter.test((E) item))
			{
				transientVector.conj(item);
			}
		}
		return getRaw().length() != transientVector.count() ? new ImmutableList<>((IPersistentVector) transientVector.persistent()) : this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<E> stream()
	{
		return castToAPersistentVector().stream();
	}

	/**
	 * {@inheritDoc}
	 */
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

	public ImmutableList<E> assocN(int i, E val)
	{
		return new ImmutableList<>(vector.assocN(i, val));
	}

	public ImmutableList<E> dissocN(int i)
	{
		return new ImmutableList<>((IPersistentVector) ClojureRT.REMOVE_NTH_IN_VEC.invoke(vector, i));
	}

	public ImmutableList<E> insert(int i, E val)
	{
		return new ImmutableList<>((IPersistentVector) ClojureRT.INSERT_NTH_IN_VEC.invoke(vector, i, val));
	}

	@Override
	public IPersistentVector getRaw()
	{
		return vector;
	}

	public <T> T reduce(T initialValue, Fn2<T, E, T> reducer)
	{
		T result = (T) ClojureRT.REDUCE.invoke(fn(reducer), initialValue, getRaw());
		return result;
	}
}
