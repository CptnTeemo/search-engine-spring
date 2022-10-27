package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.entity.IndexEntity;
import searchengine.entity.Lemma;
import searchengine.entity.PageEntity;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<IndexEntity, Integer> {
    @Query(value = "SELECT * FROM index_table i WHERE i.lemma_id IN :lemmas AND i.page_id IN :pages", nativeQuery = true)
    List<IndexEntity> findByPagesAndLemmas(@Param("lemmas") List<Lemma> lemmaList, @Param("pages") List<PageEntity> pageList);
}
