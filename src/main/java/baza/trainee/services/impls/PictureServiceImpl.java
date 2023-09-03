package baza.trainee.services.impls;

import baza.trainee.services.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class PictureServiceImpl implements PictureService {
    public static final String USER_DIR = "user.dir";
    @Value("${uploads.path}")
    private String uploadPath;

    @Override
    public String addPicture(MultipartFile newPicture) throws IOException {
        /*Create new dir*/
        LocalDate currentDate = LocalDate.now();
        String newDir = File.separator + currentDate.getYear() + File.separator + currentDate.getMonthValue();
        String newFullDir = System.getProperty(USER_DIR) + uploadPath + newDir;

        return createFile(newPicture, newFullDir);
    }

    private String createFile(MultipartFile newPicture, String dir)
            throws IOException {
        if (newPicture != null && newPicture.getOriginalFilename() != null) {
            File uploadDir = new File(dir);

            if (!uploadDir.exists()) {
                Files.createDirectory(Path.of(dir));
            }

            newPicture.transferTo(new File(dir + File.separator + newPicture.getOriginalFilename()));
            return "/img" + dir + File.separator + newPicture.getOriginalFilename();
        } else {
            throw new IOException("Not file or not file name");
        }
    }

    @Override
    public String changePicture(String oldPath, MultipartFile newPicture)
            throws IOException {
        if (!deletePicture(oldPath)) {throw new IOException("Not delete file");
        }

        String oldFullName = System.getProperty(USER_DIR) + uploadPath + File.separator +
                oldPath.replace("/img/", "");
        String oldDir = new File(oldFullName).getParent();

        return createFile(newPicture, oldDir);
    }

    @Override
    public boolean deletePicture(String oldPath) throws IOException {
        if (oldPath == null || oldPath.isBlank()){return false;}
        String oldFullPath = System.getProperty(USER_DIR)
                + uploadPath + oldPath.replace("img/", "");
        Path pathFile = Path.of(oldFullPath);
        Files.delete(pathFile);
        return !Files.exists(pathFile);
    }
}
