package foo.pac.endpoints;

import foo.pac.domains.ErrorPayload;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response.Status;

/**
 *
 * checking that some service is up and running
 *
 */
@Path("/")
public class ServiceEndpoint {

    @GET
    @Path("/service/{name}/status")
    @Produces({MediaType.TEXT_HTML})
    public Response status(@PathParam("name") String name) {
        return exec(name, "status");
    }

    /**
     * does not work without proper permissions
     */
    @GET
    @Path("/service/{name}/start")
    @Produces({MediaType.TEXT_HTML})
    public Response start(@PathParam("name") String name) {
        // return exec(name, "start");
        Response response = Response.status(Status.BAD_REQUEST).entity(new ErrorPayload("commented out; think twice before actual usage")).build();
        return response;
    }

    /**
     * does not work without proper permissions
     */
    @GET
    @Path("/service/{name}/stop")
    @Produces({MediaType.TEXT_HTML})
    public Response stop(@PathParam("name") String name) {
//        return exec(name, "stop");
        Response response = Response.status(Status.BAD_REQUEST).entity(new ErrorPayload("commented out; think twice before actual usage")).build();
        return response;
    }

    private Response exec(String serviceName, String command) {
        // validate name of service to access
        if (serviceName == null
                || serviceName.isEmpty()
                || serviceName.equals("null")) {
            Response response = Response.status(Status.BAD_REQUEST).entity(new ErrorPayload("problem with service name")).build();
            return response;
        }

        // prepare payload
        String result;
        try {
            Process p = Runtime.getRuntime().exec("service " + serviceName + " " + command);
            InputStream is = p.getInputStream();
            result = new BufferedReader(new InputStreamReader(is, "UTF-8")).lines().collect(Collectors.joining("<br/>"));
            result = result.replaceAll("[^\\x00-\\x7F]", " ");
        } catch (IOException ex) {
            Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ErrorPayload(ex.getMessage())).build();
            return response;

        }

        // send responce back
        Response response = Response.ok(result, MediaType.TEXT_HTML).build();
        return response;

    }

}
