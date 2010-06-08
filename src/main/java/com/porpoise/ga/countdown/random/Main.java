package com.porpoise.ga.countdown.random;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.porpoise.ga.countdown.Operator;

/**
 * An attempt to solve the 'countdown' problem using just random guesses (no evolution)
 */
public class Main
{
    private static class Equation
    {
        private static final int          INVALID = Integer.MIN_VALUE;
        private final int[]               numbers;
        private final Operator[]          operators;
        private static Iterator<Operator> ops     = Iterators.cycle(Operator.values());
        private static final Random       rand    = new Random();

        public Equation(final int... ints)
        {
            this.numbers = Arrays.copyOf(ints, ints.length);
            this.operators = new Operator[this.numbers.length - 1];
            for (int i = 0; i < this.operators.length; i++)
            {
                this.operators[i] = nextOp();
            }
            validate();
        }

        private static Operator nextOp()
        {
            // final int s = rand.nextInt(Operator.values().length);
            // for (int i = 0; i < s; i++)
            // {
            // ops.next();
            // }
            return ops.next();
        }

        public boolean isEqual(final int val)
        {
            final int total = getTotal();
            return total == val;
        }

        public int getTotal()
        {
            validate();
            final Operator o = this.operators[0];
            int total = this.numbers[0];
            for (int i = 1; i < this.numbers.length; i++)
            {
                final int x = this.numbers[i];
                if (!o.canApply(total, x))
                {
                    return INVALID;
                }
                total = o.apply(total, x);
            }
            return total;
        }

        private void validate()
        {
            if (this.numbers.length < 2)
            {
                throw new IllegalArgumentException("less than 2 numbers!");
            }
            if (this.operators.length != this.numbers.length - 1)
            {
                throw new IllegalArgumentException(String.format("%d operators and %d numbers", this.operators.length, this.numbers.length));
            }

            final Set<Integer> uniqueNums = Sets.newHashSet();
            for (final int x : this.numbers)
            {
                if (!uniqueNums.add(x))
                {
                    throw new IllegalArgumentException("numbers aren't unique");
                }

            }
        }

        @Override
        public String toString()
        {
            final StringBuilder b = new StringBuilder();
            for (int i = 0; i < this.numbers.length - 1; i++)
            {
                b.append(this.numbers[i]);
                b.append(this.operators[i]);
            }
            b.append(this.numbers[this.numbers.length - 1]);
            b.append("=").append(getTotal());
            return b.toString();
        }

        public Equation switchNumbers()
        {
            final int index = rand.nextInt(this.numbers.length);
            int otherIndex = index;
            while (otherIndex == index)
            {
                otherIndex = rand.nextInt(this.numbers.length);
            }

            final int x = this.numbers[index];
            final int y = this.numbers[otherIndex];
            this.numbers[index] = y;
            this.numbers[otherIndex] = x;
            return this;
        }

        public Equation switchOperators()
        {
            final int index = rand.nextInt(this.operators.length);
            this.operators[index] = nextOp();
            return this;
        }
    }

    private static final long MAX = Long.MAX_VALUE;

    public static void main(final String[] args)
    {
        int[] ints;
        ints = new int[] { 2, 3 };
        ints = new int[] { 23, 12, 7, 9, 2, 4 };

        int goal;
        goal = 1;
        goal = 56;
        Equation eq = new Equation(ints);
        for (long iteration = 0; iteration < MAX; iteration++)
        {
            if (eq.isEqual(goal))
            {
                break;
            }

            eq = eq.switchNumbers();
            if (eq.isEqual(goal))
            {
                break;
            }

            eq = eq.switchOperators();
        }
        if (eq.isEqual(goal))
        {
            System.out.println("SUCCESS");
            System.out.println(eq);
        }
    }

}
