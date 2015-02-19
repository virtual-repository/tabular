package api.tabular;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.function.UnaryOperator;

import api.tabular.impl.SimpleTable;
import api.tabular.impl.StreamedTable;
import api.tabular.utils.MaterialisedTable;
import api.tabular.utils.Streamable;




/**
 * A mutable table of {@link Column}s and {@link Row}s of text, with optional {@link Properties}.
 * <p>
 * The table is semi-structured, i.e. does not enforce uniformity. 
 * Columns indicate row structure, but neither prescribe it nor necessarily fully describe it:
 * rows <em>may</em> have more or less columns than the table indicates.
 * <p>
 * Depending on the implementation, not all rows may be in memory at the same time.
 * <am>Materialised</em> tables have this property and can be iterated over multiple times.
 * <em>Streamed</em> tables do not and can be iterated over only once.
 *<p>
 * All tables are mutable because their properties and rows are. Material tables can also acquire and lose rows. 
 * 
 * @see MaterialisedTable
 */
public interface Table extends Streamable<Row> {

	/**
	 * Returns the columns of this table.
	 */ 
	List<Column> columns();
	
	/**
	 * Returns an equivalent table which can be iterated over multiple times.
	 * <p>
	 * It may return this very table if it is already materialised.
	 */
	SimpleTable materialise();
	
	/**
	 * Returns <code>true</code> if this table is materialised.
	 */
	boolean materialised();
		
	
	/**
	 * Returns a streamed table obtained transforming the rows of this table. 
	 * <p>
	 * The table shares the individual columns with this table, in the object reference sense but collects
	 * them independently (adding or removing columns from one will not have an effect on the other).
	 */
	default Table with(UnaryOperator<Row> transform) {
	
		final Iterator<Row> current = iterator();
		
		return new StreamedTable(new ArrayList<>(columns()),new Iterator<Row>() {
			
			@Override
			public boolean hasNext() {
				return current.hasNext();
			}
			
			@Override
			public Row next() {
				return transform.apply(current.next());
			}
		});
	}
	
	
	/**
	 * Returns a streamed copy of this table. 
	 */
	default Table copy() {
		
		return with(Row::new);
		
	}
	
}
