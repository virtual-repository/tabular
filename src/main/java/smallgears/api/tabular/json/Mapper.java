package smallgears.api.tabular.json;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import smallgears.api.properties.Properties;
import smallgears.api.properties.Property;
import smallgears.api.tabular.Column;
import smallgears.api.tabular.Csv;

import com.fasterxml.jackson.databind.ObjectMapper;

@UtilityClass
public class Mapper {

	public static ObjectMapper mapper = new ObjectMapper();
	
	static {
		
		mapper.addMixInAnnotations(Csv.class,Mixins.Csv.class);
		mapper.addMixInAnnotations(Column.class,Mixins.Column.class);
		mapper.addMixInAnnotations(Properties.class,Mixins.Properties.class);
		mapper.addMixInAnnotations(Property.class,Mixins.Property.class);
		
	}
	
	@SneakyThrows
	public static String jsonOf(Csv csv) {
		return mapper.writeValueAsString(csv);
	}
	
	@SneakyThrows
	public static Csv csvOf(String json) {
		return mapper.readValue(json, Csv.class);
	}
	
	
}
