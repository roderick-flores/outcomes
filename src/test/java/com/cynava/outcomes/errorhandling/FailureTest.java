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

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
	 * {@code isInstance} should return true when the supplied class is compatible
	 * with the type of exception held in the failure. It should return false
	 * otherwise
	 */
	@Test void Test19() {
		ArrayList<Failure<?>> failures = new ArrayList<>();
		failures.add( Failure.of(new NullPointerException()) );
		failures.add( Failure.of(new IllegalArgumentException()) );
		failures.add( Failure.of(new OutOfRangeException(-1.0D, 0.0D, 100.0D)) );
		failures.add( Failure.of(new IndexOutOfBoundsException()) );

		assertTrue(failures.get(0).isInstance(NullPointerException.class));
		assertTrue(failures.get(1).isInstance(IllegalArgumentException.class));
		assertTrue(failures.get(2).isInstance(OutOfRangeException.class));
		assertTrue(failures.get(2).isInstance(IllegalArgumentException.class));
		assertTrue(failures.get(3).isInstance(IndexOutOfBoundsException.class));

		for( int index = 0; index < failures.size(); index++ ) {
			if(index == 0) {
				continue;
			}
			assertFalse(failures.get(index).isInstance(NullPointerException.class));
		}

		for( int index = 0; index < failures.size(); index++ ) {
			if(index == 1 || index == 2) {
				continue;
			}
			assertFalse(failures.get(index).isInstance(IllegalArgumentException.class));
		}

		for( int index = 0; index < failures.size(); index++ ) {
			if(index == 2) {
				continue;
			}
			assertFalse(failures.get(index).isInstance(OutOfRangeException.class));
		}

		for( int index = 0; index < failures.size(); index++ ) {
			if(index == 3) {
				continue;
			}
			assertFalse(failures.get(index).isInstance(IndexOutOfBoundsException.class));
		}
	}

	/**
	 * {@code should invert a Try}
	 */
	@Test void Test20() {
		final Try<Double> failureOutcome = Failure.of(new TimeoutException());
		assertTrue(failureOutcome.isFailure());
		assertFalse(failureOutcome.failed().isFailure());
		assertDoesNotThrow(
			() -> {
				assertTrue((failureOutcome.failed()).get() instanceof TimeoutException);
			}
		);

		final Try<Double> successOutcome = Success.of(3.14159D);
		assertTrue(successOutcome.isSuccess());
		assertFalse(successOutcome.failed().isSuccess());
		assertThrows(
			UnsupportedOperationException.class,
			() -> {
				successOutcome.failed().get();
			}
		);
	}

	/**
	 * Demonstrate that specific Exceptions can be found
	 */
	@Test void Test21() {
		final ArrayList<Try<Double>> outcomes = new ArrayList<>();
		outcomes.add(Failure.of(new IllegalArgumentException("one")));
		outcomes.add(Success.of(2.0D));
		outcomes.add(Failure.of(new IllegalArgumentException("three")));
		outcomes.add(Success.of(4.0D));
		outcomes.add(Failure.of(new IllegalArgumentException("five")));
		outcomes.add(Success.of(6.0D));
		outcomes.add(Failure.of(new IllegalArgumentException("seven")));
		outcomes.add(Success.of(8.0D));
		outcomes.add(Failure.of(new IllegalArgumentException("nine")));
		outcomes.add(Success.of(10.0D));
		outcomes.add(Failure.of(new TimeoutException("eleven")));

		final Predicate<Try<Double>> successPredicate = new Predicate<>() {
			@Override
			public boolean test(Try<Double> t) {
				return t.isSuccess();
			}
						
		};

		final Predicate<Try<Double>> failurePredicate = new Predicate<>() {
			@Override
			public boolean test(Try<Double> t) {
				return t.isFailure();
			}
						
		};

		final Predicate<Try<Double>> timedOutPredicate = new Predicate<>() {
			@Override
			public boolean test(Try<Double> t) {
				return t.isFailure() ?
					 ((Failure<Double>)t).isInstance(TimeoutException.class) : false;
			}						
		};

		final List<Try<Double>> successes =
			outcomes.stream().filter(successPredicate).collect(Collectors.toList());
		assertEquals(5, successes.size());

		final List<Try<Double>> failures =
			outcomes.stream().filter(failurePredicate).collect(Collectors.toList());
		assertEquals(6, failures.size());

		final List<Try<Double>> timeoutFailures =
			outcomes.stream().filter(timedOutPredicate).collect(Collectors.toList());
		assertEquals(6, timeoutFailures.size());
	}
}