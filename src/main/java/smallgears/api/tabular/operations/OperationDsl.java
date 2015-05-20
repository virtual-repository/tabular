package smallgears.api.tabular.operations;

import static java.util.Arrays.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import smallgears.api.tabular.Row;
import smallgears.api.tabular.Table;
import smallgears.api.tabular.operations.TableOperations.Match;
import lombok.NonNull;

public class OperationDsl {

	
	public static interface IndexClause<T> {
		
		/**
		  * The columns to index on.
		  */
		default Map<String, T> using(@NonNull String... cols) {
			return using(asList(cols));
		}
		 
		 /**
		  * The columns to index on.
		  */
		 Map<String,T> using(Iterable<String> cols);

		 
		 IndexClause<String> over(String col);
	}
	
	public static interface GroupClause {
		
		/**
		  * The columns to group by.
		  */
		default Map<String, List<Row>> by(@NonNull String... cols) {
			return by(asList(cols));
		}
		 
		 /**
		  * The columns to group by.
		  */
		 Map<String,List<Row>> by(Iterable<String> cols);

	}
	
	public static interface ExistMapClause {
		
		default Map<String,Void> using(@NonNull String... cols) {
			return using(asList(cols));
		}
		 
		 /**
		  * The columns to index on.
		  */
		Map<String,Void> using(Iterable<String> cols);

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
