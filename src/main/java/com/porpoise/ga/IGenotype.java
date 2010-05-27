package com.porpoise.ga;

public interface IGenotype<T> {

    public int hashCode();

    public boolean equals(Object obj);

    public IGene<T> createGene(int index);
}
