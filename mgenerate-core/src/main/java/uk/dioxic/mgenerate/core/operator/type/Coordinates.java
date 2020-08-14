package uk.dioxic.mgenerate.core.operator.type;

import java.util.List;

import static java.util.Arrays.asList;

public class Coordinates {
        private final double x;
        private final double y;
        private boolean active = true;

        public Coordinates(List<Double> coordinates) {
            this(coordinates.get(0), coordinates.get(1));
        }

        public Coordinates(final double x, final double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        /**
         * Euclidean distance.
         * tour wraps around N-1 to 0.
         */
        public static double distance(final Coordinates[] points) {
            final int len = points.length;
            double d = points[len - 1].distance(points[0]);
            for (int i = 1; i < len; i++)
                d += points[i - 1].distance(points[i]);
            return d;
        }

        /**
         * Euclidean distance.
         */
        public final double distance(final Coordinates to) {
            return Math.sqrt(_distance(to));
        }

        /**
         * compare 2 points.
         * no need to square when comparing.
         * http://en.wikibooks.org/wiki/Algorithms/Distance_approximations
         */
        public final double _distance(final Coordinates to) {
            final double dx = this.x - to.x;
            final double dy = this.y - to.y;
            return (dx * dx) + (dy * dy);
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(final boolean active) {
            this.active = active;
        }

        public List<Double> toList() {
            return asList(x,y);
        }

        public String toString() {
            return "[" + x + "," + y + "]";
        }
}
