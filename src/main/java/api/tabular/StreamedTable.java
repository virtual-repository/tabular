package api.tabular;

import java.util.Iterator;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Streamed {@link Table} implementation.
 * 
 */
@ToString(callSuper=true, exclude="rows") //can actually print value as table is materialised
@EqualsAndHashCode(callSuper=true, exclude="rows")
public class StreamedTable extends AbstractTable {

	private final Iterator<Row> rows;

	public StreamedTable(List<Column> columns, @NonNull Iterable<Row> rows) {
		
		//if we delegate to other constructor here, we couldnt check for null
		
		super(columns);
		
		this.rows= rows.iterator();
	}
	
	public StreamedTable(List<Column> columns, @NonNull Iterator<Row> rows) {
		
		super(columns);
		
		this.rows= rows;
	}
	
	@Override
	public Iterator<Row> iterator() {
		return rows;
	}
	
	
	

}
