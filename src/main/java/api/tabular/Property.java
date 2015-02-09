package api.tabular;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A named property with a mutable, typed value.
 */
@Data
@ToString(of={"name","value"})
@RequiredArgsConstructor(staticName="prop")
@AllArgsConstructor(staticName="prop")
public class Property {

	@NonNull
	private final String name;
	
	private Object value;
	
	/**
	 * Returns the value of this property under a given type.
	 * @return the value
	 * 
	 * @throws IllegalStateException if the value cannot be returned under the given type.
	 */
	public <S> S as(Class<S> type) {
		
		if (is(type))
			return type.cast(value());
		
		throw new IllegalStateException("property value "+value()+" of type "+value().getClass()+" cannot be typed as "+type.getCanonicalName());
		
	}
	
	/**
	 * Returns <code>true</code> if the value of this property has a given type.
	 * @param type the type
	 * @return <code>true</code> if the value of this property has a given type
	 */
	public boolean is(Class<?> type) {
		return type.isInstance(value());
	}
	
	
	

}
