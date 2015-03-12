package uk.ac.ebi.pride.indexutils.modifications;

import uk.ac.ebi.pride.archive.dataprovider.identification.ModificationProvider;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.indexutils.helpers.ModificationHelper;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Noemi del Toro, Jose A. Dianes
 * @version $Id$
 */
public class Modification implements ModificationProvider {

    private String accession;
    private String name;
    private Integer mainPosition;
    private Map<Integer, CvParamProvider> positionMap = new TreeMap<Integer, CvParamProvider>();
    private CvParamProvider neutralLoss;

    public Modification() {
    }

    public Modification(String asString) {
        ModificationProvider mod = ModificationHelper.convertFromString(asString);
        this.accession = mod.getAccession();
        this.name = mod.getName();
        this.mainPosition = mod.getMainPosition();
        this.positionMap = mod.getPositionMap();
        this.neutralLoss = mod.getNeutralLoss();
    }

    @Override
    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Position used by the Archive web app. In the case of ambiguous position
     * it will be the first one reported
     */
    @Override
    public Integer getMainPosition() {
        //TODO: Change the selection from the first one to the highest one
        //In the case that we have ambiguous modification we choose the one with highest score to be expose
        if (this.getPositionMap() != null && !this.getPositionMap().isEmpty()) {
            this.mainPosition = this.getPositionMap().entrySet().iterator().next().getKey();
        }

        return mainPosition;
    }


    @Override
    public Map<Integer, CvParamProvider> getPositionMap() {
        return positionMap;
    }

    public void addPosition(Integer id, CvParamProvider param) {
        this.positionMap.put(id, param);
    }

    public void setPositionMap(Map<Integer, CvParamProvider> positionMap) {
        this.positionMap = positionMap;
    }

    public CvParamProvider getNeutralLoss() {
        return neutralLoss;
    }

    public void setNeutralLoss(CvParamProvider neutralLoss) {
        this.neutralLoss = neutralLoss;
    }

    public String getModificationAsString() {
        return ModificationHelper.convertToString(this);
    }


}
