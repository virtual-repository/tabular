package api.tabular;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import api.tabular.utils.Streamable;

/**
 * A collection of uniquely named {@link Properties}.
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
	 * Creates with some initial properties.
	 */
	public static Properties props(@NonNull Property ... properties) {
		return props().add(properties);
	}
	
	public Properties add(@NonNull Property ... props) {
		
		Arrays.asList(props).stream().forEach($->{
			properties.put($.name(),$);
		});

		
		return this;
	}

	public boolean has(@NonNull Properties props) {
		
		return props.stream().allMatch(this::has);
	}
	
	public boolean has(@NonNull String name) {
		
		return this.properties.containsKey(name);
	}
	
	public boolean has(@NonNull Property p) {
		
		return this.properties.containsValue(p);
	}

	/**
	 * Removes a given property.
	 * 
	 * @param name the name of the property
	 * 
	 * @throws IllegalStateException if a property with the given name does not exist in this collection
	 */
	public void remove(@NonNull String name) {

		if (this.properties.remove(name) == null)
			throw new IllegalStateException("unknown property " + name);
	}

	/**
	 * Returns a given property in this collection.
	 * 
	 * @param name the name of the property
	 * @return the property
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
	 * Returns <code>true</code> if this collection has no properties.
	 * 
	 * @return <code>true</code> if this collection has no properties
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
	
	/**
	 * Returns this properties as an array.
	 * @return the properties
	 */
	public Property[] toArray() {
		return properties.values().toArray(new Property[properties.size()]);
	}

	@Override
	public String toString() {
		
		Collection<?> copy = null;
		
		synchronized(properties) {
			copy = new ArrayList<>(properties.values());
		}
		
		return toString(copy,100);
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	
}
