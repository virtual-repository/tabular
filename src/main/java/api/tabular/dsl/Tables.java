package api.tabular.dsl;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.NonNull;
import api.tabular.Column;
import api.tabular.Csv;
import api.tabular.Properties;
import api.tabular.Property;
import api.tabular.Row;
import api.tabular.Table;
import api.tabular.dsl.Dsl.NameClause;
import api.tabular.dsl.Dsl.RowClause;
import api.tabular.dsl.Dsl.SourceClause;
import api.tabular.dsl.Dsl.TableClause;
import api.tabular.dsl.Dsl.ValueClause;
import api.tabular.impl.CsvTable;
import api.tabular.impl.SimpleTable;
import api.tabular.impl.StreamedTable;

public class Tables {

	/**
	 * A new property.
	 */
	public static Property prop(String name) {
		return Property.prop(name);
	}
	
	/**
	 * A new property.
	 */
	public static Property prop(String name, Object value) {
		return Property.prop(name,value);
	}
	
	/**
	 * A new group of properties.
	 */
	public static Properties props() {
		return Properties.props();
	}
	
	/**
	 * A new group of properties.
	 */
	public static Properties props(Property ... properties) {
		return Properties.props(properties);
	}
	
	/**
	 * A new column.
	 */
	public static Column col(String name) {
		return new Column(name);
	}
	
	/**
	 * New {@link Csv} directives.
	 */
	public static Csv csv() {
		return Csv.csv();
	}
	
	/**
	 * A new array
	 ***/
	@SafeVarargs
	public static <T> T[] $(T ... val) {
		return val;
	}
	
	/**
	 * A new row.
	 */
	public static Row row(String[] cols, String[] vals) {
		
		Row row = new Row();
		
		for (int i = 0; i < cols.length; i++)
			if (i<vals.length)
				row.set(cols[i], vals[i]);
		
		return row;
	}
	
	/**
	 * A new row.
	 */
	public static NameClause row() {
		
		final Map<String,String> map = new HashMap<>();
		
		class $Clause implements NameClause {
			
			@Override
			public Row end() {
				return new Row(map);
			}
			
			@Override
			public NameClause col(String col, String val) {
				return col(col).is(val);
			}
			
			@Override
			public ValueClause col(String name) {
				
				//in older java compilers, creating local classes inside closures creates infinite loops
				//let's do it out here.
				$Clause clause = new $Clause();
				
				return ($) -> {
					map.put(name,$);
					return clause;
				};
			}
			
			@Override
			public ValueClause col(Column col) {
				return col(col.name());
			}
			
			@Override
			public NameClause col(Column col, String val) {
				return col(col).is(val);
			}
		}; 
		
		return new $Clause();
	}
	
	
	/**
	 * Creates a table with columns and rows.
	 */
	public static Table table(String[] cols, String[]... rows) {
		
		return table().with(cols, rows);
	}
	
	/**
	 * Creates a table.
	 */
	public static TableClause table() {
		
		
		final List<Column> cols = new ArrayList<>();
		
		class $Clause implements TableClause {
			
			@Override
			public Table with(String[] cols, String[]... rows) {
				return cols(cols).rows(rows);
			}
			
			@Override
			public SourceClause from(Csv csv) {
				
				return stream -> new CsvTable(csv,stream);
				
			}
			
			@Override
			public TableClause cols(@NonNull Column... $) {
				return cols(asList($));
			}
			
			@Override
			public TableClause cols(@NonNull Iterable<Column> $) {
				$.forEach(cols::add);
				return this;
			}
			
			@Override
			public TableClause cols(@NonNull String... $) {
				
				return cols(asList($).stream().map(name->col(name)).collect(toList()));
			}
			
			@Override
			public TableClause cols(@NonNull Collection<String>  $) {
				
				return cols($.stream().map(name->col(name)).collect(toList()));
			}
			
			@Override
			public Table rows(@NonNull Row... $) {
				return new SimpleTable(cols,asList($));
			}
			
			@Override
			public Table rows(@NonNull Iterable<Row> $) {
				return new SimpleTable(cols,$);
			}
			
			@Override
			public Table rows(@NonNull Iterator<Row> $) {
				return new StreamedTable(cols,$);
			}
			
			@Override
			public Table rows(String[]... rows) {
				
				List<Row> rowlist = asList(rows).stream().map($->list2row(cols,asList($))).collect(toList());
				
				return rows(rowlist);
			}
			
			@Override
			public RowClause row(@NonNull String... vals) {
				return row(asList(vals));
			}
			
			@Override
			public RowClause row(@NonNull Iterable<String> vals) {
				
				List<Row> rows = new ArrayList<Row>();
				
				class $RowClause implements RowClause {
					
					@Override
					public RowClause row(@NonNull String... vals) {
						
						return row(asList(vals));
					}
					
					@Override
					public RowClause row(Iterable<String> vals) {

						rows.add(list2row(cols,vals));
						
						return new $RowClause();
					}
					
					@Override
					public Table end() {
						
						return new SimpleTable(cols,rows);
					}
				}
				
				return new $RowClause().row(vals);
			}
		}
			
			return new $Clause();
			
		}
		
	
	
	private static Row list2row(List<Column> cols, Iterable<String> vals) {
		
		NameClause row = Tables.row();
		
		Iterator<String> it = vals.iterator();
	
		for (Column col : cols)
			if (it.hasNext())
				row.col(col).is(it.next());
		
		return row.end();
	}

}
