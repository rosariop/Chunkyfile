package de.rosariop.chunkyfile.file;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Consumes("*/*")
@Tag(name = "Bucket API")
@Path("/file")
public class FileResource {

    private static final Logger LOG = Logger.getLogger(FileResource.class);

    @Inject
    FileService fileService;

    @POST
    @Path("/upload/{bucketName}")
    @Consumes("multipart/form-data")
    @Produces("*/*")
    public Response uploadFile(MultipartFormDataInput input, @PathParam("bucketName") String bucketName){
        try{
            fileService.uploadFile(input, bucketName);
            return Response.ok().build();
        } catch (IOException e){
            LOG.error(e);
            LOG.error("ERROR uploading file");
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{bucketName}/{fileName}")
    @Consumes("*/*")
    @Produces("*/*")
    public Response deleteFile(@PathParam("bucketName") String bucketName, @PathParam("fileName") String fileName){
        try{
            fileService.deleteFile(bucketName, fileName);
            return Response.ok().build();
        } catch (IOException e) {
            LOG.error(e);
            LOG.error("ERROR deleting file");
            return Response.serverError().build();
        }
    }
}
