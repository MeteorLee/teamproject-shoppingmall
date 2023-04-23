package project.finalproject1backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;

@SpringBootTest
@WebAppConfiguration
public class AwsS3Test {
    @Autowired
    private AwsS3 awsS3 = AwsS3.getInstance();

    @Test
    public void upload() {
        File file = new File("C://upload/s3uploadtest.png");
        String key = "img/my-img.png";
        awsS3.upload(file, key);
    }
    @Test
    public void copy() {
        File file = new File("C://upload/s3uploadtest.png");
        String orgKey = "img/my-img.png";
        String copyKey = "img/my-img-copy.png";
        awsS3.copy(orgKey, copyKey);
    }
    @Test
    public void delete() {
        File file = new File("C://upload/s3uploadtest.png");
        String key = "img/my-img.png";
        awsS3.delete(key);
    }
}
