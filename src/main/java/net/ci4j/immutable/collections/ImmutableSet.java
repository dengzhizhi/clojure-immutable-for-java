/**
 * Copyright (c) 2017 Zhizhi Deng
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.ci4j.immutable.collections;

import clojure.lang.APersistentSet;
import clojure.lang.IEditableCollection;
import clojure.lang.ITransientCollection;
import clojure.lang.PersistentHashSet;
import net.ci4j.immutable.clojure_utils.ClojureRT;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A typed immutable set backing by clojure set
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

	public static <T> ImmutableSet<T> refEmpty()
	{
		return EMPTY;
	}

	/**
	 * Obtain an ImmutableSet instance of the given items
	 *
	 * @param items in the result immutable set
	 * @return An ImmutableSet instance
	 */
	public static <T> ImmutableSet<T> create(T... items)
	{
		if (items == null || items.length == 0)
		{
			return EMPTY;
		}
		else
		{
			return new ImmutableSet<>(PersistentHashSet.create((Object[]) items));
		}
	}

	/**
	 * Obtain an ImmutableSet instance of the given items
	 *
	 * @param items in the result immutable set
	 * @return An ImmutableSet instance
	 */
	public static <T> ImmutableSet<T> create(List<T> items)
	{
		if (items == null || items.isEmpty())
		{
			return EMPTY;
		}
		else
		{
			return new ImmutableSet<>(PersistentHashSet.create(items));
		}
	}

	/**
	 * Obtain an ImmutableSet instance of the given items
	 *
	 * @param items in the result immutable set
	 * @return An ImmutableSet instance
	 */
	public static <T> ImmutableSet<T> create(Iterable<T> items)
	{
		return new ImmutableSet<>(PersistentHashSet.create(items));
	}

	/**
	 * Obtain an ImmutableSet object represented by the EDN (https://github.com/edn-format/edn) string.
	 *
	 * @param ednString The edn format string representing a clojure set
	 * @return An ImmutableSet instance
	 * @throws ClassCastException if the ednString does not represent a valid clojure vector
	 */
	public static <T> ImmutableSet<T> fromString(String ednString)
	{
		return ednString != null ? new ImmutableSet(ClojureRT.readString(ednString)) : EMPTY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size()
	{
		return aSet.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty()
	{
		return aSet.count() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o)
	{
		return aSet.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator()
	{
		return aSet.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray()
	{
		return aSet.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		return (T[]) aSet.toArray(a);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableSet<?> that = (ImmutableSet<?>) o;
		return Objects.equals(aSet, that.aSet);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(aSet);
	}

	/**
	 * Create a new ImmutableSet with the given item added to the original set.
	 * If the given item is already existing in the original set, it should return the
	 * original set instance.
	 *
	 * @param item The item to be added
	 * @return a ImmutableSet contains all items in the original set and the given item.
	 */
	public ImmutableSet<E> cons(E item)
	{
		final APersistentSet cons = (APersistentSet) aSet.cons(item);
		return aSet != cons
			? new ImmutableSet<>(cons)
			: this;
	}

	/**
	 * Create a new ImmutableSet with all the given items added to the original set.
	 * If all the given items are already existing in the original set, it should return the
	 * original set instance.
	 *
	 * @param items The items to be added
	 * @return a ImmutableSet contains all items in the original set and the given items.
	 */
	public ImmutableSet<E> consAll(E... items)
	{
		final ITransientCollection trans = ((IEditableCollection) aSet).asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		final APersistentSet newSet = (APersistentSet) trans.persistent();
		return aSet.size() != newSet.size()
			? new ImmutableSet<>(newSet)
			: this;
	}

	public ImmutableSet<E> consAll(Collection<E> items)
	{
		final ITransientCollection trans = ((IEditableCollection) this.aSet).asTransient();
		for (E item : items)
		{
			trans.conj(item);
		}
		final APersistentSet newSet = (APersistentSet) trans.persistent();
		return aSet.size() != newSet.size()
			? new ImmutableSet<>(newSet)
			: this;
	}

	/**
	 * Create a new ImmutableSet WITHOUT the items matches the filter.
	 * <p>
	 * This operation is NOT lazy. A new ImmutableSet result will be fully created on every
	 * invocation, unless there is no item to be filtered out, in this case the original
	 * ImmutableSet instance shall be returned.
	 * <p>
	 * For better performance in chained collection transformation, consider using {@link List#stream()} instead.
	 *
	 * @param filter A lambda to select the items to be excluded.
	 * @return A new ImmutableSet without the items match the filter. If no item has been filtered out, the original set instance shall be returned.
	 */
	public ImmutableSet<E> filterOut(Predicate<E> filter)
	{
		final ITransientCollection trans = ((IEditableCollection) this.aSet).asTransient();
		for (Object item : aSet)
		{
			if (!filter.test((E) item))
			{
				trans.conj(item);
			}
		}
		final APersistentSet newSet = (APersistentSet) trans.persistent();
		return newSet.size() != aSet.size()
			? new ImmutableSet<>(newSet)
			: this;
	}

	/**
	 * Create a new ImmutableSet contains only the items match the filter.
	 * <p>
	 * This operation is NOT lazy. A new ImmutableSet result will be fully created on every
	 * invocation, unless all items are included in the result, in this case the original
	 * ImmutableSet instance shall be returned.
	 * </p>
	 * <p>
	 * For better performance in chained collection transformation, consider using {@link List#stream()} instead.
	 * </p>
	 * @param filter A lambda to select the items to be included.
	 * @return A new ImmutableSet contains the items match the filter. If no item has been filtered out, the original list instance shall be
	 * returned.
	 */
	public ImmutableSet<E> filter(Predicate<E> filter)
	{
		final ITransientCollection trans = ((IEditableCollection) this.aSet).asTransient();
		for (Object item : aSet)
		{
			if (filter.test((E) item))
			{
				trans.conj(item);
			}
		}
		final APersistentSet newSet = (APersistentSet) trans.persistent();
		return newSet.size() != aSet.size()
			? new ImmutableSet<>(newSet)
			: this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<E> stream()
	{
		return aSet.stream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<E> parallelStream()
	{
		return aSet.parallelStream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Consumer<? super E> action)
	{
		aSet.forEach(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ImmutableSet{" + "set=" + aSet + '}';
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public APersistentSet getRaw()
	{
		return aSet;
	}

}
