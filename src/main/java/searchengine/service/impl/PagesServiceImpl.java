package searchengine.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.IndexConfig;
import searchengine.dto.PageDto;
import searchengine.entity.Site;
import searchengine.entity.Status;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PagesRepository;
import searchengine.repository.SiteRepository;
import searchengine.service.PagesService;
import searchengine.utils.Indexing;
import searchengine.utils.LemmaCollector;
import searchengine.utils.MappingUtils;
import searchengine.utils.SiteIndex;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagesServiceImpl implements PagesService {


    private final IndexConfig indexConfig;
    private ExecutorService executorService;
    @Autowired
    private PagesRepository pagesRepository;
    @Autowired
    private LemmaRepository lemmaRepository;
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private final SiteRepository siteRepository;
    private final LemmaCollector lemmaCollector;
    private final Indexing indexing;
    private static final int processorCoreCount = Runtime.getRuntime().availableProcessors();
    private MappingUtils mappingUtils;

    @Override
    public List<PageDto> getAllPages() {
        List<PageDto> pageDtoList;
        pageDtoList = pagesRepository.findAll().stream()
                .map(e -> mappingUtils.pageEntityToPageDto(e))
                .collect(Collectors.toList());
        return pageDtoList;
    }

    @Override
    public boolean saveDataFromUrl(String url) {
        if (urlCheck(clearUrl(url))) {
            executorService = Executors.newFixedThreadPool(processorCoreCount);
            executorService.submit(new SiteIndex(pagesRepository,
                    lemmaRepository,
                    indexRepository,
                    siteRepository,
                    lemmaCollector,
                    indexing,
                    url,
                    indexConfig));
            executorService.shutdown();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean saveAllPagesFromSiteList() {
        if (isIndexingActive()) {
            return false;
        } else {
            var urlList = indexConfig.getSite();
            executorService = Executors.newFixedThreadPool(processorCoreCount);
            for (Map<String, String> map : urlList) {
                String url = map.get("url");
                executorService.submit(new SiteIndex(pagesRepository,
                        lemmaRepository,
                        indexRepository,
                        siteRepository,
                        lemmaCollector,
                        indexing,
                        url,
                        indexConfig));
            }
            executorService.shutdown();
            return true;
        }
    }

    public boolean stopIndexing() {
        if (isIndexingActive()) {
            log.info("Останавливаем индексацию");
            executorService.shutdownNow();
            return true;
        } else {
            return false;
        }
    }

    private boolean isIndexingActive() {
        var siteList = siteRepository.findAll();
        for (Site site : siteList) {
            if (site.getStatus() == Status.INDEXING) {
                return true;
            }
        }
        return false;
    }

    private boolean urlCheck(String url) {
        var urlList = indexConfig.getSite();
        for (Map<String, String> map : urlList) {
            if (map.get("url").equals(url)) {
                return true;
            }
        }
        return false;
    }

    private String clearUrl(String url) {
        String regex = "(http|https)(://.*)(.ru|.com)(\\/\\w|\\/\\d)(.*)";
        return url.replaceAll(regex, "$1"+"$2"+"$3");
    }

    @Override
    public void deleteAllPages() {
        pagesRepository.deleteAll();
    }


}
