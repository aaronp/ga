package com.porpoise.ga;

/**
 * A gene pool holds a number of GeneSequences. It should also be able to inspect those gene sequences for a 'solution', as well as cull its
 * population via {@link #thinPopulation()}
 */
public interface IGenePool extends Iterable<GeneSequence>
{

    /**
     * If this method returns true, then {@link #getSolution()} should return a valid, complete, non-null solution.
     * 
     * @return true if this gene pool contains one or more 'solutions'
     */
    boolean hasSolution();

    /**
     * @return a valid, complete solution or null if none exists
     */
    GeneSequence getSolution();

    /**
     * add the given gene sequence to the gene pool.
     * 
     * @param seq
     *            the gene sequence to add
     */
    void populate(GeneSequence seq);

    /**
     * Thin out this gene pool. This is a callback method, notifying (asking) the gene pool to cull/thin out its population. This will
     * presumably be based on fitness, though what actually happens will be up to the implementation. This method may not have any affect,
     * and after calling the total population may be less than, equivalent to or even greater than the population before calling.
     */
    void thinPopulation();

}
