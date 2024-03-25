package hei.school.soratra.repository;

import hei.school.soratra.repository.model.Txt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxtRepository extends JpaRepository<Txt, String> {}
