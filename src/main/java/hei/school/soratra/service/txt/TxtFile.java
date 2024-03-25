package hei.school.soratra.service.txt;

import hei.school.soratra.repository.TxtRepository;
import hei.school.soratra.repository.model.Txt;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TxtFile {
  private final TxtRepository repository;

  @Transactional
  public Txt save(Txt txtFile) {
    return repository.save(txtFile);
  }

  public Txt getTxtFileById(String id) {
    return repository.findById(id).orElse(null);
  }
}
