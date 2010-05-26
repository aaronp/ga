package com.porpoise.impl;

import java.util.Random;

import com.porpoise.IGene;

class GeneImpl<T> implements IGene<GeneData<T>> {

    private final GeneSequence<GeneData<T>> pool;
    private final GeneData<T>           data;
    private final Random                rand = new Random();

    GeneImpl(final GeneSequence<GeneData<T>> poolValue, final GeneData<T> dataValue) {
        pool = poolValue;
        data = dataValue;
        assert data != null;
    }

    @Override
    public IGene<GeneData<T>> cross(final IGene<GeneData<T>> gene) {
        gene.getType();
        return null;
    }

    @Override
    public GeneData<T> getType() {
        return data;
    }

    @Override
    public IGene<GeneData<T>> mutate(final float random) {
        final int next = (int) (100 * random);
        final int index = prepareIndex(ordinal() + next);
        return getSafe(index);
    }

    /**
     * @param index
     * @return
     */
    private IGene<GeneData<T>> getSafe(final int index) {
        return pool.get(index % size());
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
            //
            index = index + rand.nextInt(size);
            index = index % size;
        }
        assert index != ordinal();
        return index;
    }

    private int ordinal() {
        return data.getIndex();
    }

    /**
     * @return
     */
    private int size() {
        return pool.size();
    }
}
