package de.rosariop.chunkyfile.file;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FileService {

    private static final Logger LOG = Logger.getLogger(FileService.class);

    @ConfigProperty(name = "config.delimiter")
    String delimiter;

    @ConfigProperty(name = "config.basePath")
    String basePath;

    public void uploadFile(MultipartFormDataInput input, String bucketName) throws IOException {
        String fileName;
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        for (InputPart inputPart : inputParts) {
            MultivaluedMap<String, String> header = inputPart.getHeaders();
            fileName = this.getFileName(header);

            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] bytes = IOUtils.toByteArray(inputStream);


            String bucketPath = basePath + delimiter + bucketName + delimiter;
            fileName = bucketPath + fileName;

            this.writeFile(bytes, fileName);
            LOG.debug("File Uploaded");
        }
    }

    public void deleteFile(String bucketName, String fileName) throws IOException {
        String absoluteBucketPathString = basePath+ delimiter +bucketName;
        Path absoluteFilePath = Path.of(absoluteBucketPathString+ delimiter +fileName);
        if(Files.exists(absoluteFilePath)){
            Files.delete(absoluteFilePath);
        }else {
            LOG.debug("File does not exist.");
        }
    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                return name[1].trim().replace("\"", "");
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {

        File file = new File(filename);

        if (!file.exists()) {
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                return;
            }
        }

        try (FileOutputStream fop = new FileOutputStream(file)) {
            fop.write(content);
            fop.flush();
        }
    }
}
