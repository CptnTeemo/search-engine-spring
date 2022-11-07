package searchengine.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "page")
public class PageEntity {

    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(name = "path", nullable = false, columnDefinition = "VARCHAR(500)")
    private String path;
    @Column(name = "code", nullable = false)
    private Integer code;
    @Column(name = "content", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;
    @Column(name = "site_id")
    private Integer siteId;
    @ManyToOne
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    private Site site;
    @OneToMany(mappedBy = "pageEntity", cascade = CascadeType.ALL)
    private Set<IndexEntity> indexes = new HashSet<>();

    public PageEntity() {
    }

    public PageEntity(String path, Integer code, String content) {
        this.path = path;
        this.code = code;
        this.content = content;
        this.id = hashCode();
    }

    public PageEntity(String path, Integer code, String content, Integer siteId) {
        this.path = path;
        this.code = code;
        this.content = content;
        this.siteId = siteId;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageEntity that = (PageEntity) o;
        return Objects.equals(path, that.path) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, content);
    }

    @Override
    public String toString() {
        return getPath();
    }
}
