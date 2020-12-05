package de.rosariop.chunkyfile.bucket;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class BucketService {

    private static final Logger LOG = Logger.getLogger(BucketService.class);

    @ConfigProperty(name = "config.basePath")
    String basePath;

    @ConfigProperty(name = "config.delimiter")
    String delimiter;


    public void createBucket(String bucketName) throws IOException {
        Path absoluteBucketPath = Paths.get(basePath + delimiter + bucketName);
        if (!Files.exists(absoluteBucketPath)) {
            Files.createDirectory(absoluteBucketPath);
        } else {
            LOG.debug("Bucket already existed.");
        }
    }

    public List<String> getFileNamesFromBucket(String bucketName) throws IOException {
        Path absoluteBucketFolderPath = Paths.get(basePath + delimiter + bucketName + delimiter);
        List<String> files = new ArrayList<>();
        if (Files.exists(absoluteBucketFolderPath)) {
            try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(absoluteBucketFolderPath)){
                for (Path filePath : directoryStream) {
                    files.add(filePath.getFileName().toString());
                }
            }
        }
        return files;
    }

    public void deleteBucket(String bucketName) throws IOException {
        Path absoluteBucketPath = Paths.get(basePath + delimiter + bucketName);
        if (Files.exists(absoluteBucketPath)) {
            Files.delete(absoluteBucketPath);
        } else {
            LOG.debug("Bucket does not exist.");
        }
    }

    public void renameBucket(String oldBucketName, String newBucketName) throws IOException {
        Path oldAbsoluteBucketPath = Paths.get(basePath + delimiter + oldBucketName);
        Path newAbsoluteBucketPath = Paths.get(basePath + delimiter + newBucketName);

        if (Files.exists(oldAbsoluteBucketPath) && !Files.exists(newAbsoluteBucketPath)) {
            Files.move(oldAbsoluteBucketPath, newAbsoluteBucketPath);
        }
    }
}
