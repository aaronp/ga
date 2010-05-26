package com.porpoise.ga;

public interface IGene<T> {

    T getValue();

    IGene<T> mutate(float random);
}
