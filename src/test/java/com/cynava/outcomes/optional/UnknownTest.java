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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;;

/**
 * Unit tests for the {@code Unknown} class
 * 
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class UnknownTest {
	/**
	 * {@code isPresent} should be false for a {@code Unknown} instance
	 */
	@Test void Test01() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertFalse(ourOptional.isPresent());
	}

	/**
	 * {@code isEmpty} should be false for a {@code Unknown} instance
	 */
	@Test void Test02() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertFalse(ourOptional.isEmpty());
	}

	/**
	 * {@code isUnknown} should be true for a {@code Unknown} instance
	 */
	@Test void Test03() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertTrue(ourOptional.isUnknown());
	}

	/**
	 * {@code ifPresent} is a no-op for a {@code Unknown} instance
	 */
	@Test void Test04() {
		final Consumer<Double> consumer = new Consumer<>() {
			@Override
			public void accept(Double t) {
				throw new UnsupportedOperationException("this consumer should not be called");
			}
			
		};

		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertDoesNotThrow(
			() -> {
				ourOptional.ifPresent(consumer);
			}
		);
	}

	/**
	 * {@code ifPresentOrElse} should execute the the {@code Runnable} for a
	 * {@code Unknown} instance
	 */
	@Test void Test05() {
		final Consumer<Double> consumer = new Consumer<>() {
			@Override
			public void accept(Double t) {
				throw new UnsupportedOperationException("this consumer should not be called");
			}
		};

		class TestRunnable implements Runnable {
			private boolean executed;

			public TestRunnable() {
				executed = false;
			}

			@Override
			public void run() {
				executed = true;
			}

			public boolean wasExecuted() {
				return executed;
			}
		};

		final TestRunnable ourRunnable = new TestRunnable();
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertFalse(ourRunnable.wasExecuted());
		assertDoesNotThrow(
			() -> {
				ourOptional.ifPresentOrElse(consumer, ourRunnable);
			}
		);
		assertTrue(ourRunnable.wasExecuted());
	}

	/**
	 * {@code get} should throw a {@code NoSuchElementException} for a
	 * {@code Unknown} instance
	 */
	@Test void Test06() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertThrows(
			NoSuchElementException.class,
			() -> {
				ourOptional.get();
			}
		);
	}

	/**
	 * {@code orElse} should respond with the alternative value for a
	 * {@code Unknown}
	 */
	@Test void Test07() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertEquals(3.0D, ourOptional.orElse(3.0D));
	}

	/**
	 * {@code orElseGet} should respond with the value created by the
	 * {@code Supplier} for a {@code Unknown} instance
	 */
	@Test void Test08() {
		final Supplier<Double> supplier = new Supplier<>() {
			@Override
			public Double get() {
				return 4.0D;
			}
		};

		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertEquals(4.0D, ourOptional.orElseGet(supplier));
	}

	/**
	 * {@code orElseThrow} should throw the specified exception for a
	 * {@code Unknown} instance
	 */
	@Test void Test09() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertThrows(
			NoSuchElementException.class,
			() -> {
				ourOptional.orElseThrow();
			}
		);
	}

	/**
	 * {@code orElseThrow} should throw an exception for a {@code Unknown}
	 * instance
	 */
	@Test void Test10() {
		class UnknownException extends RuntimeException {}

		final Supplier<UnknownException> exceptionSupplier = new Supplier<>() {
			@Override
			public UnknownException get() {
				throw new UnknownException();
			}			
		};

		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertThrows(
			UnknownException.class,
			() -> {
				ourOptional.orElseThrow(exceptionSupplier);
			}
		);
	}

	/**
	 * {@code orElseThrow} should throw a {@code NullPointerException} when
	 * called with a null supplier
	 */
	@Test void Test11() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertThrows(
			NullPointerException.class,
			() -> {
				ourOptional.orElseThrow(null);
			}
		);
	}

	/**
	 * {@code filter} should respond with an {@code Unknown} when called on an
	 * {@code Unknown} instance
	 */
	@Test void Test12() {
		final Predicate<Double> predicate = new Predicate<Double>() {
			@Override
			public boolean test(Double t) {
				return(Math.abs(t-3.0D) < 1E-8);
			}			
		};

		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertTrue(ourOptional.filter(predicate).isUnknown());
	}

	/**
	 * {@code filter} should respond with an {@code Unknown} when the filter
	 * predicate is null
	 */
	@Test void Test13() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertTrue(ourOptional.filter(null).isUnknown());
	}

	/**
	 * {@code map} should respond with an {@code Unknown} when a mapping
	 * function is provided
	 */
	@Test void Test14() {
		final Function<Double, Integer> mapper = new Function<>() {
			@Override
			public Integer apply(Double t) {
				return (int)Math.round(t);
			}
		};

		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertTrue(ourOptional.map(mapper).isUnknown());
	}

	/**
	 * {@code map} should respond with an {@code Unknown} when the mapping
	 * function is null
	 */
	@Test void Test15() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertTrue(ourOptional.map(null).isUnknown());
	}

	/**
	 * {@code flatMap} respond with an {@code Unknown} for a {@code Unknown}
	 * when a mapping function is used
	 */
	@Test void Test16() {
		final Function<Double, OptionalInterface<Integer>> ourMapper = new Function<>() {
			@Override
			public OptionalInterface<Integer> apply(Double t) {
				return Optional.of((int)Math.round(t));
			}
		};

		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		final OptionalInterface<Integer> ourResult =
			ourOptional.flatMap(ourMapper);
		assertTrue(ourResult.isUnknown());
	}

	/**
	 * {@code flatMap} should respond with a {@code Unknown} when the mapping
	 * function is null
	 */
	@Test void Test17() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		final OptionalInterface<Integer> ourResult =
			ourOptional.flatMap(null);
		assertTrue(ourResult.isUnknown());
	}

	/**
	 * {@code or} should respond with the value created by the {@code Supplier}
	 * for a {@code Unknown} instance
	 */
	@Test void Test18() {
		final Supplier<OptionalInterface<Double>> ourSupplier = new Supplier<>() {
			@Override
			public OptionalInterface<Double> get() {
				return Optional.of(5.0D);
			}
		};

		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertTrue(ourOptional.or(ourSupplier).isPresent());
		assertEquals(5.0D, ourOptional.or(ourSupplier).get());
	}

	/**
	 * {@code or} should respond with a {@code NullPointerException} when
	 * the {@code Supplier} is null
	 */
	@Test void Test19() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertThrows(
			NullPointerException.class,
			() -> {
				ourOptional.or(null);
			}
		);
	}

	/**
	 * {@code stream} should return nothing with a {@code Empty} instance
	 */
	@Test void Test20() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		final List<Double> ourList = ourOptional.stream().collect(Collectors.toList());
		assertEquals(0, ourList.size());
	}

	/**
	 * Unknown instances should return "Optional.unknown" when {@code toString}
	 * is called
	 */
	@Test void Test21() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertEquals("Optional.unknown", ourOptional.toString());
	}

	/**
	 * Unknown instances should all be equal
	 */
	@Test void Test22() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertEquals(Optional.unknown(), ourOptional);
	}

	/**
	 * Unknown instances should not be equal to present instances
	 */
	@Test void Test23() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertNotEquals(Optional.of(3.0D), ourOptional);

	}

	/**
	 * Unknown instances should not be equal to empty instances
	 */
	@Test void Test24() {
		final OptionalInterface<Double> ourOptional = Unknown.unknown();
		assertNotEquals(Optional.empty(), ourOptional);
	}
}