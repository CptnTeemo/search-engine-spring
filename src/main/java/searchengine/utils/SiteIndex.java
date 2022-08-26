package searchengine.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import searchengine.config.IndexConfig;
import searchengine.dto.PageDto;
import searchengine.dto.SiteDto;
import searchengine.entity.PageEntity;
import searchengine.entity.Site;
import searchengine.entity.Status;
import searchengine.repository.PagesRepository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Slf4j
public class SiteIndex implements Runnable{

    private static final int processorCoreCount = Runtime.getRuntime().availableProcessors();
    private final PagesRepository pageRepository;
//    private final LemmaParser lemmaParser;
//    private final LemmaRepository lemmaRepository;
//    private final IndexParser indexParser;
//    private final IndexRepository indexRepository;
//    private final SiteRepository siteRepository;
    private final String url;
//    private final IndexConfig indexConfig;

    private final String SOURCE = "http://www.playback.ru/";
    private final String SITE_NAME = "Телефоны";
    private MappingUtils mappingUtils;
    private final IndexConfig indexConfig;


//    public SiteIndex(PagesRepository pageRepository, String url, IndexConfig indexConfig) {
//        this.pageRepository = pageRepository;
//        this.url = url;
//        this.indexConfig = indexConfig;
//    }

    @Override
    public void run() {
        log.info("Начата индексация - " + url);
        Site site = new Site();
        site.setUrl(url);
        site.setName(getName());
        site.setStatus(Status.INDEXING);
        site.setStatusTime(new Date());
//        siteRepository.save(site);
        var pageEntityList = getAllPagesDto();
        log.info("пытаемся записать - " + url);
        log.info(String.valueOf(pageEntityList.size()));
        pageRepository.saveAll(pageEntityList);
        log.info("Индексация закончена- " + url);
    }

    private Set<PageEntity> getAllPagesDto() {
        log.info("Индексируем в getAllPagesDto - вызов метода");
        Set<PageEntity> pageEntityList = new HashSet<>();
        SiteDto siteDto = new SiteDto(url, getName());
        ForkJoinPool pool = new ForkJoinPool(processorCoreCount);
//        Set<PageDto> pageDtoList = pool.invoke(new LinkCollector(SOURCE, siteDto));
        log.info("Индексируем в getAllPagesDto - создали pool");
        LinkCollector linkCollector = new LinkCollector(url, siteDto);
        pool.execute(linkCollector);
        pool.shutdown();
        Set<PageDto> pageDtoList = linkCollector.join();
        pageDtoList.forEach(e -> pageEntityList.add(MappingUtils.pageToPageEntity(e)));
        return pageEntityList;
    }

    private String getName() {
        var urlList = indexConfig.getSite();
        for (Map<String, String> map : urlList) {
            if (map.get("url").equals(url)) {
                return map.get("name");
            }
        }
        return "";
    }

}
