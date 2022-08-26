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
    private PageDto pageDto;
    private final SiteDto siteDto;
    private static Map<String, PageDto> pagesCollection = new HashMap<>();

    public LinkCollector(String path, SiteDto siteDto) {
        this.path = path;
        this.siteDto = siteDto;
    }


    @SneakyThrows
    @Override
    protected Set<PageDto> compute() {
        Document document = getConnect(path);
            Thread.sleep(250);

        if (document != null) {
            Elements linkList = document.select("a");
            List<LinkCollector> tasks = new ArrayList<>();
            Set<PageDto> siteMap = new TreeSet<>();
            Connection.Response response = document.connection().response();
            Integer statusCode = response.statusCode();
            String getUrl = siteDto.getUrl() + "(.*)";
            PageDto pageDto =
                    new PageDto(path.replaceAll(getUrl, "/"+"$1"),
                            document.html(), statusCode, siteDto.getId());

            pagesCollection.put(path, pageDto);

            Set<String> hrefList = linksCollector(linkList, path);
            if (hrefList.size() > 0) {
                hrefList.forEach(e -> {
                    LinkCollector webSiteMapCreator = new LinkCollector(e, siteDto);
                    webSiteMapCreator.fork();
                    tasks.add(webSiteMapCreator);
                });
            }
            Set<Map.Entry<String, PageDto>> entrySet = pagesCollection.entrySet();
            entrySet.forEach(e -> siteMap.add(e.getValue()));

            addResultsFromTasks(siteMap, tasks);
            return new TreeSet<>(siteMap);
        }
        return new TreeSet<>();
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
                    !subPath.contains("#") && !subPath.contains(".pdf")) {
                hrefList.add(subPath);
            }
        });
        return hrefList;
    }

}
