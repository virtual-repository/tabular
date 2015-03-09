package smallgears.api.tabular;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static smallgears.api.tabular.utils.TableUtils.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import smallgears.api.tabular.utils.Streamable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
	
	/**
	 * Copy constructor
	 */
	public Row(Row row) {
		this();
		merge(row);
	}
	
	@Override
	public Iterator<String> iterator() {
		return data.values().iterator();
	}
	
	/**
	 * Applies a function to each (column,value) pair.
	 */
	public void forEach(BiConsumer<String,String> consumer) {
		
		data.entrySet().forEach(e->consumer.accept(e.getKey(), e.getValue()));
		
	}

	/**
	 * Returns <code>true</code> if this row contains the given columns.
	 */
	public boolean has(Iterable<Column> cols) {
		return has(streamof(cols).map(Column::name).collect(toList()));
	}
	
	/**
	 * Returns <code>true</code> if this row contains the given columns.
	 */
	public boolean has(Collection<String> cols) {
		return cols.stream().allMatch(data::containsKey);
	}
	
	/**
	 * Returns <code>true</code> if this row contains the given columns.
	 */
	public boolean has(Column ... cols) {
		return has(asList(cols));
	}
	
	/**
	 * Returns <code>true</code> if this row contains the given columns.
	 */
	public boolean has(String ... cols) {
		return has(asList(cols));
	}
	
	/**
	 * Returns the value of a given column in this row, if any.
	 */
	public String get(String name) {
		return data.get(name);
	}
	
	
	/**
	 * Returns the value of a given column in this row, if any.
	 */
	public String get(Column col) {
		return get(col.name());
	}
	
	/**
	 * Returns the value of a given column in this row, if any. Otherwise return a fallback value.
	 */
	public String getOr(Column col,String fallbackValue) {
		return getOr(col.name(),fallbackValue);
	}
	
	/**
	 * Returns the value of a given column in this row, if any. Otherwise returns a fallback value.
	 */
	public String getOr(String col,String fallbackValue) {
		return has(col)?get(col):fallbackValue;
	}
	
	/**
	 * Returns all the columns in this row.
	 */
	public Set<String> columns() {
		return new HashSet<>(data.keySet());
	}

	/**
	 * Adds a value of a given column to this row.
	 */
	public Row set(Column column, Object value) {
		data.put(column.name(), value.toString());
		return this;
	}
	
	/**
	 * Adds a value of a given column to this row.
	 */
	public Row set(String column, Object value) {
		data.put(column, value.toString());
		return this;
	}
	
	/**
	 * Adds the values of given rows to this row.
	 * <p>
	 * Rows are processed in order, thus so does overwriting. 
	 */
	public Row merge(Row ... rows) {
		return merge(asList(rows));
	}
	
	/**
	 * Adds the values of given rows to this row.
	 * <p>
	 * Rows are processed in order, thus so does overwriting. 
	 */
	public Row merge(Iterable<Row> rows) {
		streamof(rows).forEach(r->data.putAll(r.data));
		return this;
	}

	/**
	 * Removes given columns from this row.
	 */
	public Row remove(Column ... columns) {
		return remove(Arrays.asList(columns));
	}
	
	/**
	 * Removes given columns from this row.
	 */
	public Row remove(Iterable<Column> columns) {
		return remove(streamof(columns).map(Column::name).collect(toList()));
	}
	
	/**
	 * Removes given columns from this row.
	 */
	public Row remove(String ... columns) {
		return remove(Arrays.asList(columns));
	}
	
	/**
	 * Removes given columns from this row.
	 */
	public Row remove(Collection<String> columns) {
		
		for (String col : columns)
			data.remove(col);
		
		return this;
	}
	
	/**
	 * Removes the columns of another row from this row.
	 */
	public Row remove(Row row) {
		return remove(data.keySet());
	}
	
	
	/**
	 * Returns a new row with some of the columns in this row.
	 */
	public Row extract(Column ... columns) {
		return extract(Arrays.asList(columns));
	}
	
	/**
	 * Returns a new row with some of the columns in this row.
	 */
	public Row extract(Iterable<Column> columns) {
		return extract(streamof(columns).map(Column::name).collect(toList()));
	}
	
	/**
	 * Returns a new row with some of the columns in this row.
	 */
	public Row extract(String ... columns) {
		return extract(Arrays.asList(columns));
	}
	
	/**
	 * Returns a new row with some of the columns in this row.
	 */
	public Row extract(Collection<String> columns) {
		
		Row row = new Row();
		
		for (String col : columns)
			if (has(col))
				row.set(col, get(col));
		
		return row;
	}
	
	/**
	 * Returns the number of columns in this row.
	 */
	public int size() {
		return data.size();
	}
	
}
