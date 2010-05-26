package com.porpoise.ga;

public interface IGeneEvaluation {

    /**
     * convert a given sequence into a score, the lower being better.
     * 
     * Zero is the best score, which will be considered the correct solution.
     * 
     * The range (scope) does not matter, as the result will just be compared with each other.
     * 
     * @param sequence
     * @return a score, 0.0F being the best score
     */
    public float score(GeneSequence sequence);

}
