package searchengine.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import searchengine.dto.PageDto;
import searchengine.dto.SiteDto;

import java.util.*;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class LinkCollector extends RecursiveTask<Set<PageDto>> {

    private String path;
    private final SiteDto siteDto;
    private final Set<PageDto> pagesCollection;

    public LinkCollector(String path, SiteDto siteDto, Set<PageDto> pagesCollection) {
        this.path = path;
        this.siteDto = siteDto;
        this.pagesCollection = pagesCollection;
    }


    @SneakyThrows
    @Override
    protected Set<PageDto> compute() {
        Thread.sleep(250);
        Document document = getConnect(path);
        String getUrl = siteDto.getUrl() + "(.*)";
        List<LinkCollector> tasks = new ArrayList<>();
        Set<PageDto> siteMap = new TreeSet<>();


        if (document != null) {
            Map<String, PageDto> pagesCollectionMap = new HashMap<>();
            Elements linkList = document.select("a");
            Connection.Response response = document.connection().response();
            Integer statusCode = response.statusCode();
            PageDto pageDto =
                    new PageDto(modUrl(getUrl, path), document.html(), statusCode, siteDto.getId());

            pagesCollectionMap.put(path, pageDto);

            Set<String> hrefList = linksCollector(linkList, path);
            if (hrefList.size() > 0) {
                hrefList.forEach(e -> {
                    LinkCollector webSiteMapCreator = new LinkCollector(e, siteDto, pagesCollection);
                    webSiteMapCreator.fork();
                    tasks.add(webSiteMapCreator);
                });
            }
            Set<Map.Entry<String, PageDto>> entrySet = pagesCollectionMap.entrySet();
            entrySet.forEach(e -> siteMap.add(e.getValue()));

            addResultsFromTasks(siteMap, tasks);
        }
        else {
            log.debug("Ошибка парсинга - " + getUrl);
            siteMap.add(new PageDto(modUrl(getUrl, path),
                    "", 500, siteDto.getId()));
        }
        return new TreeSet<>(siteMap);
    }

    public Document getConnect(String url) {
        Document doc = null;
        try {
            Thread.sleep(150);
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT" +
                            "5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
        } catch (Exception e) {
            log.info("Не удалось установить подключение с " + url);
        }
        return doc;
    }

    private String modUrl(String getUrl, String path) {
        String modUrl = path.replaceAll(getUrl, "$1");
        if (modUrl.isEmpty()) {
            return "/";
        }
        return modUrl;
    }

    private void addResultsFromTasks(Set<PageDto> list, List<LinkCollector> tasks) {
        for (LinkCollector item : tasks) {
            list.addAll(item.join());
        }
    }

    public Set<String> linksCollector(Elements elements, String path) {
        Set<String> hrefList = new TreeSet<>();
        elements.forEach(e -> {
            String subPath = e.attr("abs:href");
            if (subPath.contains(path) && !subPath.equals(path) &&
                    !subPath.contains("#") && !subPath.contains(".pdf")
                    && !subPath.toLowerCase(Locale.ROOT).contains(".jpg")
                    && !subPath.toLowerCase(Locale.ROOT).contains(".png")) {
                hrefList.add(subPath);
            }
        });
        return hrefList;
    }

}
