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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FailureTest {
	/**
	 * A failed outcome should be able to be stored as a {@code Failure}
	 */
	@Test void Test01() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertTrue(outcome instanceof Failure);
	}

	/**
	 * {@code isSuccess} should respond with {@code false} in a {@code Failure}
	 */
	@Test void Test02() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertFalse(outcome.isSuccess());
	}

	/**
	 * Storing a null into a {@code Failure} should throw an Exception
	 */
	@Test void Test03() {
		assertThrows(
			NullPointerException.class,
			() -> {
				final Try<Double> outcome = Failure.of(null);
				assertFalse(outcome.isSuccess());
			}
		);
	}

	/**
	 * {@code isFailure} should respond with {@code true} in a {@code Failure}
	 */
	@Test void Test04() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertTrue(outcome.isFailure());
	}

	/**
	 * {@code getOrElse} should respond with the failure provided by the
	 * {@code Supplier}
	 */
	@Test void Test05() {
		final Supplier<Double> supplier = new Supplier<>() {
			@Override
			public Double get() {
				return 4.0D;
			}
		};

		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertEquals(4.0, outcome.getOrElse(supplier));
	}

	/**
	 * {@code getOrElse} should respond with a NullPointerException when
	 * the {@code Supplier} is null
	 */
	@Test void Test06() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertThrows(
			NullPointerException.class,
			() -> {
				assertEquals(3.0, outcome.getOrElse(null));
			}
		);
	}

	/**
	 * {@code orElse} should respond with the alternate value
	 */
	@Test void Test07() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertEquals(4.0, outcome.orElse(4.0D));
	}

	/**
	 * {@code get} should throw the exception stored in a {@code Failure}
	 */
	@Test void Test08() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertThrows(
			NullPointerException.class,
			() -> {
				outcome.get();
			}
		);
	}

	/**
	 * {@code flatMap} should return itself when called with a {@code Failure}
	 */
	@Test void Test09() {
		final Function<Double, Try<Integer>> mapper = new Function<>() {
			@Override
			public Try<Integer> apply(Double t) {
				return Success.of((int)Math.round(t));
			}
		};

		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertEquals(outcome, outcome.flatMap(mapper));
	}

	/**
	 * {@code flatMap} should return itself when called with {@code Failure}
	 * using a null mapping function
	 */
	@Test void Test10() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		final Try<Integer> mapOutcome = outcome.flatMap(null);
		assertEquals(outcome, mapOutcome);
	}

	/**
	 * {@code map} should respond with itself when called on a {@code Failure}
	 */
	@Test void Test11() {
		final Function<Double, Integer> mapper = new Function<>() {
			@Override
			public Integer apply(Double t) {
				return (int)Math.round(t);
			}
		};

		final Try<Double> outcome =
			Failure.of(new IllegalArgumentException("value of x cannot be null"));
		final Try<Integer> mapOutcome = outcome.map(mapper);
		assertEquals(outcome, mapOutcome);
	}

	/**
	 * {@code map} should respond with itself when calling a {@code Failure}
	 * with a null mapping function
	 */
	@Test void Test12() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		final Try<Integer> mapOutcome = outcome.map(null);
		assertEquals(outcome, mapOutcome);
	}

	/**
	 * {@code filter} should respond with a itself when a {@code filter} is
	 * applied to a {@code Failure}
	 */
	@Test void Test13() {
		final Predicate<Double> predicate = new Predicate<Double>() {
			@Override
			public boolean test(Double t) {
				return(Math.abs(t-3.0D) < 1E-8);
			}			
		};

		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertEquals(outcome, outcome.filter(predicate));
	}

	/**
	 * {@code filter} should respond with a with a {@code Failure} when the
	 * {@code filter} function is null
	 */
	@Test void Test14() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertEquals(outcome, outcome.filter(null));
	}

	/**
	 * {@code equals} should respond with true when tested against itself
	 */
	@Test void Test15() {
		final Try<Double> outcome = Failure.of(new NullPointerException());
		assertEquals(outcome, outcome);
	}

	/**
	 * {@code equals} should respond with true when tested against a {@code Failure}
	 * with the same value
	 */
	@Test void Test16() {
		final IllegalArgumentException exception =
			new IllegalArgumentException("value of x cannot be null");
		final Try<Double> outcome1 = Failure.of(exception);
		final Try<Double> outcome2 = Failure.of(exception);
		assertEquals(outcome1, outcome2);
	}

	/**
	 * {@code equals} should respond with false when tested against a {@code Failure}
	 * with a different value
	 */
	@Test void Test17() {
		final IllegalArgumentException exception1 =
			new IllegalArgumentException("value of x cannot be null");
		final IllegalArgumentException exception2 =
			new IllegalArgumentException("value of y cannot be null");
		final Try<Double> outcome1 = Failure.of(exception1);
		final Try<Double> outcome2 = Failure.of(exception2);
		assertNotEquals(outcome1, outcome2);
	}

	/**
	 * {@code equals} should respond with  "Failure[value]]" when tested
	 * against a {@code Failure}
	 */
	@Test void Test18() {
		final Try<Double> outcome = Failure.of(
			new IllegalArgumentException("value of x cannot be null")
		);
		assertEquals(
			"Failure[java.lang.IllegalArgumentException: value of x cannot be null]",
			outcome.toString()
		);
	}

	/**
	 * A successful outcome can be created by calling {@code Failure.of} with
	 * a {@code T} value as an argument
	 */
	@Test void Test19() {
		final Try<Double> outcome = Failure.of(3.0D);
		assertTrue(outcome.isSuccess());
	}
}