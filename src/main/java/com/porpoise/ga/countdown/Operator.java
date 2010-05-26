package com.porpoise.ga.countdown;

import java.util.Arrays;
import java.util.Iterator;

import com.porpoise.common.Iterators;
import com.porpoise.ga.IGene;

public enum Operator implements IGene<Operator> {

    PLUS("+"), //
    DIVIDE("/"), //
    MULTIPLY("X"), //
    MINUS("-");//

    @SuppressWarnings("boxing")
    private static Iterator<Integer> nextJump = Iterators.cycle(Arrays.asList(1, 2, 3));

    private final String             operator;

    private Operator(final String op) {
        operator = op;
    }

    @Override
    public String toString() {
        return operator;
    }

    @Override
    public IGene<Operator> cross(final IGene<Operator> gene) {
        final int index = ordinal() + gene.getType().ordinal() * 7;
        return getSafe(index);
    }

    /**
     * @param index
     * @return
     */
    private static Operator getSafe(final int index) {
        return Operator.values()[index % size()];
    }

    /**
     * @param sum
     * @return a different index than this ordinal
     */
    private int prepareIndex(final int sum) {
        final int size = size();

        int index = sum % size;
        if (index == ordinal()) {
            //
            // we ended up where we started.
            // we could just guess randomly or ALWAYS go to the next one up.
            // instead we cycle, always going 1, 2 or three away from where we are
            //
            index = index + nextJump.next().intValue();
            index = index % size;
        }
        assert index != ordinal();
        return index;
    }

    /**
     * @return
     */
    private static int size() {
        return Operator.values().length;
    }

    @Override
    public Operator getType() {
        return this;
    }

    @Override
    public IGene<Operator> mutate(final float random) {
        final int next = (int) (100 * random);
        final int index = prepareIndex(ordinal() + next);
        return getSafe(index);
    }
}
