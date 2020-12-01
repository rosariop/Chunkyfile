package de.rosariop.chunkyfile.bucket;

import de.rosariop.chunkyfile.exception.BucketExistsException;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

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
    public Response createBucket(@PathParam String bucketName){
        try{
            bucketService.createBucket(bucketName);
        } catch (BucketExistsException e) {
            e.printStackTrace();
            System.out.println("Bucket existed");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
            Response.serverError().build();
        }
        return Response.ok().build();
    }
}




