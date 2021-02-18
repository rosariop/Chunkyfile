package de.rosariop.chunkyfile.bucket;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.ws.rs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Consumes("*/*")
@Tag(name = "Bucket API")
@Path("/bucket")
public class BucketResource {

    private static final Logger LOG = Logger.getLogger(BucketResource.class);

    @Inject
    BucketService bucketService;

    @GET
    @Produces("application/json")
    @Path("/{bucketName}")
    public Response getFileListFromBucket(@PathParam("bucketName") String bucketName){
        try {
            List<String> fileNamesInBucket =bucketService.getFileNamesFromBucket(bucketName);
            return Response.ok(fileNamesInBucket).build();
        }catch (IOException e){
            e.printStackTrace();
            LOG.debug("IO Exception creating");
            return Response.serverError().build();
        }
    }

    @POST
    @Produces("*/*")
    @Path("/{bucketName}")
    public Response createBucket(@PathParam("bucketName") String bucketName) {
        try {
            bucketService.createBucket(bucketName);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.debug("IO Exception creating");
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Produces("*/*")
    @Path("/{bucketName}")
    public Response deleteBucket(@PathParam("bucketName") String bucketName) {
        try {
            bucketService.deleteBucket(bucketName);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.debug("IO Exception deleting");
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    @PATCH
    @Produces("*/*")
    @Path("/{oldBucketName}/{newBucketName}")
    public Response renameBucket(@PathParam("oldBucketName") String oldBucketName, @PathParam("newBucketName") String newBucketName) {
        try{
            bucketService.renameBucket(oldBucketName, newBucketName);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.debug("IO Exception deleting");
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

}




