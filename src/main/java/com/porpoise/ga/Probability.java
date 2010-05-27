package com.porpoise.ga;

import java.util.Random;

import com.porpoise.common.Proportion;

public class Probability {

    private static Probability instance;

    public static final float  DEFAULT_MUTATION  = .0015F;
    public static final float  DEFAULT_CROSSOVER = .7F;

    public static final float  ALWAYS            = 1.0F;
    public static final float  NEVER             = 0.0F;

    private final float        minTrimPercentage = 0.2F;
    private final float        maxTrimPercentage = 0.5F;

    private final float        crossProbability;
    private final float        mutateProbability;
    private final Random       rand              = new Random();

    private final Proportion   defaultPoolProportion;

    public Probability(final float cross, final float mutate) {
        this(cross, mutate, Proportion.with(30, 50).and(40, 40).build());
    }

    public Probability(final float cross, final float mutate, final Proportion genePoolProportion) {
        crossProbability = cross;
        mutateProbability = mutate;
        defaultPoolProportion = genePoolProportion;

        if (defaultPoolProportion == null) {
            throw new NullPointerException("defaultPoolProportion");
        }

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
        return String.format("cross change=%.2f%%, mutatbility change=%.2f%%", Float.valueOf(crossProbability * 100),
                Float.valueOf(mutateProbability * 100));
    }

    public Proportion getPoolProportion() {
        return defaultPoolProportion;
    }

    public int nextNumberToThin(final int size) {
        final float minPercent = minTrimPercentage;
        final float maxPercent = maxTrimPercentage;
        return nextNumberToThin(minPercent, maxPercent, size);
    }

    /**
     * @param minPercent
     * @param maxPercent
     * @param size
     * @return
     */
    public int nextNumberToThin(final float minPercent, final float maxPercent, final int size) {
        if (minPercent < 0.0F || minPercent > 1.0F) {
            throw new IllegalArgumentException("min must be between 0.0 and 1.0: " + minPercent);
        }
        if (maxPercent < 0.0F || maxPercent > 1.0F) {
            throw new IllegalArgumentException("max must be between 0.0 and 1.0: " + maxPercent);
        }
        final float percentage = rand.nextFloat() * (maxPercent - minPercent);

        final int result = (int) ((minPercent + percentage) * size);
        return result;
    }
}
