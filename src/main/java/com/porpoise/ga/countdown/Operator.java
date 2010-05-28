package com.porpoise.ga.countdown;

public enum Operator
{

    PLUS("+")
    {
        @Override
        public int apply(final int valueOne, final int valueTwo)
        {
            return valueOne + valueTwo;
        }
    }, //
    DIVIDE("/")
    {
        @Override
        public int apply(final int valueOne, final int valueTwo)
        {
            return valueOne / valueTwo;
        }

        @Override
        public boolean canApply(final int valueOne, final int valueTwo)
        {
            final boolean divByZero = valueTwo == 0;
            if (divByZero)
            {
                return false;
            }

            return apply(valueOne, valueTwo) * valueTwo == valueOne;
        }
    }, //
    MULTIPLY("X")
    {
        @Override
        public int apply(final int valueOne, final int valueTwo)
        {
            return valueOne * valueTwo;
        }
    }, //
    MINUS("-")
    {
        @Override
        public int apply(final int valueOne, final int valueTwo)
        {
            return valueOne - valueTwo;
        }
    }; //

    private final String operator;

    private Operator(final String op)
    {
        this.operator = op;
    }

    @Override
    public String toString()
    {
        return this.operator;
    }

    public abstract int apply(int valueOne, int valueTwo);

    /**
     * @param valueOne
     * @param valueTwo
     * @return true if the operator can be applied to the given input values
     */
    public boolean canApply(final int valueOne, final int valueTwo)
    {
        return true;
    }

}
