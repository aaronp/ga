package com.porpoise;

public interface IGene<T> {

    T getType();

    IGene<T> cross(IGene<T> gene);

    IGene<T> mutate(float random);
}
