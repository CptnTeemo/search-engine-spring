package searchengine.dto.response;

import lombok.Value;
import searchengine.dto.SearchPageResult;

import java.util.List;

@Value
public class SearchResponse {
    boolean result;
    int count;
    List<SearchPageResult> data;
}
