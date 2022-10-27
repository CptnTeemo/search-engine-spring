package searchengine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.statistic.Detailed;
import searchengine.dto.statistic.Statistics;
import searchengine.dto.statistic.Total;
import searchengine.entity.Site;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PagesRepository;
import searchengine.repository.SiteRepository;
import searchengine.service.StatisticService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final SiteRepository siteRepository;
    private final PagesRepository pageRepository;
    private final LemmaRepository lemmaRepository;

    @Override
    public Statistics getStatistic() {
        var total = getTotal();
        var detailed = getDetailedList();
        return new Statistics(total, detailed);
    }

    private Total getTotal() {
        var sites = siteRepository.count();
        var pages = pageRepository.count();
        var lemmas = lemmaRepository.count();
        return new Total(sites, pages, lemmas, true);
    }

    private Detailed getDetailed(Site site) {
        var url = site.getUrl();
        var name = site.getName();
        var status = site.getStatus();
        var statusTime = site.getStatusTime();
        var error = site.getLastError();
        var pages = pageRepository.countBySite(site);
        var lemmas = lemmaRepository.countBySite(site);
        return new Detailed(url, name, status, statusTime, error, pages, lemmas);
    }

    private List<Detailed> getDetailedList() {
        var siteList = siteRepository.findAll();
        List<Detailed> result = new ArrayList<>();
        for (Site site : siteList) {
            Detailed detailed = getDetailed(site);
            result.add(detailed);
        }
        return result;
    }
}
