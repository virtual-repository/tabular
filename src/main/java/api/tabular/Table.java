package api.tabular;

import java.util.List;
import java.util.Properties;

import api.tabular.utils.Described;
import api.tabular.utils.Streamable;



/**
 * A table of {@link Column}s and {@link Row}s, with optional {@link Properties}.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Table extends Streamable<Row>, Described<Table> {

	/**
	 * Returns the columns of this table.
	 * 
	 * @return the columns
	 */
	List<Column> columns();
	
	/**
	 * Returns a table which can be iterated over multiple times.
	 * <p>
	 * It may return this very table if it is already materialised.
	 * @return the materialised table.
	 */
	Table materialise();
		
	
}
