package uk.ac.ebi.pride.indexutils.helpers;

import org.junit.Test;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.jmztab.model.Modification;
import uk.ac.ebi.pride.jmztab.model.Section;

import static org.junit.Assert.assertEquals;

public class ModificationHelperTest {

    @Test
    public void testConvertToModificationProvider() throws Exception {

        final Modification mzTabMod = new Modification(Section.Protein, Modification.Type.CHEMMOD, "14");
        final ModificationProvider modification = ModificationHelper.convertToModificationProvider(mzTabMod);

        assertEquals("CHEMMOD:14", modification.getAccession());
    }

    @Test
    public void testConvertFromString() throws Exception {

        final String mod = "1-CHEMMOD:14";
        final ModificationProvider modificationProvider = ModificationHelper.convertFromString(mod);

        assertEquals("CHEMMOD:14", modificationProvider.getAccession());
        assertEquals((Integer) 1, modificationProvider.getMainPosition());

    }

    @Test
    public void testConvertToString() throws Exception {

        uk.ac.ebi.pride.indexutils.modifications.Modification modification = new uk.ac.ebi.pride.indexutils.modifications.Modification();
        modification.addPosition(1, null);
        modification.setAccession("CHEMMOD:14");
        modification.setName("Unknown modification");

        final String mod = ModificationHelper.convertToString(modification);

        assertEquals("1-CHEMMOD:14", mod);

    }
}
