package searchengine.service.impl;

import searchengine.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.dto.SiteDto;
import searchengine.entity.PageEntity;
import searchengine.repository.PagesRepository;
import searchengine.utils.LinkCollector;
import searchengine.utils.MappingUtils;
import searchengine.service.PagesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
public class PagesServiceImpl implements PagesService {

    @Autowired
    private PagesRepository pagesRepository;
    private MappingUtils mappingUtils;
    private LinkCollector linkCollector;
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
    public void saveAllPages() {
        List<PageEntity> pageEntityList = new ArrayList<>();
        SiteDto siteDto = new SiteDto(SOURCE, SITE_NAME);
        ForkJoinPool pool = new ForkJoinPool();
        LinkCollector linkCollector = new LinkCollector(SOURCE, siteDto);
        pool.execute(linkCollector);
        pool.shutdown();
        Set<PageDto> pageDtoList = linkCollector.join();
        pageDtoList.forEach(e -> pageEntityList.add(mappingUtils.pageToPageEntity(e)));
        pagesRepository.saveAll(pageEntityList);
    }

    @Override
    public void deleteAllPages() {
        pagesRepository.deleteAll();
    }


}
