package de.rosariop.chunkyfile.file;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Consumes("*/*")
@Tag(name = "Bucket API")
@Path("/file")
public class FileResource {

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
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
