package searchengine.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
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
