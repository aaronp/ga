package com.porpoise.ga;


class GeneImpl<T> implements IGene<T> {

    private final int          index;
    private final T            data;
    private final GeneSequence sequence;

    GeneImpl(final GeneSequence sequence, final int index, final T value) {
        this.sequence = sequence;
        this.index = index;
        this.data = value;
    }

    @Override
    public IGene<T> cross(final IGene<T> gene) {
        gene.getType();
        return null;
    }

    @Override
    public T getType() {
        return data;
    }

    @Override
    public IGene<T> mutate(final float random) {
        final int next = (int) (100 * random);
        final int index = prepareIndex(ordinal() + next);
        return getSafe(index);
    }

    /**
     * @param index
     * @return
     */
    private IGene<T> getSafe(final int index) {
        return (IGene<T>) sequence.getGene(index % size());
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
            index = index + Probability.getInstance().nextInt(size);
            index = index % size;
        }
        assert index != ordinal();
        return index;
    }

    private int ordinal() {
        return index;
    }

    /**
     * @return
     */
    private int size() {
        return sequence.size();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
