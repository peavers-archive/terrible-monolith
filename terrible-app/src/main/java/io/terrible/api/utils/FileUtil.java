package io.terrible.api.utils;

import io.terrible.api.domain.MediaFile;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class FileUtil {

    /** System key value for home directory. */
    private final String USER_HOME_PROPERTY = "user.home";

    /**
     * Location on the filesystem relative to the home directory to store thumbnails, if not provided
     * when setting the directory.
     */
    private final String THUMBNAIL_DIRECTORY = ".terrible/_thumbnails/";

    public String getThumbnailDirectory(final MediaFile mediaFile) {

        final File file = new File(System.getProperty(USER_HOME_PROPERTY), THUMBNAIL_DIRECTORY + mediaFile.getId());

        return createDirectory(file);
    }

    /**
     * Create a new directory if possible on the host filesystem. The input path will be rejected if
     * permissions fail or the IO is unable to create. If the file directory already exists, the path
     * is returned and no IO operations are preformed.
     *
     * @param file
     * @return the path of the new directory
     * @throws IOException if unable to create the directory for any reason
     */
    private String createDirectory(File file) {

        if (Files.exists(file.toPath())) {
            return file.getAbsolutePath();
        }

        if (!file.mkdirs()) {

        }

        return file.getAbsolutePath();
    }
}
