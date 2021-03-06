package smallgears.api.tabular.utils;

import smallgears.api.tabular.Row;
import smallgears.api.tabular.Table;

/**
 * A {@link Row}-mutable {@link Table} that can be iterated over multiple times.
 */
public interface MaterialisedTable extends Table {

	/**
	 * Adds rows to this table.
	 */
	MaterialisedTable add(Row ... rows);

	/**
	 * Adds rows to this table.
	 */
	MaterialisedTable add(Iterable<Row> rows);

	/**
	 * Remove rows from this table.
	 */
	MaterialisedTable remove(Row ... rows);
	
	/**
	 * Remove rows from this table.
	 */
	MaterialisedTable remove(Iterable<Row> rows);
	
	/**
	 * Returns the number of rows in this table.
	 */
	int size();
}
