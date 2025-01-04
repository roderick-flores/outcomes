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

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.cynava.outcomes.optional.Optional;

/**
 * Interface representing the outcome of a computation that may either
 * result in a failure, represented by an Throwable, or a successfully
 * computed {@code T} value
 * 
 * @param <T> Type of object that might be stored in the Try outcome
 * @version 1.1
 * Version history:
 *    1.0 original version
 *    1.1 switched to Throwable
 *    1.2 added failed function
 */
public interface TryInterface<T>  extends Serializable {
	/**
	 * Determines if the outcome were successful
	 *
	 * @return {@code true} if the computation were successful or {@code false},
	 *         otherwise
	 */
	public boolean isSuccess();

	/**
	 * Determines if the outcome failed
	 *
	 * @return {@code true} if the computation failed or {@code false},
	 *         otherwise
	 */
	public boolean isFailure();

	/**
	 * Returns the {@code Success} value or a value produced by the specified
	 * supplier if the outcome were a {@code Failure}
	 *
	 * @param supplier Supplying function that produces a value to be returned
	 * @return Value, if success, or the result produced by the
	 *         supplying function. A {@code Failure} may be returned by the
	 *         supplier of if the supplying function is {@code null}
	 */
	public T getOrElse(final Supplier<T> supplier);

	/**
	 * Returns the {@code Success} value or the specified alternative if
	 * the outcome were a {@code Failure}
	 * 
	 * @param alternative Potentially {@code null} value on success or the
	 *        specified alternative for a failure
	 * @return Stored value or alternative
	 */
	public T orElse(final T alternative);

	/**
	 * Returns the {@code Success} value, if the outcome were successful.
	 * or throws the Throwable in the {@code Failure} otherwise
	 *
	 * @return Stored {@code Success}
	 * @throws Throwable When the {@code Try} is a {@code Failure}
	 */
	public T get() throws Throwable;

	/**
	 * Applies the given mapping function to the value from this Success or
	 * returns this if this is a Failure.
	 *
	 * @param <U> Type of value returned by the mapping function
	 * @param mapper Mapping function
	 * @return {@code Try} containing the result of the mapping function
	 *         or this instance
	 */
	public <U> Try<U> flatMap(
		final Function<? super T, ? extends Try<? extends U>> mapper
	);

	/**
	 * Applies the given mapping function to the {@code Try}, if a value
	 * is Success, or this {@code Failure} otherwise
	 *
	 * @param mapper Mapping function to apply
	 * @param <U> The type of the value returned from the mapping function
	 * @return {@code Try} containing the result of the mapping function
	 *         or this instance
	 * @throws NullPointerException Thrown when mapping function is {@code null}
	 */
	public <U> Try<U> map(
		final Function<? super T, ? extends U> mapper
	);

	/**
	 * Filters the value based upon the predicate. If the predicate is not
	 * satisfied, then a {@code Failure} is produced
	 *
	 * @param predicate Predicate to use
	 * @return {@code Optional<Try>} instance containing a value if the
	 *         predicate test succeeded
	 */
	public Try<T> filter(final Predicate<? super T> predicate);

	/**
	 * Coverts this {@code Try} to an {@code Optional} instance
	 * 
	 * @return {@code Success} containing the value for a {@code Success} or
	 *         {@code Empty} for a {@code Failure}
	 */
	public Optional<T> toOptional();

	/**
	 * Inverts this {@code Try}
	 * 
	 * @return {@code Try} containing a {@code Throwable} for a
	 *         {@code Failure} or a {@code Failure} for a {@code Success}
	 */
	public Try<Throwable> failed();
}