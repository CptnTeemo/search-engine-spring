package searchengine.morphology;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LemmaCollection {
    private Map<String, Long> title;
    private Map<String, Long> body;
    private Integer pageId;
    private Integer siteId;

    public LemmaCollection(Map<String, Long> title, Map<String, Long> body, Integer pageId, Integer siteId) {
        this.title = title;
        this.body = body;
        this.pageId = pageId;
        this.siteId = siteId;
    }

}
