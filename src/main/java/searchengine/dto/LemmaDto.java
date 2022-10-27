package searchengine.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class LemmaDto implements Comparable<LemmaDto>{
    private int id;
    private String lemma;
    private int frequency;
    private int siteId;

    public LemmaDto() {
    }

    public LemmaDto(String lemma, Integer frequency, Integer siteId) {
        this.lemma = lemma;
        this.siteId = siteId;
        this.frequency = frequency;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LemmaDto lemmaDto = (LemmaDto) o;
        return siteId == lemmaDto.siteId && Objects.equals(lemma, lemmaDto.lemma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lemma, siteId);
    }

    @Override
    public int compareTo(LemmaDto otherLemma) {
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
        return "Лемма: " + getLemma() + ", частота: " + getFrequency();
    }

}
