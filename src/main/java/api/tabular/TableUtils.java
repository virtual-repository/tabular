package api.tabular;

import static java.util.Arrays.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Table processing facilities.
 */
public class TableUtils {

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
				Function<Row,String> key = r -> join(streamof(cols).map(r::get));
				
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
				
				BiConsumer<Row, Row> function = (s,t) -> s.merge(t);
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
					
					Iterable<String> targetcols = streamof(matches).map(Match::col2).collect(toList());
					
					Map<String,Row> targetIndex = index(target).using(targetcols);
					
					table.forEach(row-> {
						
						Iterable<String> sourcecols = streamof(matches).map(Match::col1).collect(toList());
						
						String key  = join(streamof(sourcecols).map(row::get));
						
						if (targetIndex.containsKey(key))
							function.accept(row,targetIndex.get(key));
						else
							fallback.accept(row);
						
					});
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
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private static <T> Stream<T> streamof(Iterable<T> vals) {
		
		return stream(vals.spliterator(),false);
	}
	
	private static String join(Stream<String> vals) {
		
		return vals.filter(s->s!=null).collect(joining());
	}
}
