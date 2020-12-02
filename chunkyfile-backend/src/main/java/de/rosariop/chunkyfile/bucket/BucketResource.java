package de.rosariop.chunkyfile.bucket;

import javax.ws.rs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Consumes("*/*")
@Path("/bucket")
public class BucketResource {

    @Inject
    BucketService bucketService;

    @POST
    @Produces("*/*")
    @Path("/create/{bucketName}")
    public Response createBucket(@PathParam("bucketName") String bucketName){
        try{
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
    @Path("/delete/{bucketName}")
    public Response deleteBucket(@PathParam("bucketName") String bucketName){
        try {
            bucketService.deleteBucket(bucketName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception deleting");
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}




