package searchengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.SearchPageResult;
import searchengine.dto.response.FalseResponse;
import searchengine.dto.response.SearchResponse;
import searchengine.repository.SiteRepository;
import searchengine.service.SearchEngineService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchEngineController {

    private final SearchEngineService searchService;
    private final SiteRepository siteRepository;

    @GetMapping("/search")
    public ResponseEntity<Object> searchWords(
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "site", required = false, defaultValue = "") String site,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit) {
        if (query.isEmpty()) {
            return new ResponseEntity<>(new FalseResponse(false, "Задан пустой поисковый запрос"),
                    HttpStatus.BAD_REQUEST);
        } else {
            List<SearchPageResult> searchData;
            if (!site.isEmpty()) {
                if (siteRepository.findByUrl(site) == null) {
                    return new ResponseEntity<>(new FalseResponse(false, "Указанная страница не найдена"),
                            HttpStatus.BAD_REQUEST);
                } else {
                    searchData = searchService.siteSearch(query, site, offset, limit);
                }
            } else {
                searchData = searchService.allSiteSearch(query, offset, limit);
            }
            return new ResponseEntity<>(new SearchResponse(true, searchData.size(), searchData),
                    HttpStatus.OK);
        }
    }
}
