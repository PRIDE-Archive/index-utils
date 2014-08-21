package uk.ac.ebi.pride.prider.utils.spectrum;

/**
 * Interface for generating spectrum id
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface SpectrumIDGenerator {

    String generate(String ... parts);
}
