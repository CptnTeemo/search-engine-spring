package searchengine.entity;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        name = "lemma"
//        ,
//        indexes = {
//                @javax.persistence.Index(name = "LEMMA_IDX0", columnList = "lemma, site_id", unique = true) },
//        uniqueConstraints = {@UniqueConstraint(name = "UniqueLemmaAndSiteId", columnNames = {"lemma", "site_id"})}
)
@SQLInsert(sql = "INSERT INTO lemma(frequency, lemma, site_id, id) VALUES (?, ?, ?, ?)" +
        "ON DUPLICATE KEY UPDATE frequency = frequency + 1")
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
    @OneToMany(mappedBy = "lemma", cascade =
            {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<IndexEntity> indexes = new HashSet<>();

//    @Column(name = "site_id", nullable = false)
//    private int siteId;

    public Lemma() {
    }

    public Lemma(String lemma, Integer frequency, Integer siteId) {
        this.lemma = lemma;
        this.siteId = siteId;
        this.frequency = frequency;
        this.id = hashCode();
    }

    //    public Lemma(String lemma, int frequency, int siteId) {
    public Lemma(String lemma, int frequency) {
        this.lemma = lemma;
        this.frequency = frequency;
//        this.siteId = siteId;
        this.id = hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Set<IndexEntity> getIndexes() {
        return indexes;
    }

    public void setIndexes(Set<IndexEntity> indexes) {
        this.indexes = indexes;
    }

//    public int getSiteId() {
//        return siteId;
//    }

//    public void setSiteId(int siteId) {
//        this.siteId = siteId;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Lemma lemma1 = (Lemma) o;
//        return siteId == lemma1.siteId &&
//                Objects.equals(lemma, lemma1.lemma);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(lemma, siteId);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lemma lemma1 = (Lemma) o;
        return Objects.equals(lemma, lemma1.lemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lemma);
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
