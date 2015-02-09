package api.tabular;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.NonNull;
import api.tabular.Dsl.NameClause;
import api.tabular.Dsl.RowClause;
import api.tabular.Dsl.StreamClause;
import api.tabular.Dsl.TableClause;
import api.tabular.Dsl.ValueClause;

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
				
				return ($) -> {
					map.put(name,$);
					return new $Clause();
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
			public StreamClause from(Csv csv) {
				
				return $->new CsvTable(csv, $);
				
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
			public Table rows(@NonNull Row... $) {
				return new MaterialTable(cols,asList($));
			}
			
			@Override
			public Table rows(@NonNull Iterable<Row> $) {
				return new MaterialTable(cols,$);
			}
			
			@Override
			public Table rows(@NonNull Iterator<Row> $) {
				return new StreamedTable(cols,$);
			}
			
			@Override
			public Table rows(String[]... rows) {
				
				Function<String[],Row> array2row = $->list2row(cols,asList($));
				
				List<Row> rowlist = asList(rows).stream().map(array2row).collect(toList());
				
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
						
						return new MaterialTable(cols,rows);
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
