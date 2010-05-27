package com.porpoise.ga;

public enum Scores {
    ;// uninstantiable

    public static class ComparableScore<T extends Comparable<T>> implements IScore<T> {
        private final T value;
        private Boolean isComplete;
        private T       targetValue;
        private Boolean isValid;

        public ComparableScore<T> setTarget(final T target) {
            targetValue = target;
            return this;
        }

        public ComparableScore<T> setValid(final boolean valid) {
            isValid = Boolean.valueOf(valid);
            return this;
        }

        public ComparableScore<T> setComplete(final boolean complete) {
            isComplete = Boolean.valueOf(complete);
            return this;
        }

        public ComparableScore(final T valueParam) {
            this.value = valueParam;
        }

        @Override
        public boolean isComplete() {
            if (isComplete != null) {
                return isComplete.booleanValue();
            }
            if (targetValue != null && value != null) {
                return targetValue.compareTo(value) == 0;
            }
            return false;
        }

        @Override
        public boolean isValid() {
            if (isValid != null) {
                return isValid.booleanValue();
            }
            return value != null;
        }

        @Override
        public int compareTo(final IScore<T> other) {
            if (value == null) {
                return other.getValue() == null ? 0 : 1;
            }
            if (other.getValue() == null) {
                return -1;
            }
            return value.compareTo(other.getValue());
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    /**
     * @param value
     * @return a score for the given int value
     */
    public static ComparableScore<Integer> valueOf(final int value) {
        return new ComparableScore<Integer>(Integer.valueOf(value));
    }

    public static ComparableScore<Float> valueOf(final float value) {
        return new ComparableScore<Float>(Float.valueOf(value));
    }

    public static ComparableScore<Double> valueOf(final double value) {
        return new ComparableScore<Double>(Double.valueOf(value));
    }

    public static <T extends Comparable<T>> ComparableScore<T> valueOf(final T value) {
        return new ComparableScore<T>(value);
    }

    /**
     * @param value
     * @return a score for the given int value
     */
    public static IScore<Integer> invalidInt() {
        return new ComparableScore<Integer>(null);
    }

}
