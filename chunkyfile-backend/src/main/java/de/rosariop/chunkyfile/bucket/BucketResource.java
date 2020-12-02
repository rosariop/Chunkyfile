package de.rosariop.chunkyfile.bucket;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Consumes("*/*")
@Tag(name = "Bucket API")
@Path("/bucket")
public class BucketResource {

    @Inject
    BucketService bucketService;

    @POST
    @Produces("*/*")
    @Path("/{bucketName}")
    public Response createBucket(@PathParam("bucketName") String bucketName) {
        try {
            bucketService.createBucket(bucketName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception creating");
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
            System.out.println("IO Exception deleting");
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
            System.out.println("IO Exception deleting");
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

}




