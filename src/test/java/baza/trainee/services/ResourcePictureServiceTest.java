package baza.trainee.services;

import baza.trainee.enums.TypePicture;
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

        byte[] content = service.getPicture(TypePicture.ORIGINAL, "noImages.jpg");
        assertTrue(content.length > 0);
    }
}