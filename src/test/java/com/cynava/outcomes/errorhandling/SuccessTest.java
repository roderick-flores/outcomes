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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SuccessTest {
	/**
	 * A successful outcome should be able to be stored as a {@code Success}
	 */
	@Test void Test01() {
		final Try<Double> outcome = Success.of(3.0D);
		assertTrue(outcome instanceof Success);
	}

	/**
	 * {@code isSuccess} should respond with {@code true} in a {@code Success}
	 */
	@Test void Test02() {
		final Try<Double> outcome = Success.of(3.0D);
		assertTrue(outcome.isSuccess());
	}

	/**
	 * Storing a null into a {@code Success} should throw an Exception
	 */
	@Test void Test03() {
		assertThrows(
			NullPointerException.class,
			() -> {
				final Try<Double> outcome = Success.of(null);
				assertFalse(outcome.isSuccess());
			}
		);
	}

	/**
	 * {@code isFailure} should respond with {@code false} in a {@code Success}
	 */
	@Test void Test04() {
		final Try<Double> outcome = Success.of(3.0D);
		assertFalse(outcome.isFailure());
	}

	/**
	 * {@code getOrElse} should respond with the stored value in a {@code Success}
	 */
	@Test void Test05() {
		final Supplier<Double> supplier = new Supplier<>() {
			@Override
			public Double get() {
				return 4.0D;
			}
		};

		final Try<Double> outcome = Success.of(3.0D);
		assertEquals(3.0, outcome.getOrElse(supplier));
	}

	/**
	 * {@code getOrElse} should respond with the stored value in a {@code Success}
	 * when the {@code Supplier} is null
	 */
	@Test void Test06() {
		final Try<Double> outcome = Success.of(3.0D);
		assertEquals(3.0, outcome.getOrElse(null));
	}

	/**
	 * {@code orElse} should respond with the stored value in a {@code Success}
	 */
	@Test void Test07() {
		final Try<Double> outcome = Success.of(3.0D);
		assertEquals(3.0, outcome.orElse(4.0D));
	}

	/**
	 * {@code get} should respond with the stored value in a {@code Success}
	 */
	@Test void Test08() {
		final Try<Double> outcome = Success.of(3.0D);
		assertDoesNotThrow(
			() -> {
				assertEquals(3.0D, outcome.get());
			}
		);
	}

	/**
	 * {@code flatMap} should respond with a mapping of the value in the
	 * {@code Success}
	 */
	@Test void Test09() {
		final Function<Double, Try<Integer>> mapper = new Function<>() {
			@Override
			public Try<Integer> apply(Double t) {
				return Success.of((int)Math.round(t));
			}
		};

		final Try<Double> outcome = Success.of(3.4D);
		final Try<Integer> mapOutcome = outcome.flatMap(mapper);
		assertDoesNotThrow(
			() -> {
				assertEquals(3, mapOutcome.get());
			}
		);
	}

	/**
	 * {@code flatMap} should respond with a {@code Failure} when the
	 * mapping function is null
	 */
	@Test void Test10() {
		final Try<Double> outcome = Success.of(3.4D);
		final Try<Integer> mapOutcome = outcome.flatMap(null);
		assertThrows(
			NullPointerException.class,
			() -> {
				assertEquals(3, mapOutcome.get());
			}
		);
	}

	/**
	 * {@code map} should respond with a mapping of the value in the
	 * {@code Success}
	 */
	@Test void Test11() {
		final Function<Double, Integer> mapper = new Function<>() {
			@Override
			public Integer apply(Double t) {
				return (int)Math.round(t);
			}
		};

		final Try<Double> outcome = Success.of(3.4D);
		final Try<Integer> mapOutcome = outcome.map(mapper);
		assertDoesNotThrow(
			() -> {
				assertEquals(3, mapOutcome.get());
			}
		);
	}

	/**
	 * {@code map} should respond with a {@code Failure} when the
	 * mapping function is null
	 */
	@Test void Test12() {
		final Try<Double> outcome = Success.of(3.4D);
		final Try<Integer> mapOutcome = outcome.map(null);
		assertThrows(
			NullPointerException.class,
			() -> {
				assertEquals(3, mapOutcome.get());
			}
		);
	}

	/**
	 * {@code filter} should respond with the instance when the {@code filter}
	 * matches the value stored in the {@code Success}
	 */
	@Test void Test13() {
		final Predicate<Double> predicate = new Predicate<Double>() {
			@Override
			public boolean test(Double t) {
				return(Math.abs(t-3.0D) < 1E-8);
			}			
		};

		final Try<Double> outcome = Success.of(3D);
		assertDoesNotThrow(
			() -> {
				assertEquals(3.0D, outcome.filter(predicate).get());
			}
		);
	}

	/**
	 * {@code filter} should respond with a {@code NoSuchElementException} when
	 * the {@code filter} does not match the value stored in the {@code Success}
	 */
	@Test void Test14() {
		final Predicate<Double> predicate = new Predicate<Double>() {
			@Override
			public boolean test(Double t) {
				return(Math.abs(t-4.0D) < 1E-8);
			}			
		};

		final Try<Double> outcome = Success.of(3D);
		assertThrows(
			NoSuchElementException.class,
			() -> {
				assertEquals(3, outcome.filter(predicate).get());
			}
		);
	}

	/**
	 * {@code filter} should respond with a with a {@code Failure} when the
	 * {@code filter} function is null
	 */
	@Test void Test15() {
		final Try<Double> outcome = Success.of(3D);
		assertThrows(
			NullPointerException.class,
			() -> {
				assertEquals(3, outcome.filter(null).get());
			}
		);
	}

	/**
	 * {@code equals} should respond with true when tested against itself
	 */
	@Test void Test16() {
		final Try<Double> outcome = Success.of(3D);
		assertEquals(outcome, outcome);
	}

	/**
	 * {@code equals} should respond with true when tested against a {@code Success}
	 * with the same value
	 */
	@Test void Test17() {
		final Try<Double> outcome1 = Success.of(3D);
		final Try<Double> outcome2 = Success.of(3D);
		assertEquals(outcome1, outcome2);
	}

	/**
	 * {@code equals} should respond with false when tested against a {@code Success}
	 * with a different value
	 */
	@Test void Test18() {
		final Try<Double> outcome1 = Success.of(3D);
		final Try<Double> outcome2 = Success.of(3.0000001D);
		assertNotEquals(outcome1, outcome2);
	}

	/**
	 * {@code equals} should respond with  "Success[value]]" when tested
	 * against a {@code Success}
	 */
	@Test void Test19() {
		final Try<Double> outcome = Success.of(3.4D);
		assertEquals("Success[3.4]", outcome.toString());
	}

	/**
	 * A failed outcome can be created by calling {@code Success.of} with
	 * an exception as an argument
	 */
	@Test void Test20() {
		final IllegalArgumentException e =
			new IllegalArgumentException("Success.of can but should not take an exception");
		final Try<Double> outcome = Success.of(e);
		assertTrue(outcome.isFailure());
	}
}