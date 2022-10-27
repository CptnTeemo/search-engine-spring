package searchengine.service;

import searchengine.dto.PageDto;

import java.util.List;

public interface PagesService {

    List<PageDto> getAllPages();
    boolean saveDataFromUrl(String url);
    boolean saveAllPagesFromSiteList();
    void deleteAllPages();
}
