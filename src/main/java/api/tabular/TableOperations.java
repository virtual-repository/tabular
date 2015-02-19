package api.tabular;

import static api.tabular.utils.TableUtils.*;
import static java.util.Arrays.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import api.tabular.utils.TableUtils;

/**
 * Table processing facilities.
 */
public class TableOperations {

	private static BiConsumer<Row, Row> joinfunction = (s,t) -> s.merge(t);

	
	/**
	 * Indexes a table by the (concatenated) values of one or more columns.
	 */
	public static IndexClause index(@NonNull Table table) {
		
		return new IndexClause() {
			
			@Override
			public Map<String, Row> using(@NonNull String... cols) {
				return using(asList(cols));
			}
			
			@Override
			public Map<String, Row> using(@NonNull Iterable<String> cols) {
				
				//key is concatenation of column values
				Function<Row,String> key = r -> TableUtils.join(streamof(cols).map(r::get));
				
				//deal with duplicates by picking latest (random choice)
				BinaryOperator<Row> picklatestduplicate = (r1,r2)->r2;
				
				return table.stream().filter(r->!key.apply(r).isEmpty()).collect(toMap(key,identity(),picklatestduplicate));
			}
		};
	}

	/**
	 * Pairs two columns.
	 */
	public static Match match(@NonNull String col) {
		return new Match(col,col);
	}
	
	/**
	 * Pairs two columns.
	 */
	public static Match match(@NonNull String sourcecol, @NonNull String targetcol) {
		return new Match(sourcecol,targetcol);
	}
	
	/**
	 * Joins the rows of two tables which have the same values in some columns.
	 */
	public static WithClause join(@NonNull Table table) {
		
		return target -> {
			
			return new JoinClause() {
				
				BiConsumer<Row, Row> function = joinfunction;
				Consumer<Row> fallback = (__) -> {};
				
				@Override
				public JoinClause using(@NonNull BiConsumer<Row, Row> function) {
					
					this.function = function;
					
					return this;
				}
				
				@Override
				public JoinClause fallbackWith(@NonNull Consumer<Row> function) {
					
					this.fallback=function;
					return this;
				}
				
				@Override
				public void basedOn(@NonNull Iterable<Match> matches) {
					
					List<String> targetcols = streamof(matches).map(Match::col2).collect(toList());
					
					Map<String,Row> targetIndex = index(target).using(targetcols);
					
					table.forEach(row-> {
						
						List<String> sourcecols = streamof(matches).map(Match::col1).collect(toList());
						
						String key  = TableUtils.join(streamof(sourcecols).map(row::get));
						
						if (targetIndex.containsKey(key))
							function.accept(row,targetIndex.get(key));
						else
							fallback.accept(row);
						
					});
					
					//add columns as we know the consumer function
					//unless they are matching columns that already exist.
					if (function==joinfunction)
						
						target.columns().stream()
						.filter(c ->
						!table.columns().contains(c) || !targetcols.contains(c.name())
						)
						.forEach(table.columns()::add);
						
					
				}
				
				@Override
				public void basedOn(@NonNull Match... matches) {
					
					basedOn(asList(matches));
				}
			};
			
		};
		
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	@AllArgsConstructor @Getter
	public static class Match {
		
		String col1;
		String col2;
	}
	
	public static interface IndexClause {
		
		 /**
		  * The columns to index on.
		  */
		 Map<String,Row> using(String ... cols);
		 
		 /**
		  * The columns to index on.
		  */
		 Map<String,Row> using(Iterable<String> cols);

	}
	
	@FunctionalInterface
	public static interface WithClause {
		
		/**
		 * The table to join with.
		 */
		JoinClause with(Table table);
		 
	}
	
	public static interface JoinClause {
		
		 /**
		 * The function that processes matching rows.
		 * <p>
		 * By default, the rows are joined.
		 */
		 JoinClause using(BiConsumer<Row, Row> function);
		 
		 /**
		 * The function that processes unmatched rows.
		 */
		 JoinClause fallbackWith(Consumer<Row> function);
		
		 /**
		 * One or more pairs of columns to match.
		 */
		 void basedOn(Match ... matches);
		 
		 /**
		 * One or more pairs of columns to match.
		 */
		 void basedOn(Iterable<Match> matches);
		 
	}
	
}