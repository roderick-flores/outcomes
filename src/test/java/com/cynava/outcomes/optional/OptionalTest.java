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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code Optional} class
 * 
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class OptionalTest {
	/**
	 * Instances created using (@code of} with a null should be empty
	 */
	@Test void Test01() {
		final OptionalInterface<Double> ourOptional = Optional.of(null);
		assertTrue(ourOptional.isEmpty());

		// because the original version allows nulls to be values, this will throw
		// an exception
		assertThrows(
			NullPointerException.class,
			() -> {
				java.util.Optional.of(null);
			}
		);
	}

	/**
	 * Instances created using {@code of} with a value should be present
	 */
	@Test void Test02() {
		final OptionalInterface<Double> ourOptional = Optional.of(3.0D);
		assertTrue(ourOptional.isPresent());
		assertEquals(3.0D, ourOptional.get());

		final java.util.Optional<Double> originalOptional = java.util.Optional.of(3.0D);
		assertTrue(originalOptional.isPresent());
		assertEquals(3.0D, ourOptional.get());
	}

	/**
	 * Instances created using {@code ofNullable} without a value should be empty
	 */
	@Test void Test03() {
		final OptionalInterface<Double> ourOptional = Optional.ofNullable(null);
		assertTrue(ourOptional.isEmpty());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.ofNullable(null);
		assertTrue(originalOptional.isEmpty());
	}

	/**
	 * Instances created using {@code ofNullable} with a value should be present
	 */
	@Test void Test04() {
		final OptionalInterface<Double> ourOptional = Optional.ofNullable(3.0D);
		assertTrue(ourOptional.isPresent());
		assertEquals(3.0D, ourOptional.get());

		final java.util.Optional<Double> originalOptional = java.util.Optional.ofNullable(3.0D);
		assertTrue(originalOptional.isPresent());
		assertEquals(3.0D, originalOptional.get());
	}

	/**
	 * Empty instances should be empty
	 */
	@Test void Test05() {
		final OptionalInterface<Double> ourOptional = Optional.empty();
		assertTrue(ourOptional.isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertTrue(originalOptional.isEmpty());
	}

	/**
	 * Unknown instances should be unknown
	 */
	@Test void Test06() {
		final OptionalInterface<Double> ourOptional = Optional.unknown();
		assertTrue(ourOptional.isUnknown());
	}
}