package de.rosariop.chunkyfile.file;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FileService {

    private static final Logger LOG = Logger.getLogger(FileService.class);

    @ConfigProperty(name = "config.delemitter")
    String delemitter;

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


            String bucketPath = basePath + delemitter + bucketName + delemitter;
            fileName = bucketPath + fileName;

            this.writeFile(bytes, fileName);
            LOG.debug("File Uploaded");
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
