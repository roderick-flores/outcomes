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
package com.cynava.outcomes.worm;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cynava.outcomes.optional.Optional;

/**
 * Write once, read many (WORM) value built around an
 * {@code OptionalInterface}. The value can be set to a value,
 * unknown, or empty.
 * 
 * @param <T> Type of value in the WORM
 * @see com.cynava.outcomes.optional.Optional
 */
public final class WormValue<T> {
	/** Value in the WORM */
	private Optional<T> value;
	/** lock used to synchronize threads */
	private final ReadWriteLock lock;
	/** read lock used to synchronize threads */
	private final Lock rLock;
	/** write lock used to synchronize threads */
	private final Lock wLock;

	/**
	 * Constructs a WormValue instance with no value set
	 */
	private WormValue() {
		lock = new ReentrantReadWriteLock();
		rLock = lock.readLock();
		wLock = lock.writeLock();
		value = Optional.empty();
	}

	/**
	 * Sets the value of the worm. Null values are not acceptable
	 * 
	 * @param value Value to set in the worm
	 */
	public void set(T value) {
		if(value == null) {
			throw new IllegalArgumentException("value cannot be null");
		}

		// block so that only one thread can be setting at a time
		wLock.lock();

		// synchronize over the write-once value so that reads are
		// blocked
		synchronized( this.value ) {
			// if the value is not empty then it cannot be set
			if( this.value.isEmpty() ) {
				this.value = Optional.of(value);
			} else {
				// let the next thread in so it can fail
				wLock.unlock();
				throw new UnsupportedOperationException("value is already set");
			}
		}
	}

	/**
	 * Indicate that the worm value cannot be determined
	 */
	public void setUnknown() {
		// block so that only one thread can be setting at a time
		wLock.lock();

		// synchronize over the write-once value so that reads are
		// blocked
		synchronized( this.value ) {
			// if the value is not empty then it cannot be set
			if( this.value.isEmpty() ) {
				this.value = Optional.unknown();
			} else {
				// let the next thread in so it can fail
				wLock.unlock();
				throw new UnsupportedOperationException("value is already set to unknown");
			}
		}
	}

	/**
	 * Retrieves the value as an {@code Optional}
	 * 
	 * @return {@code T} Value
	 */
	public Optional<T> get() {
		rLock.lock();
		try {
			return value;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Determines if the value is set, including unknown
	 * 
	 * @return {@code True} if the value is present or {@code False}
	 *         otherwise
	 */
	public boolean ifPresent() {
		rLock.lock();
		try {
			return value.isPresent() || value.isUnknown();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Determines if the value is set and known
	 * 
	 * @return {@code True} if the value is known or {@code False}
	 *         otherwise
	 */
	public boolean isSet() {
		rLock.lock();
		try {
			return value.isPresent();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Determines if the value is unknown
	 * 
	 * @return {@code True} if the value is unknown or {@code False}
	 *         otherwise
	 */
	public boolean isUnknown() {
		rLock.lock();
		try {
			return value.isUnknown();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Creates a Worm with a set value
	 * 
	 * @param <T> Type of value stored in the worm
	 * @param value Value to set in the worm
	 * @return {@code Worm<T>} instance
	 */
	public static <T> WormValue<T> of(final T value) {
		final WormValue<T> worm = new WormValue<>();
		worm.set(value);
		return worm;
	}

	/**
	 * Creates a Worm with an unknown value
	 * 
	 * @param <T> Type of value stored in the worm
	 * @return {@code Worm<T>} instance
	 */
	public static <T> WormValue<T> unknown() {
		final WormValue<T> worm = new WormValue<>();
		worm.setUnknown();
		return worm;
	}

	/**
	 * Creates a Worm without a value
	 * 
	 * @param <T> Type of value stored in the worm
	 * @return {@code Worm<T>} instance
	 */
	public static <T> WormValue<T> create() {
		return new WormValue<>();
	}
}
