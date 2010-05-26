package com.porpoise.ga.impl;

/**
 * wrapper for our data type
 * 
 * @param <T>
 */
public class GeneData<T> {

    private final T   value;
    private final int index;

    public GeneData(final int index, final T value) {
        this.index = index;
        this.value = value;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return this.index;
    }

    @Override
    public String toString() {
        return String.format("[%d]=>%s", Integer.valueOf(index), value);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        final GeneData<T> other = (GeneData<T>) obj;
        if (this.index != other.index) {
            return false;
        }
        return true;
    }
}
