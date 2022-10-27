package searchengine.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "index_table")
public class IndexEntity {

    @Id
    @Column(nullable = false)
    private int id;

    @Column(name = "page_id", nullable = false)
    private Integer pageId;
    @Column(name = "lemma_id", nullable = false)
    private Integer lemmaId;
    @Column(name = "rank_rate", nullable = false)
    private float rank;

    @ManyToOne
    @JoinColumn(name = "page_id", insertable = false, updatable = false)
    private PageEntity pageEntity;

    @ManyToOne
    @JoinColumn(name = "lemma_id", insertable=false, updatable=false)
    private Lemma lemma;

    public IndexEntity() {
    }

    public IndexEntity(Integer pageId, Integer lemmaId, float rank) {
        this.pageId = pageId;
        this.lemmaId = lemmaId;
        this.rank = rank;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexEntity that = (IndexEntity) o;
        return Objects.equals(pageId, that.pageId) && Objects.equals(lemmaId, that.lemmaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageId, lemmaId);
    }
}
