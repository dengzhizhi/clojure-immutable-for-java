package net.ci4j.immutable.object;

import static net.ci4j.immutable.ClojureRT.ASSOC;
import static net.ci4j.immutable.ClojureRT.ASSOC_IN;
import static net.ci4j.immutable.ClojureRT.DISSOC;
import static net.ci4j.immutable.ClojureRT.DROP_LAST;
import static net.ci4j.immutable.ClojureRT.UPDATE_IN;

import java.util.Objects;

import net.ci4j.immutable.ClojureJson;
import net.ci4j.immutable.ClojureRT;
import net.ci4j.immutable.collections.ImmutableCollection;
import net.ci4j.immutable.collections.ImmutableList;
import net.ci4j.immutable.collections.ImmutableMap;
import net.ci4j.immutable.collections.ImmutableSet;
import clojure.lang.APersistentMap;
import clojure.lang.APersistentSet;
import clojure.lang.ArraySeq;
import clojure.lang.IPersistentCollection;
import clojure.lang.IPersistentVector;
import clojure.lang.Keyword;
import clojure.lang.PersistentArrayMap;

public class ImmutableObject implements ImmutableCollection<IPersistentCollection>
{
	private IPersistentCollection state;

	private Keyword specKey;

	public final static ImmutableObject EMPTY = new ImmutableObject();

	/**
	 * **Slow** Do not use this method in loops.
	 * @param key
	 * @param spec
	 * @return
	 */
	public ImmutableObject attachSpec(String key, String spec)
	{
		final Keyword intern = Keyword.intern("rasp.spec", key);
		ClojureRT.evalIn("rasp.spec","(s/def ::" + key + " " + spec + ")");
		return new ImmutableObject(this.state, intern);
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

	public ImmutableObject confirmSpec()
	{
		if (!checkValid()) throw new SpecViolationException(explainSpec());
		return this;
	}

	public ImmutableObject(IPersistentCollection state)
	{
		this.state = state;
	}

	public ImmutableObject(IPersistentCollection state, Keyword specKey)
	{
		this.state = state;
		this.specKey = specKey;
	}

	private ImmutableObject()
	{
		this.state = PersistentArrayMap.EMPTY;
	}

	public ImmutableObject assoc(Object key, Object value)
	{
		return new ImmutableObject((IPersistentCollection) ASSOC.invoke(state, key, value), specKey);
	}

	public ImmutableObject assocIn(Object value, Object... path)
	{
		return new ImmutableObject((IPersistentCollection) ASSOC_IN.invoke(state, ArraySeq.create(path), value), specKey);
	}

	public ImmutableObject dissoc(Object key)
	{
		return new ImmutableObject((IPersistentCollection) DISSOC.invoke(state, key), specKey);
	}

	public ImmutableObject dissocIn(Object... path)
	{
		final Object finalKey = path[path.length - 1];
		return new ImmutableObject((IPersistentCollection) UPDATE_IN.invoke(state, DROP_LAST.invoke(ArraySeq.create(path)), DISSOC, finalKey),
			specKey);
	}

	public <T> T getIn(T defaultValue, Object... path)
	{
		return smartGet(ClojureRT.GET_IN.invoke(state, ArraySeq.create(path), defaultValue));
	}

	public <T> T optIn(Object... path)
	{
		return smartGet(ClojureRT.GET_IN.invoke(state, ArraySeq.create(path)));
	}

	public ImmutableObject optInAsImmutableObject(Object... path)
	{
		final Object value = ClojureRT.GET_IN.invoke(state, ArraySeq.create(path));
		return value != null ? new ImmutableObject((IPersistentCollection) value) : null;
	}

	public ImmutableObject getInAsImmutableObject(Object... path)
	{
		final Object value = ClojureRT.GET_IN.invoke(state, ArraySeq.create(path));
		return value != null ? new ImmutableObject((IPersistentCollection) value) : EMPTY;
	}

	@SuppressWarnings("unchecked")
	public <T> T getInRaw(Object... path)
	{
		return (T) ClojureRT.GET_IN.invoke(state, ArraySeq.create(path));
	}

	public <T> T get()
	{
		return smartGet(this.state);
	}

	private <T> T smartGet(Object obj)
	{
		if (obj instanceof IPersistentVector)
		{
			return (T) new ImmutableList((IPersistentVector) obj);
		}
		else if (obj instanceof APersistentSet)
		{
			return (T) new ImmutableSet((APersistentSet) obj);
		}
		else if (obj instanceof APersistentMap)
		{
			return (T) new ImmutableMap((APersistentMap) obj);
		}
		else
			return (T) obj;
	}

	@Override
	public IPersistentCollection getRaw()
	{
		return state;
	}

	@Override
	public String toString()
	{
		return String.valueOf(state);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableObject that = (ImmutableObject) o;
		return Objects.equals(state, that.state);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(state);
	}

	public ImmutableObject resetFromJson(String json)
	{
		final ImmutableObject clone = ClojureJson.parse(json);
		clone.specKey = this.specKey;
		clone.confirmSpec();
		return clone;
	}

	public static ImmutableObject fromJson(String json)
	{
		return ClojureJson.parse(json);
	}

	/**
	 * **Slow** Do not use this method in loops.
	 * @param json
	 * @param specKey
	 * @param spec
	 * @return
	 */
	public static ImmutableObject fromJson(String json, String specKey, String spec)
	{
		final ImmutableObject obj = ClojureJson.parse(json).attachSpec(specKey, spec);
		obj.confirmSpec();
		return obj;
	}

	/**
	 * Parse from a edn string.
	 * 
	 * @param str
	 * @return
	 */
	public static ImmutableObject fromString(String str)
	{
		return new ImmutableObject(ClojureRT.readString(str));
	}

	/**
	 * **Slow** Do not use this method in loops.
	 * @param str
	 * @param specKey
	 * @param spec
	 * @return
	 */
	public static ImmutableObject fromString(String str, String specKey, String spec)
	{
		final ImmutableObject immutableObject = new ImmutableObject(ClojureRT.readString(str)).attachSpec(specKey, spec);
		immutableObject.confirmSpec();
		return immutableObject;
	}

	public ImmutableObject resetFromString(String str)
	{
		final ImmutableObject obj = new ImmutableObject(ClojureRT.readString(str), this.specKey);
		obj.confirmSpec();
		return obj;
	}

	public String toJson()
	{
		return ClojureJson.generateFromEDN(this);
	}

	public ImmutableObject empty() {
		final ImmutableObject obj = new ImmutableObject();
		obj.specKey = this.specKey;
		return obj;
	}
}
