package de.rosariop.chunkyfile.file;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Consumes("*/*")
@Tag(name = "Bucket API")
@Path("/file")
public class FileResource {

    private static final Logger LOG = Logger.getLogger(FileResource.class);
    private static final String BYTES = "bytes";
    private static final long CHUNK_SIZE = 2L * 1024L * 1024L;

    @ConfigProperty(name = "config.delimiter")
    String delimiter;

    @ConfigProperty(name = "config.basePath")
    String basePath;

    @Inject
    FileService fileService;


    @POST
    @Path("/upload/{bucketName}")
    @Consumes("multipart/form-data")
    @Produces("*/*")
    public Response uploadFile(MultipartFormDataInput input, @PathParam("bucketName") String bucketName) {
        try {
            fileService.uploadFile(input, bucketName);
            return Response.ok().build();
        } catch (IOException e) {
            LOG.error(e);
            LOG.error("ERROR uploading file");
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{bucketName}/{fileName}")
    @Consumes("*/*")
    @Produces("*/*")
    public Response getChunkedFile2(@PathParam("bucketName") String bucketName, @PathParam("fileName") String fileName, @HeaderParam("Range") String range) {
        long rangeStart;
        long rangeEnd;

        String absoluteMediaPathString = basePath + delimiter + bucketName + delimiter + fileName;
        File mediaFile = Paths.get(absoluteMediaPathString).toFile();

        long fileSize = mediaFile.length();

        String[] ranges = range.split("-");
        rangeStart = Long.parseLong(ranges[0].substring(6));
        if (ranges.length > 1) {
            rangeEnd = Long.parseLong(ranges[1]);
        } else {
            rangeEnd = rangeStart + CHUNK_SIZE;
        }
        if (fileSize < rangeEnd) {
            rangeEnd = fileSize;
        }

        byte[] result = new byte[(int) (rangeEnd-rangeStart)];

        String contentLength = String.valueOf(result.length);

        //Todo: build method to determine mime-type

        return Response.status(Status.PARTIAL_CONTENT)
                .entity(result)
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header("Accept-Ranges", BYTES)
                .header(HttpHeaders.CONTENT_LENGTH, contentLength)
                .header("Content-Range", BYTES + " " + rangeStart + "-" + (rangeEnd-1) + "/" +fileSize)
                .build();
    }

    @DELETE
    @Path("/{bucketName}/{fileName}")
    @Consumes("*/*")
    @Produces("*/*")
    public Response deleteFile(@PathParam("bucketName") String bucketName, @PathParam("fileName") String fileName) {
        try {
            fileService.deleteFile(bucketName, fileName);
            return Response.ok().build();
        } catch (IOException e) {
            LOG.error(e);
            LOG.error("ERROR deleting file");
            return Response.serverError().build();
        }
    }
}
