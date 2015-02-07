package api.tabular;

import java.util.Iterator;


public class Dsl {

	public static interface NameClause {
		
		/**
		 * Adds a column to the row.
		 */
		ValueClause col(String name);
		
		/**
		 * Adds a column to the row.
		 */
		ValueClause col(Column col);
		
		/**
		 * Marks the end of the row.
		 */
		Row end();
	}
	
	@FunctionalInterface
	public static interface ValueClause {
		
		/**
		 * Sets the column's value.
		 */
		NameClause is(String value);
	}
	
	
	public static interface ColClause {
		
		/**
		 * Adds one or more columns to the table.
		 */
		ColClause cols(Column ... cols);
		
		/**
		 * Adds one or more columns to the table.
		 */
		ColClause cols(String ... cols);
		
		/**
		 * Adds columns to the table.
		 */
		ColClause cols(Iterable<Column> cols);
		
		/**
		 * Adds one or more rows to the table.
		 */
		Table rows(Row ... rows);
		
		/**
		 * Adds rows to the table.
		 */
		Table rows(Iterable<Row> rows);
		
		/**
		 * Adds rows to the table.
		 */
		Table rows(Iterator<Row> rows);
		
		/**
		 * Adds a row to the table.
		 */
		RowClause row(String ... vals);
		
	}
	
	public static interface RowClause {
		
		/**
		 * Adds a row to the table.
		 */
		RowClause row(String ... vals);
		
		/**
		 * Marks the end of the table.
		 * @return
		 */
		Table end();
		
	}
}
