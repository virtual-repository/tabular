package api.tabular;

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
	
	public static final String generic_kind = "generic";

	@NonNull
	private final String name;
	private String kind = generic_kind;
	private Class<?> type = String.class;

}
