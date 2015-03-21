package smallgears.api.tabular.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Mixins {

	
	static class Csv {
		
		@JsonProperty char delimiter;
		@JsonProperty boolean hasHeader;
		@JsonProperty String encoding;
		@JsonProperty int rows;
		@JsonProperty char quote;
		@JsonProperty List<Column> columns;
	}
	
	static class Column {
		
		@JsonCreator
		Column(@JsonProperty("name") String name) {}
		
		@JsonProperty String name;
		
		@JsonProperty Properties properties;
	}
	
	static class Properties {
		
		@JsonProperty Map<String,Property> elements;
	}

	static class Property {
		
		@JsonCreator
		Property(@JsonProperty("name") String name) {}
		
		@JsonProperty String name;
		@JsonProperty Object value;
	}
}
