package com.porpoise.ga;

public enum Scores
{
    ;// uninstantiable

    public static class ComparableScore<T extends Comparable<T>> implements IScore<T>
    {
        private final T value;
        private Boolean isComplete;
        private T       targetValue;
        private Boolean isValid;

        public ComparableScore<T> setTarget(final T target)
        {
            this.targetValue = target;
            return this;
        }

        public ComparableScore<T> setValid(final boolean valid)
        {
            this.isValid = Boolean.valueOf(valid);
            return this;
        }

        public ComparableScore<T> setComplete(final boolean complete)
        {
            this.isComplete = Boolean.valueOf(complete);
            return this;
        }

        public ComparableScore(final T valueParam)
        {
            this.value = valueParam;
        }

        @Override
        public boolean isComplete()
        {
            if (this.isComplete != null)
            {
                return this.isComplete.booleanValue();
            }
            if (this.targetValue != null && this.value != null)
            {
                return this.targetValue.compareTo(this.value) == 0;
            }
            return false;
        }

        @Override
        public boolean isValid()
        {
            if (this.isValid != null)
            {
                return this.isValid.booleanValue();
            }
            return this.value != null;
        }

        @Override
        public int compareTo(final IScore<T> other)
        {
            if (this.value == null)
            {
                return other.getValue() == null ? 0 : 1;
            }
            if (other.getValue() == null)
            {
                return -1;
            }
            return this.value.compareTo(other.getValue());
        }

        @Override
        public T getValue()
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            return this.value == null ? "null" : this.value.toString();
        }
    }

    /**
     * @return a score for the given int value
     */
    public static ComparableScore<Integer> valueOf(final int value)
    {
        return new ComparableScore<Integer>(Integer.valueOf(value));
    }

    public static ComparableScore<Float> valueOf(final float value)
    {
        return new ComparableScore<Float>(Float.valueOf(value));
    }

    public static ComparableScore<Double> valueOf(final double value)
    {
        return new ComparableScore<Double>(Double.valueOf(value));
    }

    public static <T extends Comparable<T>> ComparableScore<T> valueOf(final T value)
    {
        return new ComparableScore<T>(value);
    }

    /**
     * @return a score for the given int value
     */
    public static IScore<Integer> invalidInt()
    {
        return new ComparableScore<Integer>(null);
    }

}
