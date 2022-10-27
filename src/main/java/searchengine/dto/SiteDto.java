package searchengine.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SiteDto {
    private Integer id;
    private String name;
    private String url;

    public SiteDto() {
    }

    public SiteDto(String url, String name) {
        this.url = url;
        this.name = name;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteDto siteDto = (SiteDto) o;
        return Objects.equals(name, siteDto.name) && Objects.equals(url, siteDto.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
