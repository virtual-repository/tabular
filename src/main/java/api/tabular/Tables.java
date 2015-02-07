package api.tabular;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.NonNull;
import api.tabular.Dsl.ColClause;
import api.tabular.Dsl.NameClause;
import api.tabular.Dsl.RowClause;
import api.tabular.Dsl.ValueClause;

public class Tables {

	
	public static Property prop(String name) {
		return new Property(name);
	}
	
	
	public static Column col(String name) {
		return new Column(name);
	}
	
	
	/**
	 * Creates a row.
	 */
	public static NameClause row() {
		
		final Map<String,String> map = new HashMap<>();
		
		class $Clause implements NameClause {
			
			@Override
			public Row end() {
				return new Row(map);
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
		}; 
		
		return new $Clause();
	}
	

	/**
	 * Creates a table from one ore more arrays.
	 * @param cols an array of column names
	 * @param rows zero or more arrays of row values
	 * @return the table
	 */
	public static Table table(@NonNull String[] cols, @NonNull String[] ... rows) {
		
		RowClause rclause = null;
		
		if (rows.length>0)
			for (int i=0; i<rows.length;i++)
				rclause = rclause==null? table().cols(cols).row(rows[i]) : rclause.row(rows[i]);
		
		return rclause.end();
	}
	
	/**
	 * Generates arrays for fluency of {@link #table(String[], String[]...)}.
	 *
	 **/
	public static String[] $$(String ... val) {
		return val;
	}
	
	public static ColClause table() {
		
		
		final List<Column> cols = new ArrayList<>();
		
		class $Clause implements ColClause {
			
			@Override
			public ColClause cols(@NonNull Column... $) {
				return cols(asList($));
			}
			
			@Override
			public ColClause cols(@NonNull Iterable<Column> $) {
				$.forEach(cols::add);
				return this;
			}
			
			@Override
			public ColClause cols(@NonNull String... $) {
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
			public RowClause row(@NonNull String... vals) {
				
				List<Row> rows = new ArrayList<Row>();
				
				class $RowClause implements RowClause {
					
					@Override
					public RowClause row(@NonNull String... vals) {
						
						NameClause row = Tables.row();
						
						Iterator<String> it = asList(vals).iterator();
					
						for (Column col : cols)
							if (it.hasNext())
								row.col(col).is(it.next());
						
						rows.add(row.end());
						
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
	
	

}
