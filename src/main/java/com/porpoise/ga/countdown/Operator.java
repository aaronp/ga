package com.porpoise.ga.countdown;

public enum Operator {

    PLUS("+") {
        @Override
        public int apply(final int valueOne, final int valueTwo) {
            return valueOne + valueTwo;
        }
    }, //
    DIVIDE("/") {
        @Override
        public int apply(final int valueOne, final int valueTwo) {
            return valueOne / valueTwo;
        }

        @Override
        public boolean canApply(final int valueOne, final int valueTwo) {
            final boolean divByZero = valueTwo == 0;
            if (divByZero) {
                return false;
            }

            return apply(valueOne, valueTwo) * valueTwo == valueOne;
        }
    }, //
    MULTIPLY("X") {
        @Override
        public int apply(final int valueOne, final int valueTwo) {
            return valueOne * valueTwo;
        }
    }, //
    MINUS("-") {
        @Override
        public int apply(final int valueOne, final int valueTwo) {
            return valueOne - valueTwo;
        }
    }; //

    private final String operator;

    private Operator(final String op) {
        operator = op;
    }

    @Override
    public String toString() {
        return operator;
    }

    public abstract int apply(int valueOne, int valueTwo);

    public boolean canApply(final int valueOne, final int valueTwo) {
        return true;
    }

}
