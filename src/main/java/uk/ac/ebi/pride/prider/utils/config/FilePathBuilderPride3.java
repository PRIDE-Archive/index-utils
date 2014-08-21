package uk.ac.ebi.pride.prider.utils.config;

import uk.ac.ebi.pride.archive.dataprovider.file.ProjectFileProvider;
import uk.ac.ebi.pride.archive.dataprovider.project.ProjectProvider;

import java.io.File;
import java.util.Calendar;

/**
 * @author Jose A. Dianes
 * @author Rui Wang
 * @version $Id$
 */
public class FilePathBuilderPride3 implements FilePathBuilder {

    @Override
    public String buildPublicationFilePath(String prefix, ProjectProvider project, ProjectFileProvider file) {
        if (project.isPublicProject()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(project.getPublicationDate());
            int month = calendar.get(Calendar.MONTH) + 1;

            return prefix
                    + File.separator + calendar.get(Calendar.YEAR)
                    + File.separator + (month < 10 ? "0" : "") + month
                    + File.separator + project.getAccession()
                    + (file == null ? "" : File.separator + file.getFileSource().getFolderName() + File.separator + file.getFileName());
        } else {
            return prefix
                    + File.separator + project.getAccession()
                    + (file == null ? "" : File.separator + file.getFileSource().getFolderName() + File.separator + file.getFileName());
        }

    }
}
