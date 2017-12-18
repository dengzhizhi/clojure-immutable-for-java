package net.ci4j.immutable.collections;

import clojure.lang.APersistentMap;
import clojure.lang.IEditableCollection;
import clojure.lang.IMapEntry;
import clojure.lang.ISeq;
import clojure.lang.ITransientMap;
import clojure.lang.PersistentArrayMap;
import clojure.lang.PersistentHashMap;
import net.ci4j.immutable.ClojureRT;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Immutable map backing by clojure map
 *
 * @author Zhizhi Deng
 */
@SuppressWarnings("unchecked")
public class ImmutableMap<K, V> implements Map<K, V>, ImmutableCollection<APersistentMap>
{
	private APersistentMap map;

	public static final ImmutableMap EMPTY = new ImmutableMap(PersistentArrayMap.EMPTY);

	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> refEmpty()  {
		return EMPTY;
	}

	public ImmutableMap(APersistentMap map)
	{
		this.map = map != null ? map : PersistentArrayMap.EMPTY;
	}

	@SuppressWarnings("unchecked")
	public static <K,V> ImmutableMap<K, V> create(Object... parts) {
		if (parts.length < 16) {
			return (ImmutableMap<K, V>) new ImmutableMap<>(PersistentArrayMap.createWithCheck(parts)) ;
		} else {
			return (ImmutableMap<K, V>) new ImmutableMap<>(PersistentHashMap.createWithCheck(parts)) ;
		}
	}

	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> create(Map<K, V> map) {
		if (map.size() < 16) {
			return (ImmutableMap<K, V>) new ImmutableMap<>((APersistentMap) PersistentArrayMap.create(map));
		} else {
			return (ImmutableMap<K, V>) new ImmutableMap<>((APersistentMap) PersistentHashMap.create(map));
		}
	}

	@SuppressWarnings("unchecked")
	public static <K, V> ImmutableMap<K, V> zipCreate(Iterable<K> keys, Iterable<V> values) {
		final ITransientMap trans = PersistentArrayMap.EMPTY.asTransient();
		final Iterator<K> kIterator = keys.iterator();
		final Iterator<V> vIterator = values.iterator();
		while (kIterator.hasNext() && vIterator.hasNext()) {
			trans.assoc(kIterator.next(), vIterator.next());
		}
		return new ImmutableMap<>((APersistentMap) trans.persistent());
	}

	public static <K, V> ImmutableMap<K, V> fromString(String ednString) {
		return ednString != null ? new ImmutableMap<>(ClojureRT.readString(ednString))  : EMPTY;
	}

	@Override
	public int size()
	{
		return this.map.count();
	}

	@Override
	public boolean isEmpty()
	{
		return this.map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return this.map.containsKey(key);
	}

	public IMapEntry entryAt(Object key)
	{
		return null;
	}

	@Override
	public boolean containsValue(Object value)
	{
		return this.map.containsValue(value);
	}

	@Override
	public V get(Object key)
	{
		return (V) this.map.get(key);
	}

	@Override
	public V put(K key, V value)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet()
	{
		return this.map.keySet();
	}

	@Override
	public Collection<V> values()
	{
		return this.map.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return this.map.entrySet();
	}

	public ImmutableMap<K, V> assoc(K key, V val)
	{
		return new ImmutableMap<>((APersistentMap) this.map.assoc(key, val));
	}

	public ImmutableMap<K, V> assocEx(K key, V val)
	{
		return new ImmutableMap<>((APersistentMap) this.map.assocEx(key, val));
	}

	public ImmutableMap<K, V> without(K key)
	{
		return new ImmutableMap<>((APersistentMap) this.map.without(key)) ;
	}

	public Object valAt(K key)
	{
		return this.map.valAt(key);
	}

	public Object valAt(K key, V notFound)
	{
		return this.map.valAt(key, notFound);
	}

	public int count()
	{
		return this.map.count();
	}

	public ImmutableMap<K, V> cons(Object o)
	{
		return new ImmutableMap<>((APersistentMap) this.map.cons(o)) ;
	}

	public ImmutableMap<K, V> empty()
	{
		return EMPTY;
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

	public ImmutableMap<K, V> assocAll(Map<? extends K, ? extends V> other) {
		final ITransientMap trans = (ITransientMap) ((IEditableCollection) this.map).asTransient();
		other.forEach(trans::assoc);
		return new ImmutableMap<>((APersistentMap) trans.persistent());
	}

	@Override
	public APersistentMap getRaw()
	{
		return map;
	}
}
