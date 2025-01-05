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

/**
 * Base implementation of the {@code OptionalInterface}. This
 * implementation provides a variety of static methods for constructing
 * {@code Optional} instances that behave with near drop-in compatibility
 * with {@code java.util.Optional}
 * 
 * @param <T> Type of object that might be stored in the Optional
 * 
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public abstract class Optional<T> implements OptionalInterface<T>{
	private static final long serialVersionUID = 720995935L;

	/**
	 * Constructs an Optional
	 */
	protected Optional() {}

	/**
	 * Creates a {@code Present} instance containing the given value
	 *
	 * @param value {@code Non-null} value
	 * @param <T> Type of the value
	 * @return {@code Optional} instance with the value present
	 */
	public static <T> Optional<T> of(final T value) {
		return ofNullable(value);
	}

	/**
	 * Creates an instance containing the given value, if present
	 *
	 * @param value {@code java.util.Optional} value
	 * @param <T> Type of the value
	 * @return {@code Optional} instance with the value, if it were
	 *         present and Empty otherwise
	 */
	public static <T> Optional<T> of(final java.util.Optional<T> value) {
		if( value == null || value.isEmpty() ) {
			return empty();
		} else {
			return ofNullable(value.get());
		}
	}

	/**
	 * Creates a {@code Optional} instance containing a potentially
	 * {@code null} value or an empty otherwise.
	 *
	 * @param value Potentially {@code null} {@code T} value
	 * @param <T> T type of the value
	 * @return {@code Optional} instance
	 */
	public static <T> Optional<T> ofNullable(final T value) {
		return value == null ? Empty.empty() : Present.of(value);
	}

	/**
	 * Creates an empty {@code Optional} instance.  No value is present
	 *
	 * @param <T> Type of the non-existent value
	 * @return Empty {@code Optional}
	 */
	public static <T> Empty<T> empty() {
		return Empty.empty();
	}

	/**
	 * Creates an unknown {@code Optional} instance. No value is present
	 *
	 * @param <T> Type of the unknown value
	 * @return Unknown {@code Optional}
	 */
	public static <T> Unknown<T> unknown() {
		return Unknown.unknown();
	}

	/**
	 * Creates a {@code java.util.Optional<T>} from this Optional. 
	 * {@code Unknown} instances are converted into empty ones.
	 * 
	 * @return {@code java.util.Optional} containing the value, if
	 *         present, or empty otherwise
	 */
	public java.util.Optional<T> toJavaOptional() {
		if( isPresent() ) {
			return java.util.Optional.of(get());
		} else {
			return java.util.Optional.empty();
		}
	}
}
