package baza.trainee.services.impls;

import baza.trainee.dtos.RequestPictureDto;
import baza.trainee.exceptions.StorageException;
import baza.trainee.services.PictureService;
import baza.trainee.services.PicturesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PicturesServiceImpl implements PicturesService {

    private final PictureService pictureService;

    @Override
    public List<String> addAllPictures(List<MultipartFile> pictures, String ownDir){
        return pictures.stream().map(newFile -> {
            try {
                return pictureService.addPicture(newFile, ownDir);
            } catch (StorageException e) {
                log.error("Not create file " + newFile.getOriginalFilename());
            }
            return null;
        }).toList();
    }

    @Override
    public List<Boolean> deleteAllPictures(List<String> pictures) {
        return pictures.stream().map(oldPath -> {
            try {
                return pictureService.deletePicture(oldPath);
            } catch (StorageException e) {
                log.error("Not delete file " + oldPath);
            }
            return null;
        }).toList();
    }

    @Override
    public List<String> changeAllPictures(List<RequestPictureDto> pictures) {
        return pictures.stream().map(changeFiles -> {
            try {
                return pictureService.changePicture(changeFiles.getOldPath(), changeFiles.getNewFile());
            } catch (StorageException e) {
                log.error("Not change file " + changeFiles.getOldPath() );
            }
            return null;
        }).toList();
    }
}
