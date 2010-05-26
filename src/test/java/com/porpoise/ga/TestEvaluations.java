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

    public static IGeneEvaluation increasing() {
        return new IGeneEvaluation() {
            float next = 0.0F;

            @Override
            public float score(final GeneSequence sequence) {
                next = next + 1.0F;
                return next;
            }
        };
    }

}
