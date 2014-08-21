package uk.ac.ebi.pride.prider.utils.config;

import uk.ac.ebi.pride.archive.dataprovider.file.ProjectFileProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;

/**
 * @author Jose A. Dianes
 * @version $Id$
 */
public interface FilePathBuilder {
    String buildPublicationFilePath(String prefix, ProjectProvider project, ProjectFileProvider file);

}
