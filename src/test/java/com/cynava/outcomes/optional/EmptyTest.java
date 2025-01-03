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
 * Unit tests for the {@code Empty} class
 * 
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class EmptyTest {
	/**
	 * {@code isPresent} should be false for a {@code Empty} instance
	 */
	@Test void Test01() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertFalse(ourOptional.isPresent());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertFalse(originalOptional.isPresent());
	}

	/**
	 * {@code isEmpty} should be true for a {@code Empty} instance
	 */
	@Test void Test02() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertTrue(ourOptional.isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertTrue(originalOptional.isEmpty());
	}

	/**
	 * {@code isUnknown} should be false for a {@code Empty} instance
	 */
	@Test void Test03() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertFalse(ourOptional.isUnknown());
	}

	/**
	 * {@code ifPresent} is a no-op for a {@code Empty} instance 
	 */
	@Test void Test04() {
		final Consumer<Double> consumer = new Consumer<>() {
			@Override
			public void accept(Double t) {
				throw new UnsupportedOperationException("this consumer should not be called");
			}
			
		};

		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertDoesNotThrow(
			() -> {
				ourOptional.ifPresent(consumer);
			}
		);

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertDoesNotThrow(
			() -> {
				originalOptional.ifPresent(consumer);
			}
		);
	}

	/**
	 * {@code ifPresentOrElse} should execute the the {@code Runnable} for a
	 * {@code Empty} instance
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
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertFalse(ourRunnable.wasExecuted());
		assertDoesNotThrow(
			() -> {
				ourOptional.ifPresentOrElse(consumer, ourRunnable);
			}
		);
		assertTrue(ourRunnable.wasExecuted());

		final TestRunnable originalRunnable = new TestRunnable();
		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertFalse(originalRunnable.wasExecuted());
		assertDoesNotThrow(
			() -> {
				originalOptional.ifPresentOrElse(consumer, originalRunnable);
			}
		);
		assertTrue(originalRunnable.wasExecuted());
	}

	/**
	 * {@code get} should throw a {@code NoSuchElementException} for a
	 * {@code Empty} instance
	 */
	@Test void Test06() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertThrows(
			NoSuchElementException.class,
			() -> {
				ourOptional.get();
			}
		);

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertThrows(
			NoSuchElementException.class,
			() -> {
				originalOptional.get();
			}
		);
	}

	/**
	 * {@code orElse} should respond with the alternative value for a
	 * {@code Empty}
	 */
	@Test void Test07() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertEquals(3.0D, ourOptional.orElse(3.0D));

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertEquals(3.0D, originalOptional.orElse(3.0D));
	}

	/**
	 * {@code orElseGet} should respond with the value created by the
	 * {@code Supplier} for a {@code Empty} instance
	 */
	@Test void Test08() {
		final Supplier<Double> supplier = new Supplier<>() {
			@Override
			public Double get() {
				return 4.0D;
			}
		};

		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertEquals(4.0D, ourOptional.orElseGet(supplier));

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertEquals(4.0D, originalOptional.orElseGet(supplier));
	}

	/**
	 * {@code orElseThrow} should throw the specified exception for a
	 * {@code Empty} instance
	 */
	@Test void Test09() {
		final Supplier<Double> supplier = new Supplier<>() {
			@Override
			public Double get() {
				return 4.0D;
			}
		};

		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertEquals(4.0D, ourOptional.orElseGet(supplier));

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertEquals(4.0D, originalOptional.orElseGet(supplier));
	}

	/**
	 * {@code orElseThrow} should throw an exception for a {@code Empty}
	 * instance
	 */
	@Test void Test10() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertThrows(
			NoSuchElementException.class,
			() -> {
				ourOptional.orElseThrow();
			}
		);

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertThrows(
			NoSuchElementException.class,
			() -> {
				originalOptional.orElseThrow();
			}
		);
	}

	/**
	 * {@code orElseThrow} should throw a {@code NullPointerException} when
	 * called with a null supplier
	 */
	@Test void Test11() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertThrows(
			NullPointerException.class,
			() -> {
				ourOptional.orElseThrow(null);
			}
		);

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertThrows(
			NullPointerException.class,
			() -> {
				originalOptional.orElseThrow(null);
			}
		);
	}

	/**
	 * {@code filter} should respond with an {@code Empty} when called on an
	 * {@code Empty} instance
	 */
	@Test void Test12() {
		final Predicate<Double> predicate = new Predicate<Double>() {
			@Override
			public boolean test(Double t) {
				return(Math.abs(t-3.0D) < 1E-8);
			}			
		};

		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertTrue(ourOptional.filter(predicate).isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertTrue(originalOptional.filter(predicate).isEmpty());
	}

	/**
	 * {@code filter} should respond with an {@code Empty} when the filter
	 * predicate is null
	 */
	@Test void Test13() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertTrue(ourOptional.filter(null).isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertThrows(
			NullPointerException.class,
			() -> {
				assertTrue(originalOptional.filter(null).isEmpty());
			}
		);
	}

	/**
	 * {@code map} should respond with an {@code Empty} when a mapping
	 * function is provided
	 */
	@Test void Test14() {
		final Function<Double, Integer> mapper = new Function<>() {
			@Override
			public Integer apply(Double t) {
				return (int)Math.round(t);
			}
		};

		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertTrue(ourOptional.map(mapper).isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertTrue(originalOptional.map(mapper).isEmpty());
	}

	/**
	 * {@code map} should respond with an {@code Empty} when the mapping
	 * function is null
	 */
	@Test void Test15() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertTrue(ourOptional.map(null).isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertThrows(
			NullPointerException.class,
			() -> {
				assertTrue(originalOptional.map(null).isEmpty());
			}
		);
	}

	/**
	 * {@code flatMap} respond with an {@code Empty} for a {@code Empty}
	 * when a mapping function is used
	 */
	@Test void Test16() {
		final Function<Double, OptionalInterface<Integer>> ourMapper = new Function<>() {
			@Override
			public OptionalInterface<Integer> apply(Double t) {
				return Optional.of((int)Math.round(t));
			}
		};

		final OptionalInterface<Double> ourOptional = Empty.empty();
		final OptionalInterface<Integer> ourResult =
			ourOptional.flatMap(ourMapper);
		assertTrue(ourResult.isEmpty());

		final Function<Double, java.util.Optional<Integer>> originalMapper = new Function<>() {
			@Override
			public java.util.Optional<Integer> apply(Double t) {
				return java.util.Optional.of((int)Math.round(t));
			}
		};

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		final java.util.Optional<Integer> originalResult =
			originalOptional.flatMap(originalMapper);
		assertTrue(originalResult.isEmpty());
	}

	/**
	 * {@code flatMap} should respond with a {@code Empty} when the mapping
	 * function is null
	 */
	@Test void Test17() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		final OptionalInterface<Integer> ourResult =
			ourOptional.flatMap(null);
		assertTrue(ourResult.isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertThrows(
			NullPointerException.class,
			() -> {
				originalOptional.flatMap(null);
			}
		);
	}

	/**
	 * {@code or} should respond with the value created by the {@code Supplier}
	 * for a {@code Empty} instance
	 */
	@Test void Test18() {
		final Supplier<OptionalInterface<Double>> ourSupplier = new Supplier<>() {
			@Override
			public OptionalInterface<Double> get() {
				return Optional.of(5.0D);
			}
		};

		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertTrue(ourOptional.or(ourSupplier).isPresent());
		assertEquals(5.0D, ourOptional.or(ourSupplier).get());

		final Supplier<java.util.Optional<Double>> originalSupplier = new Supplier<>() {
			@Override
			public java.util.Optional<Double> get() {
				return java.util.Optional.of(5.0D);
			}
		};

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertTrue(originalOptional.or(originalSupplier).isPresent());
		assertEquals(5.0D, originalOptional.or(originalSupplier).get());
	}

	/**
	 * {@code or} should respond with a {@code NullPointerException} when
	 * the {@code Supplier} is null
	 */
	@Test void Test19() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertThrows(
			NullPointerException.class,
			() -> {
				ourOptional.or(null);
			}
		);

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertThrows(
			NullPointerException.class,
			() -> {
				assertTrue(originalOptional.or(null).isPresent());
			}
		);
	}

	/**
	 * {@code stream} should return nothing with a {@code Empty} instance
	 */
	@Test void Test20() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		final List<Double> ourList = ourOptional.stream().collect(Collectors.toList());
		assertEquals(0, ourList.size());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		final List<Double> originalList = originalOptional.stream().collect(Collectors.toList());
		assertEquals(0, originalList.size());
	}

	/**
	 * {@code toString} should return "Optional.empty" for a {@code Empty}
	 * instance
	 */
	@Test void Test21() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertEquals("Optional.empty", ourOptional.toString());

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertEquals("Optional.empty", originalOptional.toString());
	}

	/**
	 * Empty instances should all be equal
	 */
	@Test void Test22() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertEquals(Optional.empty(), ourOptional);

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertEquals(java.util.Optional.empty(), originalOptional);
	}

	/**
	 * Empty instances should not be equal to present instances
	 */
	@Test void Test23() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertNotEquals(Optional.of(3.0D), ourOptional);

		final java.util.Optional<Double> originalOptional = java.util.Optional.empty();
		assertNotEquals(java.util.Optional.of(3.0D), originalOptional);
	}

	/**
	 * Empty instances should not be equal to unknown instances
	 */
	@Test void Test24() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertNotEquals(Optional.unknown(), ourOptional);
	}

	/**
	 * Empty instances should be equal to empty {@code java.util.Optional}
	 * instances. Note: the {@code java.util.Optional} equals method
	 * will not work
	 */
	@Test void Test25() {
		final OptionalInterface<Double> ourOptional = Empty.empty();
		assertEquals(ourOptional, java.util.Optional.empty());
	}
}