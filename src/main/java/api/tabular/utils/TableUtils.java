package api.tabular.utils;

import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;

import java.util.stream.Stream;

/**
 * Table processing facilities.
 */
public class TableUtils {

	public static <T> Stream<T> streamof(Iterable<T> vals) {
		
		return streamof(vals,false);
	}
	
	public static <T> Stream<T> streamof(Iterable<T> vals, boolean parallel) {
		
		return stream(vals.spliterator(),parallel);
	}
	
	public static String join(Stream<String> vals) {
		
		return vals.filter(s->s!=null).collect(joining());
	}
	
}
