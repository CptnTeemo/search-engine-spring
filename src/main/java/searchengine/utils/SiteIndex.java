package searchengine.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.config.IndexConfig;
import searchengine.dto.IndexDto;
import searchengine.dto.LemmaDto;
import searchengine.dto.PageDto;
import searchengine.dto.SiteDto;
import searchengine.entity.*;
import searchengine.morphology.LemmaCollection;
import searchengine.repository.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Slf4j
public class SiteIndex implements Runnable {

    private static final int processorCoreCount = Runtime.getRuntime().availableProcessors();
    private final PagesRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final SiteRepository siteRepository;
    private final LemmaCollector lemmaCollector;
    private final Indexing indexing;
    private final String url;
    private final IndexConfig indexConfig;

    private MappingUtils mappingUtils;
    private SiteDto siteDto;

    @Override
    public void run() {
        if (siteRepository.findByUrl(url) != null) {
            log.info("Начато удаление данных - " + url);
            var site = siteRepository.findByUrl(url);
            site.setStatus(Status.INDEXING);
            site.setStatusTime(new Date());
            siteRepository.save(site);
            siteRepository.delete(site);
        }
        log.info("Начата индексация - " + url);
        siteDto = new SiteDto(url, getName());
        Site site = new Site();
        site.setUrl(url);
        site.setName(getName());
        site.setStatus(Status.INDEXING);
        site.setStatusTime(new Date());
        site.setId(siteDto.getId());
        siteRepository.save(site);
        try {
            var pageDtoList = getAllPagesDto();
            log.info("пытаемся записать - " + url);
            savePagesToDataBase(pageDtoList);
            log.info("Приступаем к лемматизации - " + url);
            var lemmaCollectionMap = new HashMap<>(getLemmas(pageDtoList));
            log.info("Приступаем к индексированию - " + url);
            indexingLemmas(lemmaCollectionMap);
            log.info("Индексация закончена- " + url);
        } catch (Exception e) {
            log.error("Индексация остановлена - " + url);
            site.setLastError("Индексация остановлена");
            site.setStatus(Status.FAILED);
            site.setStatusTime(new Date());
            siteRepository.save(site);
        }

    }

    private Set<PageDto> getAllPagesDto() throws InterruptedException {
        if (!Thread.interrupted()) {
            var site = siteRepository.findByUrl(url);
            var siteDto = new SiteDto(site.getUrl(), site.getName());
            log.info("Индексируем в getAllPagesDto - вызов метода");
            ForkJoinPool pool = new ForkJoinPool(processorCoreCount);
            log.info("Индексируем в getAllPagesDto - создали pool");
            Set<PageDto> pagesCollection = new HashSet<>();
            var pageDtoList = pool.invoke(new LinkCollector(siteDto.getUrl(), siteDto, pagesCollection));
            return new CopyOnWriteArraySet<>(pageDtoList);
        } else throw new InterruptedException();
    }

    private void savePagesToDataBase(Set<PageDto> pageDtoList) throws InterruptedException {
        if (!Thread.interrupted()) {
            Set<PageEntity> pageEntityList = new HashSet<>();
            pageDtoList.forEach(e -> pageEntityList.add(MappingUtils.pageToPageEntity(e)));
            pageRepository.saveAll(pageEntityList);
        } else throw new InterruptedException();
    }

    private Map<String, LemmaCollection> getLemmas(Set<PageDto> pageDtoList) throws InterruptedException {
        if (!Thread.interrupted()) {
            lemmaCollector.run(pageDtoList);
            List<LemmaDto> lemmaDtoList = new ArrayList<>(lemmaCollector.getLemmaDtoList());
            List<Lemma> lemmaEntityList = new ArrayList<>();
            Map<String, LemmaCollection> lemmaCollectionMap = new HashMap<>(lemmaCollector.getLemmaMap());
            lemmaDtoList.forEach(e -> lemmaEntityList.add(MappingUtils.lemmaDtoToLemma(e)));
            log.info("Пишем леммы в базу " + url);
            lemmaRepository.saveAll(lemmaEntityList);
            return lemmaCollectionMap;
        } else throw new InterruptedException();
    }

    private void indexingLemmas(Map<String, LemmaCollection> lemmaCollectionMap) throws InterruptedException {
        if (!Thread.interrupted()) {
            var site = siteRepository.findByUrl(url);
            indexing.run(lemmaCollectionMap);
            List<IndexDto> indexDtoList = new ArrayList<>(indexing.getIndexDtoList());
            List<IndexEntity> indexEntityList = new ArrayList<>();
            for (IndexDto indexDto : indexDtoList) {
                if (!Thread.interrupted()) {
                    indexEntityList.add(MappingUtils.indexDtoToIndexEntity(indexDto));
                    site.setStatusTime(new Date());
                } else throw new InterruptedException();
            }
            indexRepository.saveAll(indexEntityList);
            log.info("Индексация завершена - " + url);
            site.setStatusTime(new Date());
            site.setStatus(Status.INDEXED);
            siteRepository.save(site);
        } else throw new InterruptedException();
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
