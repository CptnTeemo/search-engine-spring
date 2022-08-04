package searchengine.dto;

import java.util.Objects;

public class PageDto implements Comparable<PageDto> {

    private static String path;
    private String url;
    private String html;
    private Integer statusCode;
    private Integer id;
    private Integer siteId;

    public PageDto(String url, String html, Integer statusCode, Integer siteId) {
        this.url = url;
        this.html = html;
        this.statusCode = statusCode;
        this.siteId = siteId;
        this.id = hashCode();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getUrl() {
        return url;
    }

    public String getHtml() {
        return html;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        PageDto.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @Override
    public int compareTo(PageDto o) {
        return url.compareTo(o.getUrl());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDto page = (PageDto) o;
        return Objects.equals(url, page.url) && Objects.equals(html, page.html);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, html);
    }
}
