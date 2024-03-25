package hei.school.soratra.endpoint.rest.controller.health;

import hei.school.soratra.service.txt.TxtFileService;
import java.io.File;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TxtController {

  private final TxtFileService service;

  public TxtController(TxtFileService service) {
    this.service = service;
  }

  @PutMapping(value = "/upload-txt/{id}")
  public ResponseEntity<String> uploadTxtFile(
      @PathVariable(name = "id") String id, @RequestBody(required = false) byte[] file) {

    File output = service.uploadTxtFile(id, file);
    if (output == null) {
      return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("");
    }
    return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("OK");
  }

  @GetMapping(value = "/txt-file/{id}")
  public ResponseEntity<String> getTxtFileById(@PathVariable(name = "id") String id) {
    String txtUrl = service.getTxtFileUrlById(id);
    if (txtUrl == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(txtUrl);
  }
}
