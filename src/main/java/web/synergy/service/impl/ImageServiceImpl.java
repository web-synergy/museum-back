package web.synergy.service.impl;

import web.synergy.config.ImageCompressionConfig;
import web.synergy.config.StorageProperties;
import web.synergy.dto.SaveImageResponse;
import web.synergy.service.ImageService;
import web.synergy.utils.FileSystemStorageUtils;
import web.synergy.utils.ImageCompressor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static web.synergy.service.impl.ImageServiceImpl.ImageType.ORIGINAL;
import static web.synergy.service.impl.ImageServiceImpl.ImageType.PREVIEW;

/**
 * Implementation of the {@link ImageService} interface for managing
 * image-related operations.
 * This service handles loading, storing, and processing images.
 *
 * @author Evhen Malysh
 */
@Service
public class ImageServiceImpl implements ImageService {

    private final int previewWidth;
    private final int desktopWidth;
    private final float previewQuality;
    private final float desktopQuality;

    private final Path rootPath;
    private final Path imagesPath;
    private final Path tempPath;

    private final String originalDirName;
    private final String previewDirName;

    /**
     * Constructs an instance of the ImageServiceImpl class.
     *
     * @param storageProperties Image storage configuration properties.
     * @param compressionConfig Image compression properties.
     */
    public ImageServiceImpl(
            final StorageProperties storageProperties,
            final ImageCompressionConfig compressionConfig) {
        this.rootPath = Paths.get(storageProperties.getRootImageLocation());
        this.imagesPath = rootPath.resolve(storageProperties.getPersistImageLocation());
        this.tempPath = rootPath.resolve(storageProperties.getTempImagesLocation()).normalize();

        this.originalDirName = storageProperties.getOriginalImagesLocation();
        this.previewDirName = storageProperties.getCompressedImagesLocation();

        this.previewWidth = compressionConfig.getPreviewWidth();
        this.previewQuality = compressionConfig.getPreviewQuality();
        this.desktopWidth = compressionConfig.getDesktopWidth();
        this.desktopQuality = compressionConfig.getDesktopQuality();

        FileSystemStorageUtils.init(rootPath, tempPath);
    }

    /**
     * Load a resource (image) by filename and type.
     *
     * @param fileId The name of the file.
     * @param type   The type of the resource (either "preview" or "original").
     * @return A byte array containing the resource data.
     */
    @Override
    public byte[] loadResource(final String fileId, final String type) {
        var currentPath = getCurrentPath(imagesPath, fileId, type);

        return getResourceFromPath(currentPath);
    }

    /**
     * Load a temporary resource (image) by filename and temp folder name.
     *
     * @param fileId   The name of the file.
     * @param username Username associated with the temporary
     *                 resource.
     * @return A byte array containing the temporary resource data.
     */
    @Override
    public byte[] loadTempResource(
            final String fileId,
            final String username,
            final String type) {
        var tempSessionPath = tempPath.resolve(username);
        var currentPath = getCurrentPath(tempSessionPath, fileId, type);

        return getResourceFromPath(currentPath);
    }

    /**
     * Convert and store an image from multipart file as a temporary resources.
     *
     * @param file       The image as {@link MultipartFile} to store.
     * @param tempFolder Temp folder name to create for temporary resources.
     * @return The ID of the stored file that represent name of subfolder
     *         with converted images.
     */
    @Override
    public SaveImageResponse storeToTemp(final MultipartFile file, final String tempFolder) {
        var imageId = UUID.randomUUID().toString();
        var userTempPath = this.tempPath.resolve(tempFolder).resolve(imageId);
        convertAndStore(userTempPath, file, ORIGINAL, PREVIEW);

        var response = new SaveImageResponse();
        response.setImageId(imageId);
        response.setOriginalUrl("/api/admin/images/temp?filename=" + imageId + "&type=" + ORIGINAL.getValue());

        return response;
    }

    /**
     * Persist image by ID from temp folder to image folder.
     *
     * @param imageId   The list of filenames to persist and process.
     * @param tempFolder Folder associated with the temp files.
     */
    @Override
    public void persist(final String imageId, final String tempFolder) {
        var sessionPath = tempPath.resolve(tempFolder);

        var imageTempPath = FileSystemStorageUtils.loadPath(sessionPath).resolve(imageId);
        var destPath = imagesPath.resolve(imageId);
        FileSystemStorageUtils.copyRecursively(imageTempPath, destPath);
        FileSystemStorageUtils.deleteRecursively(sessionPath);
    }

    /**
     * Delete existing image.
     *
     * @param imageId ID of image to delete.
     */
    @Override
    public void deleteImage(String imageId) {
        var pathToDelete = imagesPath.resolve(imageId);
        FileSystemStorageUtils.deleteRecursively(pathToDelete);
    }

    private Path getCurrentPath(final Path root, final String fileId, final String type) {
        var imageType = ImageType.fromString(type);

        var currentDir = switch (imageType) {
            case ORIGINAL -> originalDirName;
            case PREVIEW -> previewDirName;
        };

        return root.resolve(fileId).resolve(currentDir);
    }

    private void convertAndStore(Path basePath, MultipartFile image, ImageType... types) {
        for (var imageType : types) {
            Path directPath = switch (imageType) {
                case ORIGINAL -> basePath.resolve(originalDirName);
                case PREVIEW -> basePath.resolve(previewDirName);
            };
            MultipartFile compressedFile = switch (imageType) {
                case ORIGINAL -> ImageCompressor.compress(
                        image,
                        desktopWidth,
                        desktopQuality);
                case PREVIEW -> ImageCompressor.compress(
                        image,
                        previewWidth,
                        previewQuality);
            };
            FileSystemStorageUtils.storeToPath(compressedFile, directPath);
        }
    }

    private byte[] getResourceFromPath(final Path currentPath) {
        var file = FileSystemStorageUtils.loadFilePath(currentPath);
        var resource = FileSystemStorageUtils.getResource(file.toUri());

        return FileSystemStorageUtils.getByteArrayFromResource(resource);
    }

    @Getter
    @RequiredArgsConstructor
    public enum ImageType {
        ORIGINAL("ORIGINAL"),
        PREVIEW("PREVIEW");

        private final String value;

        public static ImageType fromString(String val) {
            return switch (val) {
                case "ORIGINAL" -> ImageType.ORIGINAL;
                case "PREVIEW" -> ImageType.PREVIEW;
                default -> throw new IllegalArgumentException("No enum of value: " + val);
            };
        }
    }

}
