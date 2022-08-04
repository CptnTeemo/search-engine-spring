package searchengine.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import searchengine.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "/api/startIndexing")
    public void saveAllPages() {
        pagesService.saveAllPages();
    }

    @DeleteMapping(value = "/api/clearPages")
    public void deleteAllPages() {
        pagesService.deleteAllPages();
    }
}
