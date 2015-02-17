package api.tabular;

import static java.nio.file.Files.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Iterator;

import lombok.Cleanup;
import lombok.SneakyThrows;


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
		SourceClause from(Csv csv);
		

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
	
	public static interface SourceClause {
		
		/**
		 * Provides a stream with the CSV data.
		 */
		Table in(InputStream stream);
		
		/**
		 * Provides a String with the CSV data.
		 */
		default Table in(String data) {
			return in(new ByteArrayInputStream(data.getBytes()));
		}
		
		/**
		 * Provides a String with the CSV data file.
		 */
		@SneakyThrows
		default Table at(Path file) {
				
			if (!isReadable(file) || isDirectory(file))
				throw new IllegalArgumentException(file+" is unreadable or a directory.");
			
			return in(new FileInputStream(file.toFile()));
			
		}
		
		
	}
	
	public static interface SinkClause {
		
		/**
		 * Sets a given stream as the sink.
		 * <p>
		 * Clients are responsible for closing the stream.
		 */
		void to(OutputStream stream);
		
		/**
		 * Sets a given file as the sink.
		 */
		@SneakyThrows
		default void at(Path file) {
				
			if (isDirectory(file) || (exists(file) && !isWritable(file)))
				throw new IllegalArgumentException(file+" is unwritable or a directory.");
			
			@Cleanup
			OutputStream stream = newOutputStream(file);
		
			to(stream);
			
		}
		
		
	}
	
	
}
