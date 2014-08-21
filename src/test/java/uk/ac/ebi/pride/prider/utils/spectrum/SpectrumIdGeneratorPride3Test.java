package uk.ac.ebi.pride.prider.utils.spectrum;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class SpectrumIdGeneratorPride3Test {
    @Test
    public void testGenerate() throws Exception {
        SpectrumIdGeneratorPride3 spectrumIdGeneratorPride3 = new SpectrumIdGeneratorPride3();

        String id = spectrumIdGeneratorPride3.generate("PXD000001", "file.mgf", "index=1");

        assertThat("Spectrum id doesn't match", id.equals("PXD000001;file.mgf;index=1"));
    }
}
