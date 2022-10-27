package searchengine.morphology;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.morphology.Morphology;
import org.springframework.stereotype.Component;
import searchengine.dto.PageDto;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Lemmatizer {

    private static LuceneMorphology luceneMorph;

    static {
        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, LemmaCollection> getLemmaCollections(Set<PageDto> set) {
        Map<String, LemmaCollection> lemmaSet = new HashMap<>();
        set.forEach(e -> {
                    LemmaCollection lemma = new LemmaCollection(uniqueWordCounter(getTitleText(e.getHtml())),
                            uniqueWordCounter(getBodyText(e.getHtml())), e.getId(), e.getSiteId());
                    lemmaSet.put(e.getUrl(), lemma);
                    if (lemmaSet.size() % 25 == 0) {
                        log.info("Лемматизация в процессе - " + e.getUrl() + " " + e.getSiteId() + " " + e.getId());
                    }
        });
        return new HashMap<>(lemmaSet);
    }

    public static Map<String, Long> uniqueWordCounter(String text) {
        List<String> words = Arrays.asList(text.toLowerCase(Locale.ROOT).split("[^А-яЁё]"));
        List<String> uniqueWords = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty() && word.matches("[а-яё]+")) {
                List<String> wordBaseForms = luceneMorph.getMorphInfo(word);
                for (String wordBase : wordBaseForms) {
//                    if (!isServiceWord(wordBase))
                    if (!wordBase.contains("ПРЕДЛ") && !wordBase.contains("СОЮЗ") &&
                            !wordBase.contains("МЕЖД") && !wordBase.contains("ЧАСТ")) {
                        uniqueWords.add(wordBase.replaceAll("([А-яЁё]+)(|)(.*)", "$1"));
                    }
                }
            }
        }
        return countDuplicates(uniqueWords);
    }

    public static Map<String, Long> countDuplicates(List<String> inputList) {
        return inputList.stream().collect(Collectors.toMap(Function.identity(), v -> 1L, Long::sum));
    }

    public static String getBodyText(String html) {
        Document htmlContent = Jsoup.parse(html);
        return htmlContent.select("body").text();
    }

    public static String getTitleText(String html) {
        Document htmlContent = Jsoup.parse(html);
        return htmlContent.select("title").text();
    }

    public List<String> getLemma(String word) {
        List<String> lemmaList = new ArrayList<>();
        try {
            var baseRusForm = luceneMorph.getNormalForms(word);
            if (!isServiceWord(word)) {
                lemmaList.addAll(baseRusForm);
            }
        } catch (Exception e) {
            log.debug("Символ не найден - " + word);
        }
        return lemmaList;
    }

    public List<Integer> findLemmaIndexInText(String content, String lemma) {
        List<Integer> lemmaIndexList = new ArrayList<>();
        var elements = content.toLowerCase(Locale.ROOT).split("\\p{Punct}|\\s");
        int index = 0;
        for (String el : elements) {
            var lemmas = getLemma(el);
            for (String lem : lemmas) {
                if (lem.equals(lemma)) {
                    lemmaIndexList.add(index);
                }
            }
            index += el.length() + 1;
        }
        return lemmaIndexList;
    }

    private static boolean isServiceWord(String word) {
        var morphForm = luceneMorph.getMorphInfo(word);
        for (String l : morphForm) {
            if (l.contains("ПРЕДЛ")
                    || l.contains("СОЮЗ")
                    || l.contains("МЕЖД")
                    || l.contains("МС")
                    || l.contains("ЧАСТ")
                    || l.length() <= 3) {
                return true;
            }
        }
        return false;
    }

}
