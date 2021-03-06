package uk.ac.ebi.pride.indexutils.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.indexutils.modifications.Modification;
import uk.ac.ebi.pride.jmztab.model.CVParam;
import uk.ac.ebi.pride.jmztab.model.MZTabUtils;
import uk.ac.ebi.pride.jmztab.model.Section;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pride.utilities.pridemod.model.PTM;

import java.util.Map;

import static uk.ac.ebi.pride.jmztab.model.Modification.Type;
import static uk.ac.ebi.pride.jmztab.model.Modification.findType;

/**
 * User: ntoro
 * Date: 01/07/2014
 * Time: 17:54
 */
public class ModificationHelper {

    public static final String SPLIT_CHAR = ":";
    private static Logger log = LoggerFactory.getLogger(ModificationHelper.class);

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

        String accession = null;
        String ptmName = null;

        final Type mzTabModType = mzTabMod.getType();
        if (!mzTabModType.equals(Type.NEUTRAL_LOSS)) {
            accession = mzTabModType.name() + SPLIT_CHAR + mzTabMod.getAccession();
            if(mzTabModType.equals(Type.MOD) || mzTabModType.equals(Type.UNIMOD)){
                PTM ptm = modReader.getPTMbyAccession(accession);
                if(ptm == null) {
                    log.warn("The provided modification " + accession + " cannot be found in the PSIMOD or Unimod ontology.");
                    return null;
                }
                ptmName = ptm.getName();
            }
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
        else {
            //If we don't have accession could be an unknown mod or a neutral loss
            if(modification.getNeutralLoss()!= null){
               type = "NEUTRAL_LOSS";
            }
        }

        uk.ac.ebi.pride.jmztab.model.Modification mzTabMod
                = new uk.ac.ebi.pride.jmztab.model.Modification(
                Section.PSM,
                findType(type),
                accession);

        mzTabMod.setNeutralLoss((CVParam) CvParamHelper.convertFromCvParamProvider(modification.getNeutralLoss()));

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
