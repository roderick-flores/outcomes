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

import org.junit.jupiter.api.Test;

import com.cynava.outcomes.optional.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TryTest {
	/**
	 * A successful outcome should be able to be stored as a {@code Success}
	 */
	@Test void Test01() {
		final Try<Double> outcome = Try.of(3.0D);
		assertTrue(outcome.isSuccess());
		assertDoesNotThrow(
			() -> {
				assertEquals(3.0D, outcome.get());
			}
		);
	}

	/**
	 * An exception outcome should be able to be stored as a {@code Failure}
	 */
	@Test void Test02() {
		final Try<Double> outcome = Try.of(new NullPointerException());
		assertTrue(outcome.isFailure());
		assertThrows(
			NullPointerException.class,
			() -> {
				assertEquals(3.0D, outcome.get());
			}
		);
	}

	/**
	 * A {@code Success} should be able to be converted to a {@code Present}
	 */
	@Test void Test03() {
		final Try<Double> outcome = Try.of(3.0D);
		final Optional<Double> outcomeOptional = outcome.toOptional();
		assertTrue(outcomeOptional.isPresent());
		assertEquals(3.0D, outcomeOptional.get());
	}

	/**
	 * A {@code Failure} should be able to be converted to an {@code Empty}
	 */
	@Test void Test04() {
		final Try<Double> outcome = Try.of(new NullPointerException());
		final Optional<Double> outcomeOptional = outcome.toOptional();
		assertTrue(outcomeOptional.isEmpty());
	}

}