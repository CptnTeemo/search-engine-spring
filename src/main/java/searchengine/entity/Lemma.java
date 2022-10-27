package searchengine.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "lemma")
public class Lemma implements Comparable<Lemma> {

    @Id
    @Column(nullable = false)
    private int id;
    @Column(name = "lemma", nullable = false)
    private String lemma;
    @Column(name = "frequency", nullable = false)
    private int frequency;
    @Column(name = "site_id")
    private Integer siteId;
    @ManyToOne
    @JoinColumn(name = "site_id", insertable = false, updatable = false)
    private Site site;
    @OneToMany(mappedBy = "lemma", cascade = CascadeType.ALL)
    private Set<IndexEntity> indexes = new HashSet<>();

    public Lemma() {
    }

    public Lemma(String lemma, Integer frequency, Integer siteId) {
        this.lemma = lemma;
        this.siteId = siteId;
        this.frequency = frequency;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lemma lemma1 = (Lemma) o;
        return Objects.equals(lemma, lemma1.lemma) && Objects.equals(siteId, lemma1.siteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lemma, siteId);
    }

    @Override
    public int compareTo(Lemma otherLemma) {
        if (this.frequency < otherLemma.getFrequency()) {
            return -1;
        } else if (this.frequency > otherLemma.getFrequency()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Лемма: " + getLemma()
//                + ", частота: " + getFrequency() + "\n" +
//                "страницы: " + getIndexes().size() + "\n" +
//                "путь страницы 0: " + getIndexes().size()
                ;
    }
}
