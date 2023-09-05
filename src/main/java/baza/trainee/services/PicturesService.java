package baza.trainee.services;

import baza.trainee.dtos.RequestPictureDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PicturesService {
    List<String> addAllPictures(List<MultipartFile> pictures);

    List<Boolean> deleteAllPictures(List<String> pictures);

    List<String> changeAllPictures(List<RequestPictureDto> pictures);
}
