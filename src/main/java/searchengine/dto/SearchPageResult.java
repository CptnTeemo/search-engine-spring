package searchengine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPageResult implements Comparable<SearchPageResult>{

    String site;
    String siteName;
    String uri;
    String title;
    String snippet;
    Float relevance;

    public SearchPageResult(String site, String siteName, String uri, String title, String snippet, Float relevance) {
        this.site = site;
        this.siteName = siteName;
        this.uri = uri;
        this.title = title;
        this.snippet = snippet;
        this.relevance = relevance;
    }

    @Override
    public int compareTo(SearchPageResult otherSearchResult) {
        if (this.relevance < otherSearchResult.getRelevance()) {
            return -1;
        } else if (this.relevance > otherSearchResult.getRelevance()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return getUri() + " || " + getTitle() + " || " + getSnippet() + "\n" +
                getRelevance();
    }
}
