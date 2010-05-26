package com.porpoise.ga;

public interface IScore<T> extends Comparable<IScore<T>> {

    boolean isComplete();

    boolean isValid();

    T getValue();

}
