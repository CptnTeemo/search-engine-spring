package searchengine.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import searchengine.dto.LemmaDto;
import searchengine.dto.PageDto;
import searchengine.entity.Lemma;
import searchengine.morphology.LemmaCollection;
import searchengine.morphology.Lemmatizer;

import java.util.*;

@Component
@RequiredArgsConstructor
public class LemmaCollector {

    private List<LemmaDto> lemmaDtoList;
    private Map<String, LemmaCollection> lemmaMap;

    public List<LemmaDto> getLemmaDtoList() {
        return lemmaDtoList;
    }

    public Map<String, LemmaCollection> getLemmaMap() {
        return lemmaMap;
    }

    public void run(Set<PageDto> pageDtoList) {
        lemmaMap = new HashMap<>();
        lemmaMap = Lemmatizer.getLemmaCollections(pageDtoList);
        lemmaDtoList = new ArrayList<>();

        HashMap<String, Integer> lemmaList = new HashMap<>();

        List<LemmaCollection> lemmaCollectionList = new ArrayList<>(lemmaMap.values());

        for (LemmaCollection lemmaCollection : lemmaCollectionList) {
            Map<String, Long> titleList = lemmaCollection.getTitle();
            Map<String, Long> bodyList = lemmaCollection.getBody();

            Set<String> allWords = new HashSet<>();
            allWords.addAll(titleList.keySet());
            allWords.addAll(bodyList.keySet());

            for (String word : allWords) {
                int frequency = lemmaList.getOrDefault(word, 0);
                lemmaList.put(word, frequency + 1);
            }

            for (String lemma : lemmaList.keySet()) {
                var frequency = lemmaList.get(lemma);
                lemmaDtoList.add(new LemmaDto(lemma, frequency, lemmaCollection.getSiteId()));
            }
        }
    }
}
