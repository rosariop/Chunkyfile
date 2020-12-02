package de.rosariop.chunkyfile.bucket;

import de.rosariop.chunkyfile.exception.BucketExistsException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestScoped
public class BucketService {

    @ConfigProperty(name = "config.basePath")
    String basePath;

    public void createBucket(String bucketName) throws IOException {
        Path absoluteBucketPath = Paths.get(basePath+"/"+bucketName);
        if (!Files.exists(absoluteBucketPath)){
            Files.createDirectory(absoluteBucketPath);
        } else {
            System.out.println("Bucket already existed.");
        }
    }

    public void deleteBucket(String bucketName) throws IOException {
        Path absoluteBucketPath = Paths.get(basePath+"/"+bucketName);
        if(Files.exists(absoluteBucketPath)){
            Files.delete(absoluteBucketPath);
        } else {
            System.out.println("Bucket does not exist.");
        }
    }

    public void renameBucket(String oldBucketName, String newBucketName) throws IOException {
        Path oldAbsoluteBucketPath = Paths.get(basePath+"/"+oldBucketName);
        Path newAbsoluteBucketPath = Paths.get(basePath+"/"+newBucketName);

        if(Files.exists(oldAbsoluteBucketPath) && !Files.exists(newAbsoluteBucketPath)){
            Files.move(oldAbsoluteBucketPath,newAbsoluteBucketPath);
        }
    }
}
