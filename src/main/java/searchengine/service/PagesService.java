package searchengine.service;

import searchengine.dto.PageDto;

import java.util.List;

public interface PagesService {

    List<PageDto> getAllPages();
    void saveAllPages();
    void deleteAllPages();
}
