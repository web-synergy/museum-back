package baza.trainee.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestPictureDto {
    private String oldPath;
    private MultipartFile newFile;
}
