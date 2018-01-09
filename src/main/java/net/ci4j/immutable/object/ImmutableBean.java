/**
 *   Copyright (c) Zhizhi Deng. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/
package net.ci4j.immutable.object;

import static net.ci4j.immutable.object.ImmutableBeanAssocStrategy.TO_ENUM_NAME;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.ci4j.immutable.clojure_utils.ClojureJson;
import net.ci4j.immutable.clojure_utils.ClojureRT;
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
 * <p> A abstract class to build immutable java beans. </p>
 *
 * <p> An immutable java bean should:
 * <ul>
 *     <li>Extend this abstract class and use itself as the CONCRETE type parameter</li>
 *     <li>Ensure there is a public non-parameter default constructor</li>
 *     <li>Implement the {@link #getConcreteClass()} method, return the current class type</li>
 *     <li>Have a public static final EMPTY field, invoke the {@link ImmutableBean#create(Class)} method to obtain a default empty instance, pass in the current concrete type as parameter.</li>
 *     <li>Implement the {@link #refEmpty()} method, return the EMPTY value defined above</li>
 *     <li>Define getter methods, invoke {@link #valAt(Object)} or {@link #valAtEnum(Enum)} internally to retrieve field value</li>
 *     <li>To avoid confusion, setter methods should prefix with {@code assoc}, invoke {@link #assoc(Object, Object)} or {@link #assocWithEnum(Enum, Object)} internally to store field value. Assoc methods should always return the new bean instance returned by the {@link #assoc(Object, Object)} method.</li>
 * </ul>
 * </p>
 * <p> When using a immutable java bean
 * <ul>
 *     <li>In most of the cases, starts with the EMPTY instance, avoid calling constructor directly. Static create methods should be only used when </li>
 *     <li>When associating lots of properties at the same time, use {@link #asTransient()} to obtain a mutable copy for modification then call {@link #asImmutable()} at the end to avoid creating lots of intermediate objects.</li>
 * </ul>
 * </p>
 *
 * <p>Example<br/>
 * <b>A concrete immutable bean</b>
 * <pre>
 *     public class Person extends ImmutableBean&lt;Person> {
 *
 *         public final static Person EMPTY = create(Person.class);
 *
 *         {@literal @}Override
 *         protected Class<Person> getConcreteClass() {
 *             return Person.class;
 *         }
 *
 *         {@literal @}Override
 *         public Person refEmpty() {
 *             return EMPTY;
 *         }
 *
 *         private enum Fields {
 *             name,
 *             age
 *         }
 *
 *         public String getName() {
 *             return valAtEnum(Fields.name);
 *         }
 *
 *         public Person assocName(String name) {
 *             return assocWithEnum(Fields.name, name);
 *         }
 *
 *         public Integer getAge() {
 *             return valAtEnum(Fields.age);
 *         }
 *
 *         public Person assocAge(Integer age) {
 *             return assocWithEnum(Fields.age, age);
 *         }
 *     }
 * </pre>
 *
 * <b>Using a immutable bean</b>
 * <pre>
 *     public class PersonManager {
 *         private final static Person DEFAULT_ADULT = Person.EMPTY.assocAge(18);  //You can have other defaults to start with
 *
 *         private Person currentPerson;
 *
 *         public void initPerson(String name, int age) {
 *             this.currentPerson = Person.EMPTY             //Use EMPTY as a seed
 *                                        .asTransient()     //Convert to transient (mutable) copy for multiple association
 *                                        .assocName(name)
 *                                        .assocAge(age)
 *                                        .asPersistent();   //Convert back to immutable object.
 *         }
 *
 *         public void initPerson(String name) {
 *             this.currentPerson = DEFAULT_ADULT.assocName(name);   //Use alternative default as a seed
 *         }
 *
 *         public Person getCurrentPerson() {
 *             return this.currentPerson;
 *         }
 *
 *         public void setCurrentPerson(Person person) {
 *             this.currentPerson = person;
 *         }
 *
 *         public void updateCurrentPerson(Function&lt;Person, Person> updater) {
 *             this.setCurrentPerson(updater.apply(this.getCurrentPerson()));  // A util update function to avoid calling getter/setter too often
 *         }
 *
 *         public void increaseAge(int years) {
 *             this.updateCurrentPerson(it -> it.assocAge(it.getAge() + years))
 *         }
 *     }
 * </pre>
 *
 * </p>
 *
 * @author Zhizhi Deng
 */
public abstract class ImmutableBean<CONCRETE extends ImmutableBean> implements Serializable, ImmutableCollection<APersistentMap>
{
	private static final long serialVersionUID = 1L;

	ImmutableBeanAssocStrategy mutabilityStrategy;

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
			instance.mutabilityStrategy = new ImmutableAssoc();
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
			instance.mutabilityStrategy = new TransientAssoc(PersistentHashMap.EMPTY.asTransient());
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
			instance.mutabilityStrategy = new TransientAssoc(((ATransientMap) ((IEditableCollection) origin.getRaw()).asTransient()));
			return instance;

		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException("Failed to instantiate mutable copy of ImmutableBean " + clazz.getName(), e);
		}
	};

	protected void resetInternalMap(APersistentMap internalMap)
	{
		this.mutabilityStrategy.resetInternalMap(internalMap);
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
		return (String) ClojureJson.GENERATE_STRING.invoke(this.mutabilityStrategy.getRaw());
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
		final T newBean = (T) create(this.getClass(), this.mutabilityStrategy.getPersistRaw());
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
		return this.mutabilityStrategy.withState(this, state);
	}

	public CONCRETE asTransient()
	{
		if (this.mutabilityStrategy.isPersist()) {
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
		if (this.mutabilityStrategy.isPersist())
		{
			return (CONCRETE) this;
		}
		else
		{
			return withState(this.mutabilityStrategy.getPersistRaw());
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
		return mutabilityStrategy.assoc(this, key, value);
	}

	public CONCRETE assocWithEnum(Enum key, Object value)
	{
		return mutabilityStrategy.assocWithEnum(this, key, value);
	}

	public CONCRETE assocIn(Object value, Object... path)
	{
		return mutabilityStrategy.assocIn(this, value, path);
	}

	public CONCRETE assocInWithEnum(Object value, Enum... path)
	{
		return mutabilityStrategy.assocInWithEnum(this, value, path);
	}

	public CONCRETE dissoc(Object key)
	{
		return mutabilityStrategy.assocInWithEnum(this, key);
	}

	public CONCRETE dissocWithEnum(Enum key)
	{
		return mutabilityStrategy.dissocWithEnum(this, key);
	}

	public CONCRETE dissocIn(Object... path)
	{
		return mutabilityStrategy.dissocIn(this, path);
	}

	public CONCRETE dissocInWithEnum(Enum... path)
	{
		return mutabilityStrategy.dissocInWithEnum(this, path);
	}

	@SuppressWarnings("unchecked")
	public <T> T valAt(Object key)
	{
		return (T) this.mutabilityStrategy.getLookupRaw().valAt(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T valAtEnum(Enum key)
	{
		return (T) this.mutabilityStrategy.getLookupRaw().valAt(key.name());
	}

	@SuppressWarnings("unchecked")
	public <T> T valIn(T defaultValue, Object... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutabilityStrategy.getRaw(), ArraySeq.create(path), defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T valInEnum(T defaultValue, Enum... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutabilityStrategy.getRaw(), ClojureRT.MAP.invoke(TO_ENUM_NAME, ArraySeq.create((Object[]) path)),
			defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T optIn(Object... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutabilityStrategy.getRaw(), ArraySeq.create(path));
	}

	@SuppressWarnings("unchecked")
	public <T> T optInEnum(Enum... path)
	{
		return (T) ClojureRT.GET_IN.invoke(this.mutabilityStrategy.getRaw(), ClojureRT.MAP.invoke(TO_ENUM_NAME, ArraySeq.create((Object[]) path)));
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
		return this.mutabilityStrategy.isPersist();
	}

	@Override
	public APersistentMap getRaw()
	{
		return this.mutabilityStrategy.getPersistRaw();
	}

	@Override
	public String toString()
	{
		return String.valueOf(this.mutabilityStrategy.getRaw());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableBean that = (ImmutableBean) o;
		return Objects.equals(this.mutabilityStrategy.getRaw(), that.mutabilityStrategy.getRaw());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.mutabilityStrategy.getRaw());
	}
}
