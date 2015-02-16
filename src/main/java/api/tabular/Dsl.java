package api.tabular;

import java.io.InputStream;
import java.nio.file.Path;
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
		 * Adds a column to the row.
		 */
		NameClause col(Column col,String val);

		/**
		 * Adds a column to the row.
		 */
		NameClause col(String col,String val);

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
	
	
	public static interface TableClause {

		/**
		 * Sets the columns and rows of the table.
		 * <p>
		 * The rows will be 
		 * 
		 * */
		Table with(String[] cols, String[] ... rows);
		
		/**
		 * Creates an instance from  a given {@link Csv} dataset.
		 * @see CsvTable#CsvTable(Csv, InputStream)
		 */
		StreamClause from(Csv csv);
		

		/**
		 * Adds one or more columns to the table.
		 */
		TableClause cols(Column ... cols);
		
		/**
		 * Adds one or more columns to the table.
		 */
		TableClause cols(String ... cols);
		
		/**
		 * Adds columns to the table.
		 */
		TableClause cols(Iterable<Column> cols);
		
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
		 * Adds rows to the table.
		 */
		Table rows(String[] ... rows);
		
		/**
		 * Adds a row to the table.
		 */
		RowClause row(String ... vals);
		
		/**
		 * Adds a row to the table.
		 */
		RowClause row(Iterable<String> vals);
		
	}
	
	public static interface RowClause {
		
		/**
		 * Adds a row to the table.
		 */
		RowClause row(String ... vals);
		
		/**
		 * Adds a row to the table.
		 */
		RowClause row(Iterable<String> vals);
		
		/**
		 * Marks the end of the table.
		 * @return
		 */
		Table end();
		
	}
	
	public static interface StreamClause {
		
		/**
		 * Provides a stream with the CSV data.
		 */
		Table in(InputStream stream);
		
		/**
		 * Provides a String with the CSV data.
		 */
		Table in(String data);
		
		/**
		 * Provides a String with the CSV data file.
		 */
		Table at(Path file);
		
		
	}
}
