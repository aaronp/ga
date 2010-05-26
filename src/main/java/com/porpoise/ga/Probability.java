package com.porpoise.ga;

import java.util.Random;

import com.porpoise.common.Proportion;

public class Probability {

    private static Probability instance;

    private static final float DEFAULT_MUTATION  = .0015F;
    private static final float DEFAULT_CROSSOVER = .7F;

    public static final float  ALWAYS            = 1.0F;
    public static final float  NEVER             = 0.0F;

    private final float        crossProbability;
    private final float        mutateProbability;
    private final Random       rand              = new Random();

    public Probability(final float cross, final float mutate) {
        crossProbability = cross;
        mutateProbability = mutate;
        assert cross >= NEVER;
        assert mutate >= NEVER;
        assert cross <= ALWAYS;
        assert mutate <= ALWAYS;
    }

    public boolean nextCross() {
        return check(crossProbability);
    }

    /**
     * @param probability
     * @return
     */
    private boolean check(final float probability) {
        return rand.nextFloat() < probability;
    }

    public boolean nextMutate() {
        return check(mutateProbability);
    }

    public static Probability init(final float cross, final float mutate) {
        if (instance != null) {
            throw new IllegalStateException("already initialised");
        }
        instance = new Probability(cross, mutate);
        return instance;
    }

    public static Probability getInstance() {
        if (instance == null) {
            instance = init(DEFAULT_CROSSOVER, DEFAULT_MUTATION);
        }
        return instance;
    }

    public int nextInt(final int size) {
        return rand.nextInt(size);
    }

    public float nextFloat() {
        return rand.nextFloat();
    }

    public static Probability alwaysCrossNeverMutate() {
        return new Probability(Probability.ALWAYS, Probability.NEVER);
    }

    public static Probability neverCrossNeverMutate() {
        return new Probability(Probability.NEVER, Probability.NEVER);
    }

    public static Probability neverCrossAlwaysMutate() {
        return new Probability(Probability.NEVER, Probability.ALWAYS);
    }

    public static Probability alwaysCrossAlwaysMutate() {
        return new Probability(Probability.ALWAYS, Probability.ALWAYS);
    }

    @Override
    public String toString() {
        return String.format("cross change=%.2f%%, mutatbility change=%.2f%%", crossProbability * 100,
                mutateProbability * 100);
    }

    public Proportion getDefaultPoolProportion() {
        // top 30% 50% of the time, next 40% 40% of the time and the last 30%
        return Proportion.with(30, 50).and(40, 40).build();
    }
}
