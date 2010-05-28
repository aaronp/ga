package com.porpoise.ga;

import org.junit.Ignore;

@Ignore
enum TestSequencers
{
    ;// uninstantiable

    @SuppressWarnings("boxing")
    private static final IGenotype<Integer> integerGenotype   = Genotype.of(1, 2, 3, 4, 5, 6);
    private static final IGenotype<String>  characterGenotype = Genotype.of("a", "b", "c");

    public static IGenotype<Integer> integers()
    {
        return integerGenotype;
    }

    public static IGenotype<String> characters()
    {
        return characterGenotype;
    }

    public static GeneSequencer alphaNumeric()
    {
        final int size = 4;
        return alphaNumeric(size);
    }

    /**
     * @param size
     * @return
     */
    public static GeneSequencer alphaNumeric(final int size)
    {
        final GeneSequencer sequencer = new GeneSequencer();

        for (int i = 0; i < size; i++)
        {
            if (i % 2 == 0)
            {
                sequencer.addGenotype(characterGenotype);
            }
            else
            {
                sequencer.addGenotype(integerGenotype);
            }
        }

        return sequencer;
    }

}
