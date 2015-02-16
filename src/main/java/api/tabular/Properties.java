package api.tabular;

import static api.tabular.TableUtils.*;
import static java.lang.String.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import api.tabular.utils.Streamable;

/**
 * A mutable group of uniquely named {@link Property}s.
 * 
 */
@NoArgsConstructor(staticName="props")
@EqualsAndHashCode
public class Properties implements Streamable<Property> {
	
	
	private final Map<String, Property> properties = synchronizedMap(new HashMap<String,Property>());

	@Override
	public Iterator<Property> iterator() {
		
		return properties.values().iterator();
	}
	
	/**
	 * Creates a group of initial properties.
	 */
	public static Properties props(@NonNull Property ... properties) {
		return props().add(properties);
	}
	
	/**
	 * Add given properties to this group.
	 */
	public Properties add(@NonNull Property ... props) {
		
		return add(asList(props));
	}
	
	
	/**
	 * Add given properties to this group.
	 */
	public Properties add(@NonNull Iterable<Property> props) {
		
		streamof(props).forEach($->{
			properties.put($.name(),$);
		});

		
		return this;
	}

	/**
	 * Returns <code>true</code> if there are given properties in this group.
	 */
	public boolean has(@NonNull Properties props) {
		
		return has((Iterable<Property>)props);
	}
	
	/**
	 * Returns <code>true</code> if there are given properties in this group.
	 */
	public boolean has(@NonNull String ... names) {
		
		return has(asList(names));
	}
	
	/**
	 * Returns <code>true</code> if there are given properties in this group.
	 */
	//very pragmatic choice, cannot overload iterable here, pick next best thing.
	public boolean has(@NonNull Collection<String> names) {
		
		return streamof(names).allMatch(this.properties::containsKey);
	}
	
	/**
	 * Returns <code>true</code> if there are given properties in this group.
	 */
	public boolean has(@NonNull Property ... ps) {
		
		return has(asList(ps));
	}
	
	/**
	 * Returns <code>true</code> if there are given properties in this group.
	 */
	public boolean has(@NonNull Iterable<Property> ps) {
		
		return streamof(ps).allMatch(this.properties::containsValue);
	}

	/**
	 * Removes given properties from this group, if they exist.
	 */
	public Properties remove(@NonNull String ... names) {

		return remove(asList(names));
	}
	
	/**
	 * Removes given properties from this group, if they exist.
	 */
	public Properties remove(@NonNull Collection<String> names) {

		names.forEach(this.properties::remove);
		
		return this;
	}
	
	/**
	 * Removes given properties from this group, if they exist.
	 */
	public Properties remove(@NonNull Iterable<Property> properties) {

		return remove(streamof(properties).map(Property::name).collect(toList()));
	}
	
	/**
	 * Removes given properties from this group, if they exist.
	 */
	public Properties remove(@NonNull Properties properties) {

		return remove((Iterable<Property>) properties);
	}
	
	
	/**
	 * Removes given properties from this group, if they exist.
	 */
	public Properties remove(@NonNull Property ... properties) {

		return remove(asList(properties));
	}
	

	/**
	 * Returns the value of a given property in this group.
	 *
	 * @throws IllegalStateException if a property with a given name does not exist in this collection
	 */
	public Property prop(@NonNull String name) {

		Property property = this.properties.get(name);

		if (property == null)
			throw new IllegalStateException("unknown property " + name);

		return property;

	}

	/**
	 * Returns the value of a given property in this group, if it exist. 
	 * Otherwise returns a property with a given fallback value.
	 *
	 */
	public Property prop(@NonNull String name, @NonNull Object fallbackValue) {

		return has(name) ? prop(name) : Property.prop(name,fallbackValue);

	}

	/**
	 * Returns <code>true</code> if this group has no properties.
	 */
	public boolean empty() {
		return properties.isEmpty();
	}

	/**
	 * Returns the number of properties in this collection
	 * 
	 * @return the number of properties in this collection
	 */
	public int size() {
		return properties.size();
	}

	@Override
	public String toString() {
		
		Function<Entry<String,Property>,String> tostring = $->format("%s=%s",$.getKey(),$.getValue().value());
		
		return format("[%s]", properties.entrySet().stream().map(tostring).collect(joining(",")));
	}
	
}
