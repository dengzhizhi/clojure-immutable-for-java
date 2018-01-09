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

import clojure.lang.APersistentMap;
import clojure.lang.IEditableCollection;
import clojure.lang.IMapEntry;
import clojure.lang.ISeq;
import clojure.lang.ITransientMap;
import clojure.lang.PersistentArrayMap;
import clojure.lang.PersistentHashMap;
import net.ci4j.immutable.clojure_utils.ClojureJson;
import net.ci4j.immutable.clojure_utils.ClojureRT;
import net.ci4j.immutable.clojure_utils.JsonParseException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A typed immutable map backing by clojure map
 *
 * @author Zhizhi Deng
 */
@SuppressWarnings("unchecked")
public class ImmutableMap<K, V> implements Map<K, V>, ImmutableCollection<APersistentMap>
{
	private APersistentMap map;

	public static final ImmutableMap EMPTY = new ImmutableMap(PersistentArrayMap.EMPTY);

	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> refEmpty()
	{
		return EMPTY;
	}

	public ImmutableMap(APersistentMap map)
	{
		this.map = map != null ? map : PersistentArrayMap.EMPTY;
	}

	/**
	 * Create a ImmutableMap with key-value pairs
	 *
	 * @param parts {key1, value1, key2, value2, ...}
	 * @param <K> key type
	 * @param <V> value type
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> create(Object... parts)
	{
		if (parts.length == 0)
		{
			return EMPTY;
		}
		if (parts.length < 16)
		{
			return (ImmutableMap<K, V>) new ImmutableMap<>(PersistentArrayMap.createWithCheck(parts));
		}
		else
		{
			return (ImmutableMap<K, V>) new ImmutableMap<>(PersistentHashMap.createWithCheck(parts));
		}
	}

	/**
	 * Create a ImmutableMap from a Map
	 *
	 * @param map A map of initial entries
	 * @param <K> key type
	 * @param <V> value type
	 * @return An ImmutableMap instance
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> create(Map<K, V> map)
	{
		if (map == null || map.size() == 0) {
			return EMPTY;
		}
		if (map.size() < 16)
		{
			return (ImmutableMap<K, V>) new ImmutableMap<>((APersistentMap) PersistentArrayMap.create(map));
		}
		else
		{
			return (ImmutableMap<K, V>) new ImmutableMap<>((APersistentMap) PersistentHashMap.create(map));
		}
	}

	/**
	 * Build an ImmutableMap by zipping keys and values
	 * @param keys {key1, key2, key3, ...}
	 * @param values {value1, value2, value3, ...}
	 * @param <K> key type
	 * @param <V> value type
	 * @return an ImmutableMap instance
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> zipCreate(Iterable<K> keys, Iterable<V> values)
	{
		final ITransientMap trans = PersistentArrayMap.EMPTY.asTransient();
		final Iterator<K> kIterator = keys.iterator();
		final Iterator<V> vIterator = values.iterator();
		while (kIterator.hasNext() && vIterator.hasNext())
		{
			trans.assoc(kIterator.next(), vIterator.next());
		}
		return new ImmutableMap<>((APersistentMap) trans.persistent());
	}

	/**
	 * Obtain an ImmutableMap object represented by the EDN (https://github.com/edn-format/edn) string.
	 *
	 * @param ednString The edn format string representing a clojure set
	 * @return An ImmutableMap instance
	 * @throws ClassCastException if the ednString does not represent a valid clojure vector
	 */
	public static <K, V> ImmutableMap<K, V> fromString(String ednString)
	{
		return ednString != null ? new ImmutableMap<>(ClojureRT.readString(ednString)) : EMPTY;
	}

	/**
	 * Obtain an ImmutableList object represented by the JSON string.
	 *
	 * @param jsonString The json format string representing a javascript array
	 * @return An ImmutableList instance
	 * @throws JsonParseException if the jsonString is not a valid json string
	 * @throws ClassCastException if the jsonString does not represent a js array
	 */
	public static <K, V> ImmutableMap<K, V> fromJson(String jsonString)
	{
		if (jsonString == null) {
			return EMPTY;
		}
		try {
			return jsonString != null
				? new ImmutableMap<>((APersistentMap) ClojureJson.PARSE_STRING.invoke(jsonString))
				: EMPTY;
		} catch (Exception e) {
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
		return this.map.count();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty()
	{
		return this.map.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key)
	{
		return this.map.containsKey(key);
	}

	/**
	 * Get the key-value pair by the given key.
	 * @param key key
	 * @return A {@link IMapEntry} object to access the key-value pair
	 */
	public IMapEntry entryAt(Object key)
	{
		return this.map.entryAt(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(Object value)
	{
		return this.map.containsValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(Object key)
	{
		return (V) this.map.get(key);
	}

	@Override
	@Deprecated
	public V put(K key, V value)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public V remove(Object key)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putAll(Map<? extends K, ? extends V> m)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<K> keySet()
	{
		return this.map.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<V> values()
	{
		return this.map.values();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return this.map.entrySet();
	}

	/**
	 * assoc[iate]. returns a new map of the same (hashed/sorted) type, that contains
	 * the mapping of key to val.
	 *
	 * @param key key
	 * @param val value
	 */
	public ImmutableMap<K, V> assoc(K key, V val)
	{
		final APersistentMap newMap = (APersistentMap) this.map.assoc(key, val);
		return this.map.size() != newMap.size()
			? new ImmutableMap<>(newMap)
			: this;
	}

	/**
	 * assoc[iate]. returns a new map of the same (hashed/sorted) type, that contains
	 * the mapping of key to val. Throws exception if the given key has already mapped.
	 *
	 * @param key key
	 * @param val value
	 */
	public ImmutableMap<K, V> assocEx(K key, V val)
	{
		return new ImmutableMap<>((APersistentMap) this.map.assocEx(key, val));
	}

	/**
	 * Return a new map without the given key mapping.
	 * @param key key
	 */
	public ImmutableMap<K, V> without(K key)
	{
		final APersistentMap without = (APersistentMap) this.map.without(key);
		return this.map != without
			? new ImmutableMap<>(without)
			: this;
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or {@code null} if this map contains no mapping for the key.
	 *
	 * @see {@link #get(Object)}
	 *
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or
	 *         {@code null} if this map contains no mapping for the key
	 */
	public Object valAt(K key)
	{
		return this.map.valAt(key);
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or a default value if this map contains no mapping for the key.
	 *
	 * @see {@link #get(Object)}
	 *
	 * @param key the key whose associated value is to be returned
	 * @param notFound the default value to be returned if the specified key not found.
	 * @return the value to which the specified key is mapped, or
	 *         a default value if this map contains no mapping for the key
	 */
	public Object valAt(K key, V notFound)
	{
		return this.map.valAt(key, notFound);
	}

	/**
	 * @see {@link #size()}
	 */
	public int count()
	{
		return this.map.count();
	}

	public ImmutableMap<K, V> cons(Object o)
	{
		return new ImmutableMap<>((APersistentMap) this.map.cons(o));
	}

	public boolean equiv(Object o)
	{
		return this.map.equiv(o);
	}

	public ISeq seq()
	{
		return this.map.seq();
	}

	public Iterator iterator()
	{
		return this.map.iterator();
	}

	/**
	 * Associate all entries from another Map
	 * @param other The map to be associated into the current ImmutableMap
	 *
	 * @return A new map contains entries from the original map and the other map
	 */
	public ImmutableMap<K, V> assocAll(Map<? extends K, ? extends V> other)
	{
		final ITransientMap trans = (ITransientMap) ((IEditableCollection) this.map).asTransient();
		other.forEach(trans::assoc);
		final APersistentMap newMap = (APersistentMap) trans.persistent();
		return newMap.size() != this.size()
			? new ImmutableMap<>(newMap)
			: this;
	}

	@Override
	public APersistentMap getRaw()
	{
		return map;
	}
}
