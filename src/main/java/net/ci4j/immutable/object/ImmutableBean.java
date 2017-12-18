package net.ci4j.immutable.object;

import static net.ci4j.immutable.object.ImmutableBeanAssocStrategy.TO_ENUM_NAME;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.ci4j.immutable.ClojureJson;
import net.ci4j.immutable.ClojureRT;
import net.ci4j.immutable.collections.ImmutableCollection;
import net.ci4j.immutable.collections.ImmutableList;
import net.ci4j.immutable.collections.ImmutableMap;
import net.ci4j.immutable.collections.ImmutableSet;
import clojure.lang.APersistentMap;
import clojure.lang.APersistentSet;
import clojure.lang.ATransientMap;
import clojure.lang.ArraySeq;
import clojure.lang.IEditableCollection;
import clojure.lang.IPersistentVector;
import clojure.lang.Keyword;
import clojure.lang.PersistentArrayMap;
import clojure.lang.PersistentHashMap;

/**
 * @author Zhizhi Deng
 */
public abstract class ImmutableBean<CONCRETE extends ImmutableBean> implements Serializable, ImmutableCollection<APersistentMap>
{
	private static final long serialVersionUID = 1L;

	ImmutableBeanAssocStrategy mutablityStrategy;

	Keyword specKey;

	protected abstract Class<CONCRETE> getConcreteClass();

	public abstract CONCRETE refEmpty();

	protected ImmutableBean()
	{}

	@SuppressWarnings("unchecked")
	public static <T extends ImmutableBean> T create(Class<T> clazz)
	{
		try
		{
			final T instance = clazz.newInstance();
			instance.mutablityStrategy = new ImmutableAssoc();
			return instance;

		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException("Failed to instantiate ImmutableBean " + clazz.getName(), e);
		}
	};

	public static <T extends ImmutableBean> T createTransient(Class<T> clazz)
	{
		try
		{
			final T instance = clazz.newInstance();
			instance.mutablityStrategy = new TransientAssoc(PersistentHashMap.EMPTY.asTransient());
			return instance;

		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException("Failed to instantiate mutable copy of ImmutableBean " + clazz.getName(), e);
		}
	};

	static <T extends ImmutableBean> T createTransient(Class<T> clazz, ImmutableBean<T> origin)
	{
		try
		{
			final T instance = clazz.newInstance();
			instance.mutablityStrategy = new TransientAssoc(((ATransientMap) ((IEditableCollection) origin.getRaw()).asTransient()));
			return instance;

		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException("Failed to instantiate mutable copy of ImmutableBean " + clazz.getName(), e);
		}
	};

	protected void resetInternalMap(APersistentMap internalMap)
	{
		this.mutablityStrategy.resetInternalMap(internalMap);
	}

	protected void resetSpecKey(Keyword key)
	{
		this.specKey = key;
	}

	public static <T extends ImmutableBean> T create(Class<T> clazz, APersistentMap state)
	{
		final T newBean = create(clazz);
		newBean.resetInternalMap(state);
		return newBean;
	}

	public static <T extends ImmutableBean> T create(Class<T> clazz, APersistentMap state, Keyword specKey)
	{
		final T newBean = create(clazz);
		newBean.resetInternalMap(state);
		newBean.resetSpecKey(specKey);
		return newBean;
	}

	public static <T extends ImmutableBean> T create(Class<T> clazz, APersistentMap state, String specKey, String spec)
	{
		final T newBean = create(clazz);
		newBean.resetInternalMap(state);
		ClojureRT.evalIn("rasp.spec", "(s/def ::" + specKey + " " + spec + ")");
		final Keyword intern = Keyword.intern("rasp.spec", specKey);
		newBean.resetSpecKey(intern);
		return newBean;
	}

	public static <C extends ImmutableBean> C fromJson(Class<C> clazz, String json)
	{
		return create(clazz, (APersistentMap) ClojureJson.PARSE_STRING.invoke(json));
	}

	/**
	 * **Slow** Do not use this method in loops.
	 * 
	 * @param json
	 * @param specKey
	 * @param spec
	 * @return
	 */
	public static <T extends ImmutableBean> T fromJson(Class<T> clazz, String json, String specKey, String spec)
	{
		T newBean = create(clazz, (APersistentMap) ClojureJson.PARSE_STRING.invoke(json), specKey, spec);
		newBean.confirmSpec();
		return newBean;
	}

	public CONCRETE resetFromJson(String json)
	{
		final CONCRETE clone = this.withState((APersistentMap) ClojureJson.PARSE_STRING.invoke(json));
		clone.confirmSpec();
		return clone;
	}


	/**
	 * Parse from a edn string.
	 *
	 * @param edn
	 * @return
	 */
	public static <T extends ImmutableBean> T fromString(Class<T> clazz, String edn)
	{
		return create(clazz, ClojureRT.readString(edn));
	}

	/**
	 * Parse from a edn string.
	 * **Slow** Do not use this method in loops.
	 * 
	 * @param edn
	 * @param specKey
	 * @param spec
	 * @return
	 */
	public static <T extends ImmutableBean> T fromString(Class<T> clazz, String edn, String specKey, String spec)
	{
		final T newBean = create(clazz, ClojureRT.readString(edn), specKey, spec);
		newBean.confirmSpec();
		return newBean;
	}

	public CONCRETE resetFromString(String str)
	{
		final CONCRETE obj = withState(ClojureRT.readString(str));
		obj.confirmSpec();
		return obj;
	}

	public CONCRETE mergeString(String str)
	{
		final APersistentMap from = ClojureRT.readString(str);
		return mergeState(from);
	}

	public CONCRETE mergeString(String str, String... more)
	{
		final Object target = this.getRaw();
		final APersistentMap from = ClojureRT.readString(str);
		final Object[] froms = Stream.concat(Stream.of(target, from), Stream.of(more).map(x -> ((APersistentMap) ClojureRT.readString(x)))).toArray();

		final APersistentMap mergedState = (APersistentMap) ClojureRT.MERGE.applyTo(ClojureRT.pathSeq(froms));
		return withState(mergedState);
	}

	public CONCRETE mergeJson(String json)
	{
		final APersistentMap from = (APersistentMap) ClojureJson.PARSE_STRING.invoke(json);
		return mergeState(from);
	}

	public CONCRETE mergeJson(String str, String... more)
	{
		final APersistentMap target = this.getRaw();
		final APersistentMap from = (APersistentMap) ClojureJson.PARSE_STRING.invoke(str);
		final Object[] froms = Stream.concat(Stream.of(target, from), Stream.of(more).map(x -> ((APersistentMap) ClojureJson.PARSE_STRING.invoke(x))))
			.toArray();

		final APersistentMap mergedState = (APersistentMap) ClojureRT.MERGE.applyTo(ClojureRT.pathSeq(froms));
		return withState(mergedState);
	}

	private CONCRETE mergeState(APersistentMap from)
	{
		final APersistentMap target = this.getRaw();
		final APersistentMap merged = (APersistentMap) ClojureRT.MERGE.invoke(target, from);
		return withState(merged);
	}

	public String toJson()
	{
		return (String) ClojureJson.GENERATE_STRING.invoke(this.mutablityStrategy.getRaw());
	}

	public CONCRETE withEmpty()
	{
		return this.withState(PersistentArrayMap.EMPTY);
	}

	/**
	 * Define and attach a clojure spec to the current bean
	 * **Slow** Do not use this method in loops.
	 * 
	 * @param specKey
	 * @param spec
	 * @return
	 */
	public <T extends ImmutableBean> T attachSpec(String specKey, String spec)
	{
		ClojureRT.evalIn("rasp.spec", "(s/def ::" + specKey + " " + spec + ")");
		return attachSpec(specKey);
	}

	/**
	 * Attach a predefined clojure spec to the current bean
	 * @param specKey
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ImmutableBean> T attachSpec(String specKey)
	{
		final Keyword intern = Keyword.intern("rasp.spec", specKey);
		final T newBean = (T) create(this.getClass(), this.mutablityStrategy.getPersistRaw());
		newBean.resetSpecKey(intern);
		return newBean;
	}

	public boolean checkValid()
	{
		return specKey == null || ClojureRT.specValid(specKey, this.getRaw());
	}

	public String explainSpec()
	{
		if (specKey != null)
		{
			return ClojureRT.specExplain(specKey, this.getRaw());
		}
		else
		{
			return "No spec defined.";
		}
	}

	@SuppressWarnings("unchecked")
	public CONCRETE withState(APersistentMap state)
	{
		return this.mutablityStrategy.withState(this, state);
	}

	public CONCRETE asTransient()
	{
		if (this.mutablityStrategy.isPersist()) {
			final CONCRETE aTransient = createTransient(getConcreteClass(), this);
			return aTransient;
		} else {
			return (CONCRETE) this;
		}
	}

	public CONCRETE withTransientMode(Consumer<CONCRETE> statement) {
		final CONCRETE transientBean = this.asTransient();
		statement.accept(transientBean);
		return (CONCRETE) transientBean.asImmutable();
	}

	public CONCRETE asImmutable()
	{
		if (this.mutablityStrategy.isPersist())
		{
			return (CONCRETE) this;
		}
		else
		{
			return withState(this.mutablityStrategy.getPersistRaw());
		}
	}

	@SuppressWarnings("unchecked")
	public CONCRETE confirmSpec()
	{
		if (!checkValid())
		{
			throw new SpecViolationException(explainSpec());
		}
		return (CONCRETE) this;
	}

	public CONCRETE assoc(Object key, Object value)
	{
		return mutablityStrategy.assoc(this, key, value);
	}

	public CONCRETE assocWithEnum(Enum key, Object value)
	{
		return mutablityStrategy.assocWithEnum(this, key, value);
	}

	public CONCRETE assocIn(Object value, Object... path)
	{
		return mutablityStrategy.assocIn(this, value, path);
	}

	public CONCRETE assocInWithEnum(Object value, Enum... path)
	{
		return mutablityStrategy.assocInWithEnum(this, value, path);
	}

	public CONCRETE dissoc(Object key)
	{
		return mutablityStrategy.assocInWithEnum(this, key);
	}

	public CONCRETE dissocWithEnum(Enum key)
	{
		return mutablityStrategy.dissocWithEnum(this, key);
	}

	public CONCRETE dissocIn(Object... path)
	{
		return mutablityStrategy.dissocIn(this, path);
	}

	public CONCRETE dissocInWithEnum(Enum... path)
	{
		return mutablityStrategy.dissocInWithEnum(this, path);
	}

	@SuppressWarnings("unchecked")
	public <T> T valAt(Object key)
	{
		return (T) this.mutablityStrategy.getLookupRaw().valAt(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T valAtEnum(Enum key)
	{
		return (T) this.mutablityStrategy.getLookupRaw().valAt(key.name());
	}

	@SuppressWarnings("unchecked")
	public <T> T valIn(T defaultValue, Object... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutablityStrategy.getRaw(), ArraySeq.create(path), defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T valInEnum(T defaultValue, Enum... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutablityStrategy.getRaw(), ClojureRT.MAP.invoke(TO_ENUM_NAME, ArraySeq.create((Object[]) path)),
			defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T optIn(Object... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutablityStrategy.getRaw(), ArraySeq.create(path));
	}

	@SuppressWarnings("unchecked")
	public <T> T optInEnum(Enum... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutablityStrategy.getRaw(), ClojureRT.MAP.invoke(TO_ENUM_NAME, ArraySeq.create((Object[]) path)));
	}

	public <T extends ImmutableBean> T optInAsImmutableBean(Class<T> beanClass, Object... path)
	{
		return create(beanClass, optIn(path));
	}

	public <T extends ImmutableBean> T valAtAsImmutableBean(Class<T> beanClass, Object key)
	{
		return create(beanClass, valAt(key));
	}

	public <E> ImmutableList<E> valAtAsImmutableList(Object key)
	{
		return new ImmutableList<>(valAt(key));
	}

	public <E> ImmutableSet<E> valAtAsImmutableSet(Object key)
	{
		return new ImmutableSet<>(valAt(key));
	}

	public <K, V> ImmutableMap<K, V> valAtAsImmutableMap(Object key)
	{
		return new ImmutableMap<>(valAt(key));
	}

	public <E> ImmutableList<E> optInAsImmutableList(Object... path)
	{
		final IPersistentVector vector = optIn(path);
		return (vector != null) ? new ImmutableList<>(vector) : null;
	}

	public <E> ImmutableSet<E> optInAsImmutableSet(Object... path)
	{
		final APersistentSet set = optIn(path);
		return (set != null) ? new ImmutableSet<>(set) : null;
	}

	public <K, V> ImmutableMap<K, V> optInAsImmutableMap(Object... path)
	{
		final APersistentMap map = optIn(path);
		return map != null ? new ImmutableMap<>(map) : null;
	}

	public boolean valImmutable()
	{
		return this.mutablityStrategy.isPersist();
	}

	@Override
	public APersistentMap getRaw()
	{
		return this.mutablityStrategy.getPersistRaw();
	}

	@Override
	public String toString()
	{
		return String.valueOf(this.mutablityStrategy.getRaw());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableBean that = (ImmutableBean) o;
		return Objects.equals(this.mutablityStrategy.getRaw(), that.mutablityStrategy.getRaw());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.mutablityStrategy.getRaw());
	}
}
