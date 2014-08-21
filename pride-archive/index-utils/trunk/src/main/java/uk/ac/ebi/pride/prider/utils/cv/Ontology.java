package uk.ac.ebi.pride.prider.utils.cv;

/**
 * An enumeration of common ontologies with their name
 *
 * @author Rui Wang
 * @version $Id$
 */
public enum Ontology {
    NEWT("NEWT"),
    BRENDA("BTO"),
    CL("CL"),
    DISEASE("DOID"),
    GO("GO");

    String name;

    Ontology(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
