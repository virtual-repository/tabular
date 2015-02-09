package api.tabular;

import java.util.Iterator;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import api.tabular.utils.Streamable;

/**
 * A row of a {@link Table}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Row implements Streamable<String> {

	@NonNull
	private Map<String,String> data;
	
	@Override
	public Iterator<String> iterator() {
		return data.values().iterator();
	}

	public String get(String name) {
		return data.get(name);
	}
	
	public String get(Column column) {
		return get(column.name());
	}
}
