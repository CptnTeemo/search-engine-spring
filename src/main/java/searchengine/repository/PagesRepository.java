package searchengine.repository;

import searchengine.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagesRepository extends JpaRepository<PageEntity, Integer> {
}
