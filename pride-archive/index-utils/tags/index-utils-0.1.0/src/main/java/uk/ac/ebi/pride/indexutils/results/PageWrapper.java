package uk.ac.ebi.pride.indexutils.results;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ntoro
 * @since 28/08/2014 11:25
 */

/**
 * Wrapper of page that allows providing highlighting as an extra information.
 * The highlighting can be empty if is not provided for the original query
 * @param <T> (e.g. Psm, ProteinIdentification)
 */
public class PageWrapper<T> {

    Page<T> page;
    Map<T, Map<String, List<String>>> highlights = new HashMap<T, Map<String,List<String>>>();


    public PageWrapper(HighlightPage<T> highlightPage){

        this.page = highlightPage;

        if(highlightPage!= null){
            for (HighlightEntry<T> highlightEntry : highlightPage.getHighlighted()) {
                Map<String, List<String>> aux = new HashMap<String, List<String>>();
                for (HighlightEntry.Highlight highlight : highlightEntry.getHighlights()) {
                    aux.put(highlight.getField().getName(), highlight.getSnipplets());
                }
                this.highlights.put(highlightEntry.getEntity(), aux);
            }
        }
    }

    public PageWrapper(Page<T> page){
        this.page = page;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public Map<T, Map<String, List<String>>> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<T, Map<String, List<String>>> highlights) {
        this.highlights = highlights;
    }
}
