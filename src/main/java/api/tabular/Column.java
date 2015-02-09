package api.tabular;

import static api.tabular.Properties.*;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import api.tabular.utils.Described;

/**
 * A column of a {@link Table}.
 * 
 */
@RequiredArgsConstructor
@Data
public class Column implements Described<Column> {
	
	@NonNull
	private final String name;
	
	private final Properties properties = props();

}
