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

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Optionals are containers of a defined item (Present), an undefined
 * item (Unknown), and no item (Empty). The primary goal is to create
 * a representation of optional values that do not allow for null
 * values.
 * <p>
 * This interface allows near drop-in compatibility with
 * {@code java.util.Optional}. 
 * <p>
 * An {@code empty} instance is traditionally overloaded to
 * simultaneously indicate that a value has not been set or that a
 * value should be assigned. For example:
 * 
 * <pre>
 *     Optional&lt;?&gt; sc_optional = readValue();
 *     if( sc_optional.isEmpty() ) {
 *         // calculate the value if it has not be done previously
 *         sc_optional = calculateValue(args);
 *     }
 * </pre>
 * 
 * This, of course, assumes that the value could be calculated based
 * upon the supplied {@code args}. In an effort to add clarity, this
 * implementation introduces the {@code Unknown} member to represent
 * situations when a value is not able to be determined. This should
 * not be confused as an error. Attempting retries or handling would
 * not be helpful in those situations. Subsequent consumers of the
 * Optional would determine how to behave when it receives an
 * {@code Unknown} instead of an {@code Empty}. Users who find no
 * utility in having an {@code Unknown} should simply ignore it.
 * 
 * @param <T> Type of object that might be stored in the Optional
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public interface OptionalInterface<T> extends Serializable {
	/**
	 * Determines if a value is present
	 *
	 * @return {@code true} if a value is present and {@code false} otherwise
	 */
	public boolean isPresent();

	/**
	 * Performs the given action with the value, if present, or does nothing
	 * otherwise.
	 *
	 * @param action {@code Consumer} that applies an operation to the value
	 * @throws NullPointerException if value is present and the given action is
	 *         {@code null}
	 */
	public void ifPresent(final Consumer<? super T> action);

	/**
	 * Performs the given action with the value, if present, or performs the
	 * given empty-based action otherwise.
	 *
	 * @param action Action to be performed if a value is present
	 * @param emptyAction Empty-based action to be performed if no value is
	 *        present
	 * @throws NullPointerException Thrown when a value is present and the given
	 *         action is {@code null}, or no value is present and the given
	 *         empty-based action is {@code null}.
	 */
	public void ifPresentOrElse(
		final Consumer<? super T> action,
		final Runnable emptyAction
	);

	/**
	 * Determines if a value is not present
	 *
	 * @return {@code true} if a value is not present and {@code false} otherwise
	 */
	public boolean isEmpty();

	/**
	 * Determines if the value cannot be known
	 *
	 * @return {@code true} if a value is unknown and {@code false} otherwise
	 */
	public boolean isUnknown();

	/**
	 * Retrieves the value or throws an exception if it is not present
	 *
	 * @return Stored value
	 * @throws NoSuchElementException Thrown when no value is present
	 */
	public T get() throws NoSuchElementException;

	/**
	 * Retrieves the value or used the alternative if it is not present
	 *
	 * @param alternative Potentially {@code null} stored value to return
	 *        when the value is not present
	 * @return Stored value or alternative
	 */
	public T orElse(final T alternative);

	/**
	 * Retrieves the value if present or the result produced by the supplying
	 * function otherwise.
	 *
	 * @param supplier Supplying function that produces a value to be returned
	 * @return Value, if present, or the result produced by the
	 *         supplying function
	 * @throws NullPointerException if no value is present and the supplying
	 *         function is {@code null}
	 */
	public T orElseGet(final Supplier<? extends T> supplier);

	/**
	 * Retrieves the value if present or throws a {@code NoSuchElementException}.
	 *
	 * @return Stored value
	 * @throws NoSuchElementException if no value is present
	 */
	public T orElseThrow();

	/**
	 * Retrieves the value if present or throws an exception produced
	 * by the exception supplying function.
	 *
	 * @param <X> Throwable result supplied by the specified supplier
	 * @param supplier Supplying function that produces the exception
	 *        to be thrown
	 * @return Stored value
	 * @throws X Thrown if no value is present
	 * @throws NullPointerException if no value is present and the exception
	 *          supplying function is {@code null}
	 */
	public <X extends Throwable> T orElseThrow(
		final Supplier<? extends X> supplier
	) throws X;

	/**
	 * Filters the value based upon the predicate. If there is a match
	 * then this instance is returned. If there is no match or the value
	 * is empty then it returns an empty {@code OptionalInterface}.
	 *
	 * @param predicate Predicate to use
	 * @return A {@code OptionalInterface} instance containing a value
	 *         if the predicate test succeeded
	 * @throws NullPointerException Thrown when the predicate is {@code null}
	 */
	public OptionalInterface<T> filter(
		final Predicate<? super T> predicate
	);

	/**
	 * Applies the given mapping function to the value, if present, or returns an
	 * empty {@code OptionalInterface}. If the mapping function returns a
	 * {@code null} result then an {@code Empty} is returned.
	 *
	 * @see OptionalInterface#map
	 *
	 * @param mapper Mapping function to apply
	 * @param <U> The type of the value returned from the
	 *         mapping function
	 * @return {@code OptionalInterface} containing the result of the mapping
	 *         function applied to the value of this instance
	 * @throws NullPointerException Thrown when mapping function is {@code null}
	 */
	public <U> OptionalInterface<U> map(
		final Function<? super T, ? extends U> mapper
	);

	/**
	 * Applies the given {@code OptionalInterface}-bearing mapping function
	 * to the value, if present, or returns an empty {@code OptionalInterface}.
	 * If the mapping function returns a {@code null} result then an empty
	 * {@code OptionalInterface} is returned.
	 *
	 * @param <U> Type of value of the {@code OptionalInterface} returned by the
	 *            mapping function
	 * @param mapper Mapping function
	 * @return an {@code OptionalInterface} containing the result of
	 *         the mapping function applied to the value of this instance
	 * @throws NullPointerException Thrown when mapping function is {@code null} or
	 *         returns a {@code null} result
	 */
	public <U> OptionalInterface<U> flatMap(
		final Function<? super T, ? extends OptionalInterface<? extends U>> mapper
	);

	/**
	 * If a value is present, returns an {@code OptionalInterface} value,
	 * otherwise returns an {@code OptionalInterface} produced by the
	 * supplying function.
	 *
	 * @param supplier the supplying function that produces a
	 *        {@code OptionalInterface} value to be returned
	 * @return {@code OptionalInterface} containing the value in this instance
	 *         if it is present or one produced by the supplying function
	 *         otherwise
	 * @throws NullPointerException if the supplying function is {@code null} or
	 *         produces a {@code null} result
	 */
	public OptionalInterface<T> or(
		final Supplier<? extends OptionalInterface<? extends T>> supplier
	);

	/**
	 * Creates a sequential {@link Stream} containing only the value, if present,
	 * or returns an empty {@code Stream}.
	 *
	 * This method can be used to transform a {@code Stream} of optional
	 * elements to a {@code Stream} of present value elements:
	 * <pre>{@code
	 *     Stream<OptionalInterface<T>> os = ..
	 *     Stream<T> s = os.flatMap(OptionalInterface::stream)
	 * }</pre>
	 *
	 * @return OptionalInterface value as a {@code Stream}
	 */
	public Stream<T> stream();
}
