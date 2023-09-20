package baza.trainee.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ResourcePictureServiceTest {
    @Autowired
    ResourcePictureService service;

    @Test
    @SneakyThrows
    void loadAsResource() {
        byte[] content = service.loadAsResource("original", "noImages.jpg");
        assertTrue(content.length > 0);
    }
}