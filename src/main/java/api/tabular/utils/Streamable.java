package api.tabular.utils;

import java.util.Iterator;
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
	
		//lambdas seems to crash older javac verson, such as on CI machines
		class InnerIterable implements Iterable<T> {
			@Override
			public Iterator<T> iterator() {
				return Streamable.this.iterator();
			}
		}
		
		return StreamSupport.<T>stream(new InnerIterable().spliterator(), parallel);
	}
	
	
}
