package com.porpoise.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * class which represents the likelihood of different proportions
 */
public final class Proportion {
    private static final double ONE_HUNDRED_PERCENT = 100.0;

    private final Random        random              = new Random();
    private final List<Slice>   slices;

    /**
     * builder used to create proportions
     */
    public static class Builder {
        private final List<Slice> slices;

        private Builder(final double sizePercentage, final double likelihoodPercentage) {
            slices = new ArrayList<Slice>();
            slices.add(new Slice(sizePercentage, likelihoodPercentage));
        }

        public Builder and(final int sizePercentage, final int likelihoodPercentage) {
            return and(sizePercentage / ONE_HUNDRED_PERCENT, likelihoodPercentage / ONE_HUNDRED_PERCENT);
        }

        public Builder and(final double sizePercentage, final double likelihoodPercentage) {
            slices.add(new Slice(sizePercentage, likelihoodPercentage));
            return this;
        }

        public Proportion build() {
            return new Proportion(slices);
        }
    }

    /**
     * A slice is a single proportion
     */
    public static class Slice {
        private final double sizePercentage;
        private final double likelihoodPercentage;

        public Slice(final double size, final double likelihood) {
            this.sizePercentage = size;
            this.likelihoodPercentage = likelihood;
            if (size > 1.0 || size < 0.0) {
                throw new IllegalArgumentException("size percentage must be between 0.0 and 1.0: " + size);
            }
            if (likelihood > 1.0 || likelihood < 0.0) {
                throw new IllegalArgumentException("likelihood percentage must be between 0.0 and 1.0: "
                        + likelihood);
            }
        }

        public double getSizePercentage() {
            return sizePercentage;
        }

        public double getLikelihoodPercentage() {
            return likelihoodPercentage;
        }

        @Override
        public String toString() {
            return String.format("%.2f%% size @ %.2f%% likelihood", Double.valueOf(sizePercentage * 100), Double
                    .valueOf(likelihoodPercentage * 100));
        }
    }

    /**
     * factory method
     * 
     * @param sizePercentage
     * @param likelihoodPercentage
     * @return
     */
    @SuppressWarnings("synthetic-access")
    public static Builder with(final double sizePercentage, final double likelihoodPercentage) {
        return new Builder(sizePercentage, likelihoodPercentage);
    }

    /**
     * factory method
     * 
     * @param sizePercentage
     * @param likelihoodPercentage
     * @return
     */
    public static Builder with(final int sizePercentage, final int likelihoodPercentage) {

        return with(sizePercentage / ONE_HUNDRED_PERCENT, likelihoodPercentage / ONE_HUNDRED_PERCENT);
    }

    Proportion(final List<Slice> slicesValues) {
        final List<Slice> temp = Lists.newArrayList(slicesValues);
        double totalSize = 0.0;
        double totalLikelihood = 0.0;
        for (final Slice slice : temp) {
            totalSize += slice.getSizePercentage();
            totalLikelihood += slice.getLikelihoodPercentage();
        }
        if (totalSize > 1.0) {
            throw new IllegalStateException("Invalid total size - proportions add up to exceed 1.0 (100%) :"
                    + totalSize);
        }
        if (totalLikelihood > 1.0) {
            throw new IllegalStateException("Invalid total likelihood - proportions add up to exceed 1.0 (100%) :"
                    + totalLikelihood);
        }

        // add the last little bit to make up the difference
        if (totalSize < 1.0 || totalLikelihood < 1.0) {
            temp.add(new Slice(1.0 - totalSize, 1.0 - totalLikelihood));
        }
        slices = Collections.unmodifiableList(temp);
    }

    /**
     * <p>
     * Given this proportional representation, return a number from the given range, considering the low numbers the
     * 'top' of the range.
     * </p>
     * <p>
     * For example, say this proportion says in essence:
     * <ol>
     * <li>70% of the time, choose a number from the top 20% of the range</li>
     * <li>25% of the time, choose a number from the middle 10% of the range</li>
     * <li>5% of the time, choose a number from the remaining 70% of the range</li>
     * </ol>
     * 
     * The 'ascending' bit of {@link #chooseAscending(int)} means that 0 is considered the 'top', and 'range' is
     * considered the bottom
     * </p>
     * 
     * @param range
     *            The range from which a number will be returned, exclusive
     * @return a number from the given range, exclusive, based on the this proportions likelihood
     */
    public int chooseAscending(final int range) {
        final double location = random.nextDouble();
        return chooseAscending(location, range);
    }

    /**
     * see {@link #chooseAscending(int)}, but consider the higher numbers (e.g. 'range') the top of the range
     * 
     * @param range
     * @return a number based on the given range, exclusive
     */
    public int chooseDescending(final int range) {
        final double location = random.nextDouble();
        return chooseDescending(location, range);
    }

    /**
     * @param range
     * @param location
     * @return
     */
    final int chooseDescending(final double location, final int range) {
        int descending = range - chooseAscending(location, range);
        if (descending < 0) {
            descending = 0;
        }
        if (descending >= range) {
            descending = range - 1;
        }
        return descending;
    }

    final int chooseAscending(final double location, final int range) {
        double cummulativeSize = 0.0;
        double size = 0.0;
        double totalLikelihood = 0.0;
        for (final Slice slice : slices) {
            totalLikelihood += slice.getLikelihoodPercentage();
            if (location <= totalLikelihood) {
                size = slice.getSizePercentage();
                break;
            }
            cummulativeSize += slice.getSizePercentage();
        }
        assert size != 0.0;

        // right - we're returning a value from (e.g. if we have .20 and .55, then
        // we return a number between the 20% and 55% of the range, inclusive. If the range value was 200, then we
        // return a number between 40 and 110) 
        final int offset = (int) (cummulativeSize * range);
        final int rangeSize = (int) (size * range);

        final int randValue = random.nextInt(rangeSize == 0 ? 1 : rangeSize);
        int next = offset + randValue;
        // System.out.println(String.format("offset=%s, size=%s, range=%s, rangeSize=%s, randValue=%s, next=%s", offset,
        // size, range, rangeSize, randValue, next));
        if (next >= range) {
            next = range - 1;
        }
        return next;
    }

    @Override
    public String toString() {
        return toString(String.format("%n"));
    }

    public String toString(final String separator) {
        return Joiner.on(separator).join(slices);
    }
}
