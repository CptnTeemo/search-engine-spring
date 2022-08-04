package searchengine.utils;

import searchengine.dto.PageDto;
import searchengine.dto.SiteDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class LinkCollector extends RecursiveTask<Set<PageDto>> {

    private String path;
    public static String mainPath = PageDto.getPath();
    private static final String PATH = "./data/";
    private PageDto pageDto;
    private SiteDto siteDto;
    private static Map<String, PageDto> pagesCollection = new HashMap<>();

//    public LinkCollector(String path) {
//        this.path = path;
//    }

    public LinkCollector(String path, SiteDto siteDto) {
        this.path = path;
        this.siteDto = siteDto;
    }

    public static void setMainPath(String mainPath) {
        LinkCollector.mainPath = mainPath;
    }

    @Override
    protected Set<PageDto> compute() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document document = null;
        if (!path.matches("(http|https)(.*)") & !path.contains(mainPath)) {
//            return new ArrayList<>();
            return new TreeSet<>();
        }
        try {
            document = Jsoup.connect(path)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT" +
                            "5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            Connection.Response response = Jsoup.connect(path)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT" +
                            "5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .execute();
//            System.out.println(response.statusCode());


            if (document != null) {

                Elements linkList = document.select("a");
                List<LinkCollector> tasks = new ArrayList<>();
                Set<PageDto> siteMap = new TreeSet<>();
                Integer statusCode = response.statusCode();
                String getUrl = mainPath + "(.*)";
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
//                    siteMap.add(new WebSiteElementInfo(path, document.html()));
//                    siteMap.addAll(hrefList);
                }
                Set<Map.Entry<String, PageDto>> entrySet = pagesCollection.entrySet();
                entrySet.forEach(e -> siteMap.add(e.getValue()));

                addResultsFromTasks(siteMap, tasks);
                return new TreeSet<>(siteMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TreeSet<>();
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
