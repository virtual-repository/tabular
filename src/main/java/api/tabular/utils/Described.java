package api.tabular.utils;


import api.tabular.Properties;
import api.tabular.Property;

public interface Described<SELF> {

	Properties properties();
	
	//reusable convenience method to increase fluency of implementors
	default SELF add(Property ... props) {
		
		properties().add(props);
		
		//strategically unsafe: relies on implementors to pass their own type
		@SuppressWarnings("unchecked")
		SELF self = (SELF) this;
		
		return self;
	}
	
}
