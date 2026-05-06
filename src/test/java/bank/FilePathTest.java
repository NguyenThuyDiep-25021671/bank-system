package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathTest {

  @Test
  public void testCrossPlatformPath() {
    String directory = "documents";
    String fileName = "report.txt";

    File file = new File(directory, fileName);

    // CÁCH 1: Sử dụng File.separator (Java sẽ tự động chọn '\' cho Windows và '/' cho Linux/Mac)
    String safePathOldSchool = directory + File.separator + fileName;
    assertEquals(file.getPath(), safePathOldSchool, "Test cách 1: Dùng File.separator");

    // CÁCH 2: Sử dụng API java.nio.file.Path (Cách hiện đại, khuyên dùng từ Java 8 trở lên)
    //Path modernPath = Paths.get(directory, fileName);
    //assertEquals(file.getPath(), modernPath.toString(), "Test cách 2: Dùng thư viện NIO Path");
  }
}