package api.tabular;

import static api.tabular.Properties.*;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A column of a {@link Table}, with optional {@link Properties}.
 */
@RequiredArgsConstructor
@Data
public class Column {
	
	@NonNull
	private final String name;
	
	private final Properties properties = props();

}
