package foo.pac.endpoints;

import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * checking that application is up and running
 *
 */
@Path("/")
public class PingEndpoint {

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_HTML)
    public Response pingMe() {
        String rp = "if you see this message then RAD is up ... " + new Date();
        Response response = Response.ok(rp, MediaType.TEXT_HTML).build();
        return response;
    }

}
