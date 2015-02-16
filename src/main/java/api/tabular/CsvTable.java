package api.tabular;

import static api.tabular.Tables.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import au.com.bytecode.opencsv.CSVReader;

/**
 * A {@link Table} backed up by an {@link InputStream} of CSV data.
 * 
 */
@Slf4j
public class CsvTable extends AbstractTable {


	private final Csv csv;

	private final RowIterator iterator;
	

	/**
	 * Creates an instance from  a given {@link Csv} dataset and its {@link InputStream}.
	 * 
	 * @throws IllegalArgumentException if the asset is inconsistently described
	 */
	public CsvTable(Csv csv, InputStream stream) {
		
		super(csv.columns());
		
		this.csv=csv;
		
		this.iterator = new RowIterator(stream);
	
	}
	
	
	@Override
	public Iterator<Row> iterator() {
		return iterator;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private class RowIterator implements Iterator<Row> {

		private final CSVReader reader;
		
		private String[] row;
		private Throwable error;
		private int count;
		
		public RowIterator(InputStream stream) {
			
			try {
				
				this.reader =  new CSVReader(new InputStreamReader(stream, csv.encoding()),csv.delimiter(),csv.quote());
				
				if (csv.hasHeader())
					parseHeader();
				
			}
			catch (Exception e) {
				throw new IllegalArgumentException("invalid csv asset: cannot read stream",e);
			}
		}
		
		public boolean hasNext() {

			if (row!=null)
				return true;
			
			if (csv.rows() <= count) {
				close();
				return false;
			}

			try {
				row = reader.readNext();
				
				count++;
				
			} catch (IOException e) {
				error = e;
			}

			return row != null;
		}

		public Row next() {
			
			try {
				checkRow();
			}
			catch(RuntimeException e) {
				close();
			}
			
			Row result = buildRow();
			
			row=null;
			
			return result;
		}
		
		private void checkRow() {

			if (error != null)
				throw new RuntimeException(error);

			if (row == null && !this.hasNext()) // reads ahead
				throw new NoSuchElementException();

		}

		// helper
		private Row buildRow() {

			Map<String, String> data = new HashMap<>();

			//synthesise missing columns from first row
			if (csv.columns().isEmpty())
				for (int i=0; i< row.length; i++)
					csv.with(col("column-"+i));
					
			for (int i = 0; i < csv.columns().size(); i++)
				if (i<row.length)
					data.put(columns.get(i).name(), row[i]);

			return new Row(data);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void close() {
			try {
				reader.close();
			} catch (Exception e) {
				log.warn("could not close CSV stream", e);
			}
		}
		
		
		
		//////////////////////////////////////////////////////////////////////////////
		
		@SneakyThrows
		private void parseHeader() {
			
			//consume header
			String[] cols = reader.readNext();
			
			//use header only if no columns are specified, otherwise ignore it.
			if (csv.columns().isEmpty())
				for (int i =0;i<cols.length;i++) {
					
					if (cols[i]==null || cols[i].isEmpty()) //synthetic column for missing header value
						cols[i] = "column-"+(i); 
					
					csv.with(col(cols[i]));
				}
					
			
		}
	}
	

}
