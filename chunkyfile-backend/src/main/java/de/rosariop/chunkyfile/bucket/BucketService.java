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

    public void createBucket(String bucketName) throws BucketExistsException, IOException {
        Path absoluteBucketPath = Paths.get(basePath+"/"+bucketName);
        if (!Files.exists(absoluteBucketPath)){
            Files.createDirectory(absoluteBucketPath);
        } else {
            throw new BucketExistsException();
        }
    }
}
