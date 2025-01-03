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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.function.Predicate;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.jupiter.api.Test;

/**
 * Acceptance tests for the Optionals using a {@code Comparable}
 * 
 * @version 1.0
 * Version history:
 *    1.0 original version
 */
public class StreamTest {
	/**
	 * Simple class to test streaming Optionals
	 */
	class Point implements Comparable<Point> {
		public final double x;
		public final double y;
		public final double error;

		public Point(double x, double y, double error) {
			this.x = x;
			this.y = y;
			this.error = error;
		}

		@Override
		public int compareTo(Point o) {
			return ((Double)Math.abs(error)).compareTo((Double)Math.abs(o.error));
		}

		@Override
		public String toString() {
			return "x: " + x + ", y: " + y + ", error: " + error;
		}
	}

	@Test void Test01() {
		final TriangularDistribution noiseDistribution =
			new TriangularDistribution(new Well19937c(101L), -.250D, 0.0D, 0.250D);

		final UniformRealDistribution xDistribution =
			new UniformRealDistribution(new Well19937c(102L), -20.0D, 20.0D);

		final UniformRealDistribution missingDistribution =
			new UniformRealDistribution(new Well19937c(103L), 0.0D, 40.0D);

		// generate random points
		final ArrayList<Optional<Point>> points = new ArrayList<>();
		final double slope = 1.0D;
		final double intercept = 1.0D;
		for( int i = 0; i < 120; i++ ) {
			final double missingDraw = missingDistribution.sample();
			if( missingDraw < 1.0D) {
				points.add(Optional.empty());
				continue;
			} else if(missingDraw < 2.0D) {
				points.add(Optional.unknown());
				continue;
			}

			final double noise = noiseDistribution.sample();

			final double x = xDistribution.sample();
			final double y = slope * x + intercept;
			final double noisyY = noise * y;

			final double error = y - noisyY;
			points.add(Optional.of(new Point(x, noisyY, error)));
		}

		// filter of values that are not present
		final Predicate<Optional<Point>> pointPredicate = 
			new Predicate<>() {
				@Override
				public boolean test(Optional<Point> t) {
					return t.isPresent();
				}
			};

		// find the point with minimum error in the points
		final Optional<Point> streamMin =
			Optional.of(
				points.stream().filter(pointPredicate).map(v -> v.get()).min(Point::compareTo)
			);
		assertTrue(Math.abs(-0.8576967711636208D - streamMin.get().x) < 1E-07);
		assertTrue(Math.abs(0.005565288809027444D - streamMin.get().y) < 1E-07);
		assertTrue(Math.abs(0.1367379400273518D - streamMin.get().error) < 1E-07);
	
		// find the point with maximum error in the points
		final Optional<Point> streamMax =
			Optional.of(
				points.stream().filter(pointPredicate).map(v -> v.get()).max(Point::compareTo)
			);
		assertTrue(Math.abs(18.973451266832228D - streamMax.get().x) < 1E-07);
		assertTrue(Math.abs(-3.304647570913039D - streamMax.get().y) < 1E-07);
		assertTrue(Math.abs(23.27809883774527D - streamMax.get().error) < 1E-07);
	}
}