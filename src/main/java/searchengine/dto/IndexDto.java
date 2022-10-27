package searchengine.dto;

import lombok.Getter;
import lombok.Setter;
import searchengine.entity.IndexEntity;
import searchengine.entity.Lemma;
import searchengine.entity.PageEntity;

import java.util.Objects;

@Getter
@Setter
public class IndexDto {

    private Integer pageId;
    private Integer lemmaId;
    private float rank;
    private Integer id;

    public IndexDto() {
    }

    public IndexDto(Integer pageId, Integer lemmaId, float rank) {
        this.pageId = pageId;
        this.lemmaId = lemmaId;
        this.rank = rank;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexDto that = (IndexDto) o;
        return Objects.equals(pageId, that.pageId) && Objects.equals(lemmaId, that.lemmaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageId, lemmaId);
    }
}
