package smallgears.api.tabular.impl;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import smallgears.api.tabular.Column;
import smallgears.api.tabular.Row;
import smallgears.api.tabular.Table;
import smallgears.api.tabular.utils.MaterialisedTable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * A {@link Table} that materialises its elements and can be iterated over multiple times.
 * 
 */
@ToString(callSuper=true) //can actually print value as table is materialised
@EqualsAndHashCode(callSuper=true)
public class SimpleTable extends AbstractTable implements MaterialisedTable {

	private final List<Row> rows = new ArrayList<Row>();

	public SimpleTable(List<Column> columns, @NonNull Iterable<Row> rows) {
		
		super(columns);
		
		rows.forEach(r->this.rows.add(r));
	}
	
	/**
	 * Materialises a given table.
	 */
	public SimpleTable(Table table) {
		
		this(table.columns(),table.stream().collect(toList()));
	}
	
	
	@Override
	public Iterator<Row> iterator() {
		return rows.iterator();
	}
	
	@Override
	public MaterialisedTable add(Row ... rows) {
		
		return add(asList(rows));
	}
	
	@Override
	public MaterialisedTable add(Iterable<Row> rows) {
		
		rows.forEach(r->this.rows.add(r));
		
		return this;
	}
	
	@Override
	public MaterialisedTable remove(Iterable<Row> rows) {
		
		rows.forEach(r->this.rows.remove(r));
		
		return this;
	}
	
	@Override
	public MaterialisedTable remove(Row... rows) {
		
		return remove(asList(rows));
	}
	

	@Override
	public int size() {
		return rows.size();
	}
	
}
