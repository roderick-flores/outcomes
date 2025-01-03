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
package com.cynava.outcomes.errorhandling;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of an optional with a value
 * 
 * @param <T> Type of object that might be stored in the Try outcome
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class Success<T> extends Try<T> {
	/** logger for this class */
	private static final Logger logger =
		LoggerFactory.getLogger(Success.class);

	/** Value in the optional */
	private final T value;

	/**
	 * Constructs a Success instance
	 * 
	 * @throws NullPointerException Thrown when value is {@code null}
	 */
	private Success(final T value) throws NullPointerException {
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * Creates a {@code Success} instance containing the given value. This
	 * will not accept a null value; use {@code Try<Optional<T>>} if
	 * undefined values are desired.
	 *
	 * @param <T> Type of object that might be stored in the Try outcome
	 * @param value Non-null value
	 * @return {@code Success} instance with the return value present
	 * @throws NullPointerException When a null is specified as the value
	 */
	public static <T> Try<T> of(final T value) throws NullPointerException {
		if( value == null ) {
			throw new NullPointerException("value cannot be null");
		}
		return new Success<T>(value);
	}

	/**
	 * Logs a warning if {@code Success.of} is called with an exception as
	 * the argument
	 *
	 * @param exception {@code Non-null} exception
	 * @param <T> Type of object that might be stored in the Try outcome
	 * @return {@code Failure} instance with the value present
	 * @throws NullPointerException When a null is specified as the value
	 */
	public static <T> Try<T> of(final Exception exception)
		throws IllegalArgumentException
	{
		if( exception == null ) {
			Failure.of(new NullPointerException("exception cannot be null"));
		}
		logger.warn("an exception should not be passed to Success.of()");
		return Failure.of(exception);
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public boolean isFailure() {
		return false;
	}

	@Override
	public T getOrElse(final Supplier<T> supplier) {
		return value;
	}

	@Override
	public T orElse(final T alternative) {
		return value;
	}

	@Override
	public T get() throws Exception {
		return value;
	}

	@Override
	public <U> Try<U> flatMap(
		final Function<? super T, ? extends Try<? extends U>> mapper
	) {
		if( mapper == null ) {
			return Failure.of(new NullPointerException("mapper function is null"));
		}

		try {
			@SuppressWarnings("unchecked")
			final Try<U> result = (Try<U>)mapper.apply(value);
			return result;
		} catch( Exception e ) {
			return Failure.of(e);
		}
	}

	@Override
	public <U> Try<U> map(final Function<? super T, ? extends U> mapper) {
		if( mapper == null ) {
			return Failure.of(new NullPointerException("mapper function is null"));
		}

		try {
			return Success.of(mapper.apply(value));
		} catch( Exception e ) {
			return Failure.of(e);
		}
	}

	@Override
	public Try<T> filter(final Predicate<? super T> predicate) {
		if( predicate == null ) {
			return Failure.of(new NullPointerException("predicate is null"));
		}
		return predicate.test(value) ? this :
			Failure.of(new NoSuchElementException("Predicate does not hold for " + value));
	}

	@Override
	public boolean equals(final Object object) {
		// The other object is considered equal if it is also a {@code Present} 
		// and both instances values are equal to each other   
		if( this == object ) {
			return true;
		}

		return object instanceof Success<?> other
				&& Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return ("Success[" + value + "]");
	}
}
