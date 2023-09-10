package baza.trainee.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomMultipartFile implements MultipartFile {
    private final String name;
    private final String originalFilename;
    @Nullable
    private final String contentType;
    private final byte[] content;

    /**
     * Constructs a CustomMultipartFile with the specified attributes.
     *
     * @param name             The name of the file parameter.
     * @param originalFilename The original filename of the uploaded file, or an empty string if not available.
     * @param contentType      The content type (MIME type) of the uploaded file, or null if not available.
     * @param content          The byte array representing the content of the uploaded file.
     * @throws IllegalArgumentException if the 'name' parameter is empty.
     */
    public CustomMultipartFile(final String name, @Nullable final String originalFilename,
                               @Nullable final String contentType,
                               @Nullable final byte[] content) {
        Assert.hasLength(name, "Name must not be empty");
        this.name = name;
        this.originalFilename = originalFilename != null ? originalFilename : "";
        this.contentType = contentType;
        this.content = content != null ? content : new byte[0];
    }

    /**
     * Constructs a CustomMultipartFile with the specified attributes by reading the content from an InputStream.
     *
     * @param name             The name of the file parameter.
     * @param originalFilename The original filename of the uploaded file, or an empty string if not available.
     * @param contentType      The content type (MIME type) of the uploaded file, or null if not available.
     * @param contentStream    An InputStream containing the content of the uploaded file.
     * @throws IOException              If an I/O error occurs while reading from the contentStream.
     * @throws IllegalArgumentException if the 'name' parameter is empty.
     */
    public CustomMultipartFile(final String name, @Nullable final String originalFilename,
                               @Nullable final String contentType,
                               final InputStream contentStream) throws IOException {
        this(name, originalFilename, contentType, FileCopyUtils.copyToByteArray(contentStream));
    }

    @Override
    public @NonNull String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.content.length == 0;
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public @NonNull byte[] getBytes() {
        return this.content;
    }

    @Override
    public @NonNull InputStream getInputStream() {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(@NonNull final File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.content, dest);
    }
}

