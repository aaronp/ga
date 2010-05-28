package com.porpoise.ga;

public class GeneImpl<T> implements IGene<T> {

    private final int          index;
    private final T            data;
    private final IGenotype<T> genotype;

    public GeneImpl(final IGenotype<T> type, final int geneIndex, final T value) {
        this.genotype = type;
        this.index = geneIndex;
        this.data = value;
    }

    public GeneImpl(final GeneImpl<T> other) {
        this(other.genotype, other.index, other.data);
    }

    @Override
    public IGenotype<T> getType() {
        return genotype;
    }

    @Override
    public T getValue() {
        return data;
    }

    @Override
    public IGene<T> mutate(final float random) {
        return genotype.createGene(index);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    /**
     * @return the index
     */
    public int getPosition() {
        return this.index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.data == null ? 0 : this.data.hashCode());
        result = prime * result + (this.genotype == null ? 0 : this.genotype.hashCode());
        result = prime * result + this.index;
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GeneImpl<?> other = (GeneImpl<?>) obj;
        if (this.data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!this.data.equals(other.data)) {
            return false;
        }
        if (this.genotype == null) {
            if (other.genotype != null) {
                return false;
            }
        } else if (!this.genotype.equals(other.genotype)) {
            return false;
        }
        if (this.index != other.index) {
            return false;
        }
        return true;
    }

    @Override
    public IGene<T> copy() {
        return new GeneImpl<T>(this);
    }
}
