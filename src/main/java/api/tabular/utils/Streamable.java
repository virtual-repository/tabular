package api.tabular.utils;

import static api.tabular.utils.TableUtils.*;

import java.util.function.Consumer;
import java.util.stream.Stream;


public interface Streamable<T> extends Iterable<T> {

	/**
	 * Returns a sequential stream of the value associated with this type.
	 */
	default Stream<T> stream() {
		
		return streamof(this,false);
	}
	

	/**
	 * Returns a sequential stream of the value associated with this type.
	 * @param parallel <code>true</code> if the stream is to be consumed in parallel, <code>false</code> otherwise.
	 */
	default Stream<T> parallelStream() {

		return streamof(this, true);
	}
	
	/**
	 * @see Stream#forEach(Consumer)
	 */
	default void forEach(Consumer<? super T> consumer) {
	
		stream().forEach(consumer);
	}
	
	
}
