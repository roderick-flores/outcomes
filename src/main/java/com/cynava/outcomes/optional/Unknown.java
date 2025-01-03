/**
 * Copyright 2024-2024, Roderick Flores
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the Apache License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cynava.outcomes.optional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Representation of an Optional whose value cannot be determined
 * 
 * @param <T> Type of object that might be stored in the Optional
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class Unknown<T> extends Optional<T> {
	private static final long serialVersionUID = 916545660L;

	/**
	 * Constructs a Unknown instance
	 */
	protected Unknown() {}

	/**
	 * Creates an unknown {@code Optional} instance.  No value can be present
	 *
	 * @param <T> The type of the unknown value
	 * @return Unknown {@code Optional}
	 */
	public static <T> Unknown<T> unknown() {
		return new Unknown<T>();
	}

	@Override
	public boolean isPresent() {
		return false;
	}

	@Override
	public void ifPresent(Consumer<? super T> action) {
		// this is a no-op for an Unknown
	}

	@Override
	public void ifPresentOrElse(Consumer<? super T> action, Runnable unknownAction) {
		Objects.requireNonNull(unknownAction).run();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isUnknown() {
		return true;
	}

	@Override
	public T get() throws NoSuchElementException {
		throw new NoSuchElementException("Unknown instances cannot have values");
	}

	@Override
	public T orElse(T alternative) {
		return alternative;
	}

	@Override
	public T orElseGet(Supplier<? extends T> supplier) {
		if( supplier == null ) {
			throw new NullPointerException("supplier cannot be nul");
		}
		return supplier.get();
	}

	@Override
	public T orElseThrow() {
		return get();
	}

	@Override
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> supplier) throws X {
		throw supplier.get();
	}

	@Override
	public OptionalInterface<T> filter(Predicate<? super T> predicate) {
		return this;
	}

	@Override
	public <U> OptionalInterface<U> map(Function<? super T, ? extends U> mapper) {
		return unknown();
	}

	@Override
	public <U> OptionalInterface<U> flatMap(
		Function<? super T, ? extends OptionalInterface<? extends U>> mapper)
	{
		return unknown();
	}

	@Override
	public OptionalInterface<T> or(Supplier<? extends OptionalInterface<? extends T>> supplier) {
		@SuppressWarnings("unchecked")
		final OptionalInterface<T> response = (OptionalInterface<T>)supplier.get();
		return response;
	}

	@Override
	public Stream<T> stream() {
		return Stream.empty();
	}

	@Override
	public boolean equals(final Object object) {
		// The other object is considered equal if it is also a unknown
		// with the same hash code
		if( this == object ) {
			return true;
		}

		return( object instanceof Unknown<?>);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode("Optional.unknown");
	}

	@Override
	public String toString() {
		return "Optional.unknown";
	}
}
