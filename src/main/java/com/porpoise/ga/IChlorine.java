package com.porpoise.ga;

/**
 * interface used to thin out the gene pool. All these breeding, mutating genes are all well and all, but their evolution wouldn't be
 * possible without letting the weak ones die out (survival of the fitest). This interface is responsible for evolving one generation of
 * genes into another
 */
public interface IChlorine
{

    IGenePool evolve(final IGenePool pool);
}
