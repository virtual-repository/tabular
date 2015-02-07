package api.tabular.utils;


import static api.tabular.Properties.*;
import api.tabular.Properties;
import api.tabular.Property;

public interface Described<SELF> {

	Properties properties = props();
	
	default Properties properties() {
		return properties;
	}
	
	//reusable convenience method to increase fluency of implementors
	default SELF add(Property prop) {
		
		properties.add(prop);
		
		//strategically unsafe: relies on implementors to pass their own type
		@SuppressWarnings("unchecked")
		SELF self = (SELF) this;
		
		return self;
	}
	
}
