package api.tabular;

import java.util.List;
import java.util.Properties;

import api.tabular.utils.MaterialisedTable;
import api.tabular.utils.Streamable;



/**
 * A mutable table of {@link Column}s and {@link Row}s of text, with optional {@link Properties}.
 * <p>
 * The table is semi-structured, i.e. does not enforce uniformity. 
 * Columns indicate row structure, but neither prescribe it nor necessarily fully describe it:
 * rows <em>may</em> have more or less columns than the table indicate.
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
		
	
}
