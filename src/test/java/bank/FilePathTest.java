package bank;

import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertEquals;

import java.io.File;

public class FilePathTest {

  @Test
  public void testHardcodedWindowsPath() {
    String directory = "documents";
    String fileName = "report.txt";

    // CỐ TÌNH TẠO LỖI: Nối chuỗi bằng dấu gạch chéo ngược '\' đặc trưng của Windows
    String hardcodedPath = directory + "\\" + fileName;

    // Tạo một đối tượng File chuẩn của Java
    File file = new File(directory, fileName);

    // Kiểm tra xem đường dẫn ghép tay có giống đường dẫn chuẩn của hệ thống không
    assertEquals(hardcodedPath, file.getPath(),
        "Đường dẫn ghép tay phải khớp với đường dẫn hệ thống sinh ra");
  }
}