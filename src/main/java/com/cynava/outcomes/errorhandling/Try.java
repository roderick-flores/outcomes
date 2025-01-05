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

import com.cynava.outcomes.optional.Optional;

/**
 * Outcome of a computation that may either result in a Throwable or
 * a successfully computed value
 * 
 * @param <T> Type of object that might be stored in the Try outcome
 * @version 1.1
 * Version history:
 *    1.0 original version
 *    1.1 removed static methods
 */
public abstract class Try<T> implements TryInterface<T> {
	private static final long serialVersionUID = 991774995L;

	/**
	 * Constructs a Try
	 */
	protected Try() {}

	@Override
	public Optional<T> toOptional() {
		try {
			return Optional.of(get());
		} catch( Throwable e ) {
			// return an empty when the Try is a Failure
			return Optional.empty();
		}
	}
}
