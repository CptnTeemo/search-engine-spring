package searchengine.utils;

import searchengine.dto.IndexDto;
import searchengine.dto.LemmaDto;
import searchengine.dto.PageDto;
import searchengine.entity.IndexEntity;
import searchengine.entity.Lemma;
import searchengine.entity.PageEntity;

public class MappingUtils {

    public static PageEntity pageToPageEntity(PageDto page) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPath(page.getUrl());
        pageEntity.setCode(page.getStatusCode());
        pageEntity.setContent(page.getHtml());
        pageEntity.setId(page.getId());
        pageEntity.setSiteId(page.getSiteId());
        return pageEntity;
    }

    public static PageDto pageEntityToPageDto(PageEntity pageEntity) {
        PageDto page = new PageDto(pageEntity.getPath(), pageEntity.getContent(),
                pageEntity.getCode(), pageEntity.getSiteId());
        return page;
    }

    public static Lemma lemmaDtoToLemma(LemmaDto lemmaDto) {
        Lemma lemma = new Lemma();
        lemma.setLemma(lemmaDto.getLemma());
        lemma.setId(lemmaDto.getId());
        lemma.setFrequency(lemmaDto.getFrequency());
        lemma.setSiteId(lemmaDto.getSiteId());
        return lemma;
    }

    public static IndexEntity indexDtoToIndexEntity(IndexDto indexDto) {
        IndexEntity indexEntity = new IndexEntity();
        indexEntity.setLemmaId(indexDto.getLemmaId());
        indexEntity.setPageId(indexDto.getPageId());
        indexEntity.setRank(indexDto.getRank());
        indexEntity.setId(indexDto.getId());
        return indexEntity;
    }

}
