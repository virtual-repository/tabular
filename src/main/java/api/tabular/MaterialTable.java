package api.tabular;

import static java.util.stream.Collectors.*;

import java.util.Iterator;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * A {@link Table} that materialises its elements and can be iterated over multiple times.
 * 
 */
@ToString(callSuper=true) //can actually print value as table is materialised
@EqualsAndHashCode(callSuper=true)
public class MaterialTable extends AbstractTable implements Table {

	private final Iterable<Row> rows;

	public MaterialTable(List<Column> columns, @NonNull Iterable<Row> rows) {
		
		super(columns);
		
		this.rows= rows;
	}
	
	/**
	 * Materialises a given table.
	 */
	public MaterialTable(Table table) {
		
		super(table.columns());
		
		this.rows = table.stream().collect(toList());
	}
	
	
	@Override
	public Iterator<Row> iterator() {
		return rows.iterator();
	}
	
	@Override
	public Table materialise() {
		return this;  //materialised by definition
	}
	

	
}
