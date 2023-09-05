package baza.trainee.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RequestPicturesDto {
    List<RequestPictureDto> pictures;

    public List<String> getOldPaths(){
        return pictures.stream().map(RequestPictureDto::getOldPath).toList();
    }

    public List<MultipartFile> getFiles(){
        return pictures.stream().map(RequestPictureDto::getNewFile).toList();
    }
}
