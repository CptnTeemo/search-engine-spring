package searchengine.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.entity.Lemma;
import searchengine.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.entity.Site;

import java.util.Collection;
import java.util.List;

@Repository
public interface PagesRepository extends JpaRepository<PageEntity, Integer> {
    List<PageEntity> findBySite(Site site);
    Long countBySite(Site site);

    @Query(value = "SELECT * FROM page p JOIN index_table i ON p.id = i.page_id WHERE i.lemma_id IN :lemmas", nativeQuery = true)
    List<PageEntity> findByLemmaList(@Param("lemmas") Collection<Lemma> lemmaList);
}
