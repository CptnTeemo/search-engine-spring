package searchengine.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.IndexDto;
import searchengine.dto.LemmaDto;
import searchengine.morphology.LemmaCollection;
import searchengine.repository.FieldRepository;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class Indexing {

    private List<IndexDto> indexDtoList;

    public List<IndexDto> getIndexDtoList() {
        return indexDtoList;
    }

    public void run(Map<String, LemmaCollection> lemmaCollectionMap) {
        log.info("Индексируем");
        indexDtoList = new ArrayList<>();
        List<LemmaCollection> lemmaCollectionList = new ArrayList<>();
        lemmaCollectionList.addAll(lemmaCollectionMap.values());

        for (LemmaCollection lemmaCollection : lemmaCollectionList) {
            Map<String, Long> titleList = lemmaCollection.getTitle();
            Map<String, Long> bodyList = lemmaCollection.getBody();
            Set<String> allWords = new HashSet<>();
            allWords.addAll(titleList.keySet());
            allWords.addAll(bodyList.keySet());
            for (String keyWord : allWords) {
                LemmaDto lemmaDto = new LemmaDto(keyWord, 1, lemmaCollection.getSiteId());
                Integer lemmaId = lemmaDto.getId();
                if (titleList.containsKey(keyWord) || bodyList.containsKey(keyWord)) {
                    float totalRank = 0.0F;
                    if (titleList.get(keyWord) != null) {
                        Float titleRank = Float.valueOf(titleList.get(keyWord));
                        totalRank += titleRank;
                    }
                    if (bodyList.get(keyWord) != null) {
                        float bodyRank = (float) (bodyList.get(keyWord) * 0.8);
                        totalRank += bodyRank;
                    }
                    indexDtoList.add(new IndexDto(lemmaCollection.getPageId(), lemmaId, totalRank));
                } else {
                    log.debug("Lemma not found");
                }
            }
        }
    }

}
