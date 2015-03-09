package smallgears.api.tabular;

import static java.lang.Integer.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static smallgears.api.tabular.utils.TableUtils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import smallgears.api.tabular.dsl.Dsl.SinkClause;
import smallgears.api.tabular.dsl.Dsl.SourceClause;
import smallgears.api.tabular.impl.CsvTable;
import lombok.Cleanup;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Directives to convert between {@link Table}s and CSV streams.
 * 
 * <p>
 * Most directives exist to address well-known format variations, while {@link #columns()} and {@link #rows()}
 * have a special role.
 * 
 * <ul>
 * <li> {@link #rows()} can be used to limit the amount of rows that are parsed or serialised.
 * <li> {@link #columns()} can be used to limit or control the columns that are parsed or serialised, as follows:
 * <ul>
 * 	<li>when parsing, a non-empty {@link #columns()} supersedes any header that may be in the data. 
 * If the data {@link #hasHeader()}, the header is discarded. 
 *  <li>when serialising, a non-empty {@link #columns()} supersedes {@link Table#columns()} when the data {@link #hasHeader()}.
 * </ul>
 * </ul>
 * 
 * Note also that: <p>
 * 
 * <ul>
 * <li> when parsing or serialising, rows are truncated to match the expected number of columns. <p>
 * This can be used to "vertically" subset the data, similarly to how {@link #rows()} can be used to "horizontally" subset the data.
 * <li> <em>after</em> parsing or serialising, {@link #columns()} is always set to the columns that have been actually used in the process.
 * </ul>
 * 
 */
@Data
@NoArgsConstructor(staticName="csv")
public class Csv {
	
	/**
	 * Whether the asset starts with a header row.
	 */
	private boolean hasHeader = true;
	
	/**
	 * The character that separates values in rows.
	 */
	private char delimiter = ',';
	
	/**
	 * The encoding of values.
	 */
	@NonNull
	private String encoding = Charset.defaultCharset().name();
	
	/**
	 * The character that may be used quote values.
	 */
	private char quote = '"';
	
	/**
	 * The maximum number of rows to be parsed.
	 */
	private int rows = MAX_VALUE;
	
	/**
	 * The columns of the assets (live view).
	 * <p>
	 * This compensates for the lack of a header in the asset.
	 * If a header exists, it indicates that parser should ignore it.
	 */
	@NonNull
	private final List<Column> columns = new ArrayList<>();
	
	
	/**
	 * A convenience to specify columns in fluent fashion.
	 */
	public Csv with(@NonNull Column ... cols) {
		columns.addAll(asList(cols));
		return this;
	}
	
	/**
	 * A convenience to specify columns in fluent fashion.
	 */
	public Csv with(@NonNull Iterable<Column> cols) {
		streamof(cols).forEach(columns::add);
		return this;
	}
	
	/**
	 * A convenience to specify columns in fluent fashion.
	 */
	public Csv with(@NonNull String ... cols) {
		return with(asList(cols));
	}
	
	/**
	 * A convenience to specify columns in fluent fashion.
	 */
	public Csv with(@NonNull Collection<String> cols) {
		return with(cols.stream().map(Column::new).collect(toList()));
	}
	
	/**
	 * Serialises a table using these directives.
	 * 
	 */
	@SneakyThrows
	public SinkClause serialise(Table table) {
		
		
		return stream->{
		
			try {
				
				Writer writer = new OutputStreamWriter(stream, encoding);
				
				@Cleanup
				CSVWriter csvwriter = new CSVWriter(writer,delimiter,quote);
				
				//priority to directives, fallback to table
				List<Column> columns = columns().isEmpty() ? table.columns() : columns();
				
				if (hasHeader) {
					
					List<String> colcoll = columns.stream().map(Column::name).collect(toList());
					
					csvwriter.writeNext(colcoll.toArray(new String[0]));
				
				}
				
				table.stream().map(
						        r->columns.stream().map(c->r.get(c.name())).filter(c->c!=null && !c.isEmpty()).collect(toList()))
							  .map(l->l.toArray(new String[0]))
							  .forEachOrdered(csvwriter::writeNext);
				
				
				csvwriter.flush();
				
				if (columns().isEmpty())
					columns().addAll(columns);
			
			}
			catch(Exception e) {
				
				throw (RuntimeException) e;
			}
		};
	}
	
	/**
	 * Serialises a table in memory using these directives, and streams the result.
	 * <p>
	 * Clients are responsible for closing the stream.
	 */
	@SneakyThrows
	public InputStream convert(Table table) {
		
		@Cleanup
		ByteArrayOutputStream stream = new ByteArrayOutputStream(512);
		
		serialise(table).to(stream);
		
		return new ByteArrayInputStream(stream.toByteArray());
		
	}
	
	/**
	 * Parses a given table using this directives.
	 */
	public SourceClause parse() {
		
		return stream->new CsvTable(this,stream);
	}
	
}
