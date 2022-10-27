package searchengine.service;

import searchengine.dto.SearchPageResult;

import java.util.List;

public interface SearchEngineService {

    List<SearchPageResult> allSiteSearch(String text, int offset, int limit);
    List<SearchPageResult> siteSearch(String searchText, String url, int offset, int limit);
}
