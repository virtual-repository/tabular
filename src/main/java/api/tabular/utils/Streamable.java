package api.tabular.utils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public interface Streamable<T> extends Iterable<T> {

	/**
	 * Returns a sequential stream of the value associated with this type.
	 */
	default Stream<T> stream() {
		
		return stream(false);
	}
	

	/**
	 * Returns a sequential stream of the value associated with this type.
	 * @param parallel <code>true</code> if the stream is to be consumed in parallel, <code>false</code> otherwise.
	 */
	default Stream<T> stream(boolean parallel) {

		return StreamSupport.<T>stream(this.spliterator(), parallel);
	}
	
	
}
