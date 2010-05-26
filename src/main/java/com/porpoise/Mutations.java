package com.porpoise;

import java.util.Random;

class Mutations {
    private final static IMutator NONE = new IMutator() {
                                            @Override
                                            public void mutate(final IGene<?> gene) {
                                                // don't do anything
                                            }
                                        };

    final static class MutationImpl implements IMutator {
        private final float zeroToOne;

        public MutationImpl(final float randomValue) {
            zeroToOne = randomValue;
            assert randomValue >= 0.0;
            assert randomValue <= 1.0;
        }

        @Override
        public void mutate(final IGene<?> gene) {
            gene.mutate(zeroToOne);
        }
    }

    private final Random rand = new Random();
    private final float  probability;

    public Mutations(final float mutationProbability) {
        probability = mutationProbability;

        if (mutationProbability < 0 || mutationProbability > 1.0) {
            throw new NullPointerException("mutation probability must be between 0 and 1.0");
        }
    }

    public static IMutator none() {
        return NONE;
    }

    public IMutator next() {
        final float random = rand.nextFloat();
        if (random < probability) {
            return new MutationImpl(random);
        }
        return none();
    }

}
