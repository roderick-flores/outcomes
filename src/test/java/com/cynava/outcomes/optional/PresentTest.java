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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;;

/**
 * Unit tests for the {@code Present} class
 * 
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class PresentTest {
	/**
	 * {@code isPresent} should be true for a {@code Present} instance
	 */
	@Test void Test01() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertTrue(ourOptional.isPresent());

		final java.util.Optional<Double> originalOptional = java.util.Optional.of(3.0D);
		assertTrue(originalOptional.isPresent());
	}

	/**
	 * {@code isEmpty} should be false for a {@code Present} instance
	 */
	@Test void Test02() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertFalse(ourOptional.isEmpty());

		final java.util.Optional<Double> originalOptional = java.util.Optional.of(3.0D);
		assertFalse(originalOptional.isEmpty());
	}

	/**
	 * {@code isUnknown} should be false for a {@code Present} instance
	 */
	@Test void Test03() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertFalse(ourOptional.isUnknown());
	}

	/**
	 * {@code ifPresent} should be given the value for a {@code Present} to
	 * process using the supplied {@code Consumer} 
	 */
	@Test void Test04() {
		class TestConsumer implements Consumer<Double> {
			private Double value = Double.MAX_VALUE;

			@Override
			public void accept(Double t) {
				value = 2*t;
			}

			public Double value() {
				return value;
			}
		}
		final TestConsumer consumer = new TestConsumer();

		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		ourOptional.ifPresent(consumer);
		assertEquals(6.0D, consumer.value());

		final java.util.Optional<Double> originalOptional = java.util.Optional.of(4.0D);
		originalOptional.ifPresent(consumer);
		assertEquals(8.0D, consumer.value());
	}

	/**
	 * {@code ifPresentOrElse} should respond with the value for a {@code Present}
	 * instance
	 */
	@Test void Test05() {
		class TestConsumer implements Consumer<Double> {
			private Double value = Double.MAX_VALUE;

			@Override
			public void accept(Double t) {
				value = 2*t;
			}

			public Double value() {
				return value;
			}
		}
		final TestConsumer consumer = new TestConsumer();

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
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertFalse(ourRunnable.wasExecuted());
		ourOptional.ifPresentOrElse(consumer, ourRunnable);
		assertEquals(6.0D, consumer.value());
		assertFalse(ourRunnable.wasExecuted());

		final TestRunnable originalRunnable = new TestRunnable();
		final java.util.Optional<Double> originalOptional = java.util.Optional.of(4.0D);
		assertFalse(originalRunnable.wasExecuted());
		originalOptional.ifPresentOrElse(consumer, ourRunnable);
		assertEquals(8.0D, consumer.value());
		assertFalse(originalRunnable.wasExecuted());
	}

	/**
	 * {@code get} should respond with the value for a {@code Present}
	 * instance
	 */
	@Test void Test06() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertEquals(3.0D, ourOptional.get());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(4.0D);
		assertEquals(4.0D, originalOptional.get());
	}

	/**
	 * {@code orElse} should respond with the value for a {@code Present}
	 */
	@Test void Test07() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertEquals(3.0D, ourOptional.orElse(5.0D));

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(4.0D);
		assertEquals(4.0D, originalOptional.orElse(5.0D));
	}

	/**
	 * {@code orElseGet} should respond with the value for a {@code Present}
	 * instance
	 */
	@Test void Test08() {
		final Supplier<Double> supplier = new Supplier<>() {
			@Override
			public Double get() {
				return 5.0D;
			}
		};

		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertEquals(3.0D, ourOptional.orElseGet(supplier));

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(4.0D);
		assertEquals(4.0D, originalOptional.orElseGet(supplier));
	}

	/**
	 * {@code orElseThrow} should not throw an exception for a {@code Present}
	 * instance
	 */
	@Test void Test09() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertEquals(3.0D, ourOptional.orElseThrow());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(4.0D);
		assertEquals(4.0D, originalOptional.orElseThrow());
	}

	/**
	 * {@code orElseThrow} should respond with the value for a {@code Present}
	 * instance
	 */
	@Test void Test10() {
		class EmptyException extends RuntimeException {}

		final Supplier<EmptyException> exceptionSupplier = new Supplier<>() {
			@Override
			public EmptyException get() {
				throw new EmptyException();
			}			
		};

		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertEquals(3.0D, ourOptional.orElseThrow(exceptionSupplier));

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(4.0D);
		assertEquals(4.0D, originalOptional.orElseThrow(exceptionSupplier));
	}

	/**
	 * {@code flatMap} should respond with its {@code Present} value when the
	 * mapping function is null
	 */
	@Test void Test11() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertEquals(3.0D, ourOptional.orElseThrow(null));

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(4.0D);
		assertEquals(4.0D, originalOptional.orElseThrow(null));
	}

	/**
	 * {@code filter} should respond with the {@code Present} when the filter
	 * predicate matches the {@code Present} value
	 */
	@Test void Test12() {
		final Predicate<Double> predicate = new Predicate<Double>() {
			@Override
			public boolean test(Double t) {
				return(Math.abs(t-3.0D) < 1E-8);
			}			
		};

		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertEquals(3.0D, ourOptional.filter(predicate).get());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.0D);
		assertEquals(3.0D, originalOptional.filter(predicate).get());
	}

	/**
	 * {@code filter} should respond with an {@code Empty} when the filter
	 * predicate does not match the {@code Present} value
	 */
	@Test void Test13() {
		final Predicate<Double> predicate = new Predicate<Double>() {
			@Override
			public boolean test(Double t) {
				return(Math.abs(t-4.0D) < 1E-8);
			}			
		};

		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertTrue(ourOptional.filter(predicate).isEmpty());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.0D);
		assertTrue(originalOptional.filter(predicate).isEmpty());
	}

	/**
	 * {@code filter} should throw a {@code NullPointerException} when the filter
	 * predicate is null
	 */
	@Test void Test14() {
		final OptionalInterface<Double> ourOptional = Present.of(3.0D);
		assertThrows(
			NullPointerException.class,
			() -> {
				ourOptional.filter(null);
			}
		);

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.0D);
		assertThrows(
			NullPointerException.class,
			() -> {
				originalOptional.filter(null);
			}
		);
	}

	/**
	 * {@code map} should respond with the {@code Present} value processed by
	 * the mapping function
	 */
	@Test void Test15() {
		final Function<Double, Integer> mapper = new Function<>() {
			@Override
			public Integer apply(Double t) {
				return (int)Math.round(t);
			}
		};

		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		assertEquals(3, ourOptional.map(mapper).get());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.5D);
		assertEquals(4, originalOptional.map(mapper).get());
	}

	/**
	 * {@code map} should throw a NullPointerException for a {@code Present}
	 * when a null mapping function is used
	 */
	@Test void Test16() {
		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		assertThrows(
			NullPointerException.class,
			() -> {
				ourOptional.map(null);
			}
		);
	}

	/**
	 * {@code flatMap} should respond with the {@code Present} value processed by
	 * the mapping function
	 */
	@Test void Test17() {
		final Function<Double, OptionalInterface<Integer>> ourMapper = new Function<>() {
			@Override
			public OptionalInterface<Integer> apply(Double t) {
				return Optional.of((int)Math.round(t));
			}
		};

		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		assertEquals(3, ourOptional.flatMap(ourMapper).get());

		final Function<Double, java.util.Optional<Integer>> originalMapper = new Function<>() {
			@Override
			public java.util.Optional<Integer> apply(Double t) {
				return java.util.Optional.of((int)Math.round(t));
			}
		};

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.5D);
		assertEquals(4, originalOptional.flatMap(originalMapper).get());
	}

	/**
	 * {@code flatMap} should respond with a NullPointerException when the
	 * mapping function is null
	 */
	@Test void Test18() {
		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		assertThrows(
			NullPointerException.class,
			() -> {
				ourOptional.flatMap(null);
			}
		);

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.5D);
			assertThrows(
				NullPointerException.class,
				() -> {
					originalOptional.flatMap(null);
				}
			);
	}

	/**
	 * {@code or} should respond with the {@code Present} value
	 */
	@Test void Test19() {
		final Supplier<OptionalInterface<Double>> ourSupplier = new Supplier<>() {
			@Override
			public OptionalInterface<Double> get() {
				return Optional.of(5.0D);
			}
		};

		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		assertTrue(ourOptional.or(ourSupplier).isPresent());
		assertEquals(3.4D, ourOptional.or(ourSupplier).get());

		final Supplier<java.util.Optional<Double>> originalSupplier = new Supplier<>() {
			@Override
			public java.util.Optional<Double> get() {
				return java.util.Optional.of(5.0D);
			}
		};

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.5D);
		assertTrue(originalOptional.or(originalSupplier).isPresent());
		assertEquals(3.5D, originalOptional.or(originalSupplier).get());
	}

	/**
	 * {@code or} should respond with the {@code Present} value when a null
	 * supplier is provided
	 */
	@Test void Test20() {
		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		assertTrue(ourOptional.or(null).isPresent());
		assertEquals(3.4D, ourOptional.or(null).get());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.5D);
		assertThrows(
			NullPointerException.class,
			() -> {
				originalOptional.or(null);
			}
		);
	}

	/**
	 * {@code stream} should return a {@code Stream} with the value for a
	 * {@code Present} instance
	 */
	@Test void Test21() {
		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		final List<Double> ourList = ourOptional.stream().collect(Collectors.toList());
		assertEquals(1, ourList.size());
		assertEquals(3.4, ourList.get(0));

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.5D);
		final List<Double> originalList = originalOptional.stream().collect(Collectors.toList());
		assertEquals(1, originalList.size());
		assertEquals(3.5, originalList.get(0));
	}

	/**
	 * {@code toString} should return "Optional[value]" for a {@code Present}
	 * instance
	 */
	@Test void Test22() {
		final OptionalInterface<Double> ourOptional = Present.of(3.4D);
		assertEquals("Optional[3.4]", ourOptional.toString());

		final java.util.Optional<Double> originalOptional =
			java.util.Optional.of(3.5D);
		assertEquals("Optional[3.5]", originalOptional.toString());
	}

	/**
	 * Present instances should be equal if the value they hold is equal
	 */
	@Test void Test23() {
		final OptionalInterface<Double> ourOptional = Optional.of(3.4D);
		assertEquals(Optional.of(3.4D), ourOptional);

		final java.util.Optional<Double> originalOptional = java.util.Optional.of(3.4D);
		assertEquals(java.util.Optional.of(3.4D), originalOptional);
	}

	/**
	 * Present instances should not be equal if the value they hold is not equal
	 */
	@Test void Test24() {
		final OptionalInterface<Double> ourOptional = Optional.of(3.5D);
		assertNotEquals(Optional.of(3.4D), ourOptional);

		final java.util.Optional<Double> originalOptional = java.util.Optional.of(3.5D);
		assertNotEquals(Optional.of(3.4D), originalOptional);
	}

	/**
	 * Present instances should not be equal to Unknown instances
	 */
	@Test void Test25() {
		final OptionalInterface<Double> ourOptional = Optional.of(3.0D);
		assertNotEquals(Unknown.unknown(), ourOptional);
	}

	/**
	 * Present instances should not be equal to Empty instances
	 */
	@Test void Test26() {
		final OptionalInterface<Double> ourOptional = Optional.of(3.0D);
		assertNotEquals(Empty.empty(), ourOptional);
	}

	/**
	 * Present instances are equal to {@code java.util.Optional} instances
	 * if the value they store is the same. Note: the {@code java.util.Optional}
	 * equals method will not work
	 */
	@Test void Test27() {
		final OptionalInterface<Double> ourOptional = Optional.of(3.0D);
		assertEquals(ourOptional, java.util.Optional.of(3.0D));
	}

	/**
	 * Present instances are not equal to {@code java.util.Optional} instances
	 * if the value they store is not the same. Note: the {@code java.util.Optional}
	 * equals method will not work
	 */
	@Test void Test28() {
		final OptionalInterface<Double> ourOptional = Optional.of(3.0D);
		assertNotEquals(ourOptional, java.util.Optional.of(4.0D));
	}
}