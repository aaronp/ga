package com.porpoise.ga;

import java.util.Random;

public class Probability {

    private static Probability instance;

    private final float        crossProbability;
    private final float        mutateProbability;
    private final Random       rand = new Random();

    public Probability(final float cross, final float mutate) {
        crossProbability = cross;
        mutateProbability = mutate;
        assert cross >= 0.0;
        assert mutate >= 0.0;
        assert cross <= 1.0;
        assert mutate <= 1.0;
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
            instance = init(.7F, .0015F);
        }
        return instance;
    }

    public int nextInt(final int size) {
        return rand.nextInt(size);
    }

    public float nextFloat() {
        return rand.nextFloat();
    }

}
