package com.porpoise.ga;

/**
 * Represents the value of a sequence. Better scores should come first. If, for example, a score was simply an integer
 * value, then the score of 0 would be better than one of 7.
 * 
 * @param <T>
 */
public interface IScore<T> extends Comparable<IScore<T>> {

    /**
     * @return true if the score is the target score - the top score we're after
     */
    boolean isComplete();

    /**
     * @return true if the score is relevant
     */
    boolean isValid();

    /**
     * @return the score's value
     */
    T getValue();

}
