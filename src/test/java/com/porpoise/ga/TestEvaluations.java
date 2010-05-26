package com.porpoise.ga;

public enum TestEvaluations {
    ;

    /**
     * @param result
     * @return an evaluation instance which *always* returns the given rating
     */
    public static IGeneEvaluation constant(final float result) {
        return new IGeneEvaluation() {
            @Override
            public float score(final GeneSequence sequence) {
                return result;
            }
        };
    }

}
