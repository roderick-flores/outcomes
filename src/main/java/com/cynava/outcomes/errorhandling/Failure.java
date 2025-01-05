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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Representation of an optional with a value
 * 
 * @param <T> Type of object that might be stored in the Try outcome
 * @version 1.2
 * Version history:
 *    1.0 original version
 *    1.1 switched to Throwable; added a means to check the type of the Throwable
 *    1.2 added failed function
 */
public class Failure<T> extends Try<T> {
	private static final long serialVersionUID = 909272336L;

	/** Throwable in the failure */
	private final Throwable value;

	/**
	 * Constructs a Failure instance
	 */
	private Failure(final Throwable value) {
		super();
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * Creates a {@code Failure} instance containing the given value
	 *
	 * @param value {@code Non-null} value
	 * @param <T> Type of object that might be stored in the Try outcome
	 * @return {@code Success} instance with the return value present
	 */
	public static <T> Failure<T> of(final Throwable value) {
		if( value == null ) {
			Failure.of(new NullPointerException("value cannot be null"));
		}
		return new Failure<T>(value);
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public boolean isFailure() {
		return true;
	}

	@Override
	public T getOrElse(final Supplier<T> supplier) {
		if( supplier == null ) {
			throw new NullPointerException("supplier cannot be null");
		}
		return supplier.get();
	}

	@Override
	public T orElse(final T alternative) {
		return alternative;
	}

	@Override
	public T get() throws Throwable {
		throw value;
	}

	/**
	 * Determines if the specified {@code Throwable} class is assignment-compatible
	 * with the value in this {@code Failure}
	 * 
	 * @param <E> Type of class against which this Failure will be compared 
	 * @param throwable {@code Throwable} is assignment-compatible with the failure
	 * @return True if the failure is assignment-compatible with the failure and
	 *         false otherwise
	 */
	public <E extends Throwable> boolean isInstance(Class<E> throwable) {
		return throwable.isInstance(value);
	}

	@Override
	public <U> Try<U> flatMap(
		final Function<? super T, ? extends Try<? extends U>> mapper
	) {
		return new Failure<>(value);
	}

	@Override
	public <U> Try<U> map(final Function<? super T, ? extends U> mapper) {
		return new Failure<>(value);
	}

	@Override
	public Try<T> filter(final Predicate<? super T> predicate) {
		return this;
	}

	@Override
	public Try<Throwable> failed() {
		return Success.of(value);
	}

	@Override
	public boolean equals(final Object object) {
		// The other object is considered equal if it is also a {@code Failure} 
		// and both instances values are equal to each other   
		if( this == object ) {
			return true;
		}

		return object instanceof Failure<?> other
				&& Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return ("Failure[" + value + "]");
	}

	/**
	 * Retrieves the exception message for this failure
	 * 
	 * @return {@code String} localized message
	 */
	public String message() {
		return value.getLocalizedMessage();
	}

	/**
	 * Retrieves the stack trace for this failure
	 * 
	 * @return Array of {@code StackTraceElement} instances
	 */
	public StackTraceElement[] stackTrace() {
		return value.getStackTrace();
	}

	/**
	 * Retrieves the cause of this failure
	 * 
	 * @return {@code Throwable} cause
	 */
	public Throwable cause() {
		return value.getCause();
	}
}
