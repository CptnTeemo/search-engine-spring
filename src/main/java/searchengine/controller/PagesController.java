package searchengine.controller;

import org.springframework.web.bind.annotation.*;
import searchengine.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.entity.PageEntity;
import searchengine.service.impl.PagesServiceImpl;

import java.util.List;

@RestController
public class PagesController {
    @Autowired
    private PagesServiceImpl pagesService;

    @GetMapping(value = "/")
    public List<PageDto> list() {
        return pagesService.getAllPages();
    }

    @PostMapping(value = "/api/startIndexing")
    public void saveAllPages(@RequestBody String url) {
        pagesService.saveAllPages(url);
    }

    @GetMapping(value = "/api/startIndexing")
    public void saveAllData() {
        pagesService.saveAllPagesFromSiteList();
    }

    @DeleteMapping(value = "/api/clearPages")
    public void deleteAllPages() {
        pagesService.deleteAllPages();
    }
}
