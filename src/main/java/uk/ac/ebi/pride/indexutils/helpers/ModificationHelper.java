package uk.ac.ebi.pride.indexutils.helpers;

import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.indexutils.modifications.Modification;
import uk.ac.ebi.pride.jmztab.model.CVParam;
import uk.ac.ebi.pride.jmztab.model.MZTabUtils;
import uk.ac.ebi.pride.jmztab.model.Section;

import uk.ac.ebi.pridemod.ModReader;
import uk.ac.ebi.pridemod.model.PTM;

import java.util.Map;

/**
 * User: ntoro
 * Date: 01/07/2014
 * Time: 17:54
 */
public class ModificationHelper {

    public static final String SPLIT_CHAR = ":";

    public static ModificationProvider convertFromString(String modification) {
        uk.ac.ebi.pride.jmztab.model.Modification mzTabMod = MZTabUtils.parseModification(Section.PSM, modification);
        return convertToModificationProvider(mzTabMod);
    }

    public static String convertToString(ModificationProvider modification) {

        if (modification == null)
            return null;

        uk.ac.ebi.pride.jmztab.model.Modification mzTabMod = convertFromModification(modification);
        return mzTabMod.toString();
    }

    public static ModificationProvider convertToModificationProvider(uk.ac.ebi.pride.jmztab.model.Modification mzTabMod) {

        if (mzTabMod == null)
            return null;

        Modification modification = new Modification();
        ModReader modReader = ModReader.getInstance();

        //TODO: Handle the neutral loss cases
        String ptmName;
        String accession;
        if (mzTabMod.getType() == uk.ac.ebi.pride.jmztab.model.Modification.Type.NEUTRAL_LOSS) {
            // neutral losses are not handled yet,
            // they are defined by MS terms which are not supported by the ModReader
            accession = mzTabMod.getAccession();
            ptmName = mzTabMod.toString();
        } else {
            accession = mzTabMod.getType().name() + SPLIT_CHAR + mzTabMod.getAccession();
            PTM ptm = modReader.getPTMbyAccession(accession);
            ptmName = ptm.getName();
        }

        modification.setAccession(accession);
        modification.setName(ptmName);

        if (mzTabMod.getPositionMap() != null && !mzTabMod.getPositionMap().isEmpty()) {
            for (Map.Entry<Integer, uk.ac.ebi.pride.jmztab.model.CVParam> integerCVParamEntry : mzTabMod.getPositionMap().entrySet()) {
                modification.addPosition(
                    integerCVParamEntry.getKey(),
                    CvParamHelper.convertToCvParamProvider(integerCVParamEntry.getValue()));
            }
        }
        modification.setNeutralLoss(CvParamHelper.convertToCvParamProvider(mzTabMod.getNeutralLoss()));

        return modification;
    }


    private static uk.ac.ebi.pride.jmztab.model.Modification convertFromModification(ModificationProvider modification) {

        if (modification == null)
            return null;

        // Default values
        String type = "UNKNOWN";
        String accession = "0";

        if (modification.getAccession() != null && !modification.getAccession().isEmpty()) {
            String[] splittedAccession = modification.getAccession().split(SPLIT_CHAR);
            type = splittedAccession[0];
            accession = splittedAccession[1];
        }

        uk.ac.ebi.pride.jmztab.model.Modification mzTabMod
                = new uk.ac.ebi.pride.jmztab.model.Modification(
                Section.PSM,
                uk.ac.ebi.pride.jmztab.model.Modification.findType(type),
                accession);

        mzTabMod.setNeutralLoss((CVParam) CvParamHelper.convertFromCvParamProvider(modification.getNeutralLoss()));

        if (mzTabMod.getPositionMap() != null) {
            mzTabMod.setAmbiguity(mzTabMod.getPositionMap().size() > 1);
        }

        if (modification.getPositionMap() != null && !modification.getPositionMap().isEmpty()) {
            for (Map.Entry<Integer, CvParamProvider> integerCVParamEntry : modification.getPositionMap().entrySet()) {
                mzTabMod.addPosition(
                    integerCVParamEntry.getKey(),
                    (CVParam) CvParamHelper.convertFromCvParamProvider(integerCVParamEntry.getValue()));
            }
        }

        return mzTabMod;
    }
}
