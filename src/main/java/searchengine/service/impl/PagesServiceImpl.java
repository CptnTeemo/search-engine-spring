package searchengine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.IndexConfig;
import searchengine.dto.PageDto;
import searchengine.repository.PagesRepository;
import searchengine.service.PagesService;
import searchengine.utils.MappingUtils;
import searchengine.utils.SiteIndex;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagesServiceImpl implements PagesService {


    private final IndexConfig indexConfig;
    private ExecutorService executorService;
    @Autowired
    private PagesRepository pagesRepository;
    private static final int processorCoreCount = Runtime.getRuntime().availableProcessors();
    private MappingUtils mappingUtils;
    private final String SOURCE = "http://www.playback.ru/";
    private final String SITE_NAME = "Телефоны";

    @Override
    public List<PageDto> getAllPages() {
        List<PageDto> pageDtoList;
        pageDtoList = pagesRepository.findAll().stream()
                .map(e -> mappingUtils.pageEntityToPageDto(e))
                .collect(Collectors.toList());
        return pageDtoList;
    }

    @Override
    public void saveAllPages(String url) {
        executorService = Executors.newFixedThreadPool(processorCoreCount);
        executorService.submit(new SiteIndex(pagesRepository, url, indexConfig));
        executorService.shutdown();
    }

    @Override
    public void saveAllPagesFromSiteList() {
        var urlList = indexConfig.getSite();
        executorService = Executors.newFixedThreadPool(processorCoreCount);
        for (Map<String, String> map : urlList) {
            String url = map.get("url");
            executorService.submit(new SiteIndex(pagesRepository,
                    url,
                    indexConfig));
        }
        executorService.shutdown();
    }


    @Override
    public void deleteAllPages() {
        pagesRepository.deleteAll();
    }


}
