package api.tabular;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import api.tabular.utils.Streamable;

/**
 * A mutable row in a {@link Table}.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Row implements Streamable<String> {

	@NonNull
	private Map<String,String> data;
	
	public Row() {
		this(new HashMap<>());
	}
	
	@Override
	public Iterator<String> iterator() {
		return data.values().iterator();
	}

	/**
	 * Returns the value of a given column, if any.
	 */
	public String get(String name) {
		return data.get(name);
	}
	
	/**
	 * Returns all the columns.
	 */
	public Set<String> columns() {
		return new HashSet<>(data.keySet());
	}
	
	/**
	 * Adds a value of a given column.
	 */
	public Row set(String column, String value) {
		data.put(column, value);
		return this;
	}
	
	/**
	 * Adds the values of another row.
	 */
	public Row merge(Row row) {
		data.putAll(row.data);
		return this;
	}
	
	/**
	 * Removes columns.
	 */
	public Row remove(String ... columns) {
		return remove(Arrays.asList(columns));
	}
	
	/**
	 * Removes columns.
	 */
	public Row remove(Iterable<String> columns) {
		for (String col : columns)
			data.remove(col);
		return this;
	}
	
	/**
	 * Removes the values of another row.
	 */
	public Row remove(Row row) {
		return remove(data.keySet());
	}
	
}
