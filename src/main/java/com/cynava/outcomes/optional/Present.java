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
 * Representation of an Optional that has a value set
 * 
 * @param <T> Type of object that might be stored in the Optional
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class Present<T> extends Optional<T> {
	private static final long serialVersionUID = 956358313L;

	/** Value in the optional */
	private final T value;

	/**
	 * Constructs a present instance
	 */
	private Present(final T value) {
		super();
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * Creates a {@code Present} instance containing the given value
	 *
	 * @param value {@code Non-null} value
	 * @param <T> Type of the value
	 * @return {@code Optional} instance with the value present
	 * @throws NullPointerException Thrown when value is {@code null}
	 */
	public static <T> Optional<T> of(final T value) {
		if( value == null ) {
			return empty();
		}
		return new Present<T>(value);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public void ifPresent(final Consumer<? super T> action) {
		Objects.requireNonNull(action).accept(value);;
	}

	@Override
	public void ifPresentOrElse(final Consumer<? super T> action, Runnable emptyAction) {
		Objects.requireNonNull(action).accept(value);;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isUnknown() {
		return false;
	}

	@Override
	public T get() throws NoSuchElementException {
		return value;
	}

	@Override
	public T orElse(final T alternative) {
		return value;
	}

	@Override
	public T orElseGet(final Supplier<? extends T> supplier) {
		return value;
	}

	@Override
	public T orElseThrow() {
		return value;
	}

	@Override
	public <X extends Throwable> T orElseThrow(final Supplier<? extends X> supplier) throws X {
		return value;
	}

	@Override
	public OptionalInterface<T> filter(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		return predicate.test(value) ? this : Empty.empty();
	}

	@Override
	public <U> OptionalInterface<U> map(final Function<? super T, ? extends U> mapper) {
		return ofNullable(Objects.requireNonNull(mapper).apply(value));
	}

	@Override
	public <U> OptionalInterface<U> flatMap(
		final Function<? super T, ? extends OptionalInterface<? extends U>> mapper
	) {
		@SuppressWarnings("unchecked")
		final OptionalInterface<U> response =
			(OptionalInterface<U>) Objects.requireNonNull(mapper).apply(value);
		return Objects.requireNonNull(response);
	}

	@Override
	public OptionalInterface<T> or(
		final Supplier<? extends OptionalInterface<? extends T>> supplier
	) {
		return this;
	}

	@Override
	public Stream<T> stream() {
		return Stream.of(value);
	}

	@Override
	public boolean equals(final Object object) {
		// The other object is considered equal if it is also a {@code Present} 
		// and both instances values are equal to each other   
		if( this == object ) {
			return true;
		}

		if(object instanceof java.util.Optional<?>) {
			final java.util.Optional<?> javaOptional = (java.util.Optional<?>)object;
			return javaOptional.isPresent() && Objects.equals(value, javaOptional.get());
		}

		return object instanceof Present<?> other
			&& Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return ("Optional[" + value + "]");
	}
}
