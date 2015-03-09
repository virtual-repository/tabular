package smallgears.api.tabular;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import smallgears.api.properties.Properties;

/**
 * A column of a {@link Table}, with optional {@link Properties}.
 */
@RequiredArgsConstructor
@Data
public class Column {
	
	@NonNull
	private final String name;
	
	private final Properties properties = Properties.props();

}
