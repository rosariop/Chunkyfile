package de.rosariop.chunkyfile.bucket;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestScoped
public class BucketService {

    private static final Logger LOG = Logger.getLogger(BucketService.class);

    @ConfigProperty(name = "config.basePath")
    String basePath;

    @ConfigProperty(name = "config.delemitter")
    String delemitter;


    public void createBucket(String bucketName) throws IOException {
        Path absoluteBucketPath = Paths.get(basePath+delemitter+bucketName);
        if (!Files.exists(absoluteBucketPath)){
            Files.createDirectory(absoluteBucketPath);
        } else {
            LOG.debug("Bucket already existed.");
        }
    }

    public void deleteBucket(String bucketName) throws IOException {
        Path absoluteBucketPath = Paths.get(basePath+delemitter+bucketName);
        if(Files.exists(absoluteBucketPath)){
            Files.delete(absoluteBucketPath);
        } else {
            LOG.debug("Bucket does not exist.");
        }
    }

    public void renameBucket(String oldBucketName, String newBucketName) throws IOException {
        Path oldAbsoluteBucketPath = Paths.get(basePath+delemitter+oldBucketName);
        Path newAbsoluteBucketPath = Paths.get(basePath+delemitter+newBucketName);

        if(Files.exists(oldAbsoluteBucketPath) && !Files.exists(newAbsoluteBucketPath)){
            Files.move(oldAbsoluteBucketPath,newAbsoluteBucketPath);
        }
    }
}
