package searchengine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.response.FalseResponse;
import searchengine.dto.response.TrueResponse;
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

    @PostMapping(value = "/indexPage")
    public ResponseEntity<Object> saveDataByUrl(@RequestBody String url) {
        if (pagesService.saveDataFromUrl(url)) {
            return new ResponseEntity<>(new TrueResponse(true), HttpStatus.OK);
        } else return new ResponseEntity<>(new FalseResponse(false,
                "Данная страница находится за пределами сайтов"),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    //    @GetMapping(value = "/api/startIndexing")
    @GetMapping(value = "/startIndexing")
    public ResponseEntity<Object> saveAllData() {
        if (pagesService.saveAllPagesFromSiteList()) {
            return new ResponseEntity<>(new TrueResponse(true), HttpStatus.OK);
        } else return new ResponseEntity<>(new FalseResponse(false, "Индексация уже запущена"),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Object> stopIndexing() {
        if (pagesService.stopIndexing()) {
            return new ResponseEntity<>(new TrueResponse(true), HttpStatus.OK);
        } else return new ResponseEntity<>(new FalseResponse(false, "Индексация не запущена"),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping(value = "/api/clearPages")
    public void deleteAllPages() {
        pagesService.deleteAllPages();
    }
}
