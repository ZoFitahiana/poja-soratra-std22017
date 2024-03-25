package hei.school.soratra.service.txt;

import hei.school.soratra.file.BucketComponent;
import hei.school.soratra.repository.TxtRepository;
import hei.school.soratra.repository.model.Txt;
import jakarta.ws.rs.BadRequestException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TxtFileService {
  BucketComponent bucketComponent;
  TxtRepository txtRepository;

  public File uploadTxtFile(String id, byte[] file) {
    try {
      if (file == null) {
        throw new BadRequestException("File is mandatory");
      }
      String fileSuffix = ".txt";
      String inputFilePrefix = id + fileSuffix;
      File tmpFile;
      try {
        tmpFile = File.createTempFile(inputFilePrefix, fileSuffix);
      } catch (IOException e) {
        throw new RuntimeException("Creation of temp file failed");
      }
      writeFileFromByteArray(file, tmpFile);
      String bucketKey = getTxtFileBucketName(inputFilePrefix);
      uploadFile(tmpFile, bucketKey);
      Txt toSave = new Txt(id, bucketKey);
      txtRepository.save(toSave);
      return bucketComponent.download(bucketKey);
    } catch (BadRequestException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private void uploadFile(File file, String bucketKey) {
    bucketComponent.upload(file, bucketKey);
    file.delete();
  }

  private String getTxtFileBucketName(String filename) {
    return "txt/" + filename;
  }

  public String getSignedUrl(Optional<Txt> txtFile) {
    if (txtFile.isPresent()) {
      Txt file = txtFile.get();
      try {
        return bucketComponent.presign(file.getTxtBucketKey(), Duration.ofHours(12)).toString();
      } catch (Exception e) {
        e.printStackTrace();

        return null;
      }
    } else {

      return null;
    }
  }

  private File writeFileFromByteArray(byte[] bytes, File file) {
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(bytes);
      return file;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getTxtFileUrlById(String id) {
    Optional<Txt> txtFile = txtRepository.findById(id);
    if (txtFile == null) {
      return null;
    }
    return getSignedUrl(txtFile);
  }
}
