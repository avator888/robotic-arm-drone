package foo.pac.endpoints;

import foo.pac.domains.ErrorPayload;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class NetworkEndpoint {

    /**
     * good old ping http://localhost:8080/rad/ping/127.0.0.1
     *
     */
    @GET
    @Path("/ping/{ip}")
    @Produces({MediaType.TEXT_HTML})
    public Response ping(@PathParam("ip") String ip) {

        // prepare payload
        StringBuilder result = new StringBuilder();

        String pingCmd = "ping " + ip + " -c 10";
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append("<br/>");
                result.append(inputLine);
            }
            in.close();

        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorPayload(ex.getMessage())).build();
        }

        // plain text - console like
        Response response = Response.ok(result.toString(), MediaType.TEXT_HTML).build();
        return response;
    }

    /**
     * curl as well
     * http://localhost:8080/rad/curl?url=https://www.theglobeandmail.com
     */
    @GET
    @Path("/curl")
    @Produces({MediaType.TEXT_HTML})
    public Response status(@QueryParam("url") String url) {

        // prepare payload
        StringBuilder result = new StringBuilder();

        String pingCmd = "curl " + url;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append("<br/>");
                result.append(inputLine);
            }
            in.close();

        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorPayload(ex.getMessage())).build();
        }

        // plain text - console like
        Response response = Response.ok(result.toString(), MediaType.TEXT_HTML).build();
        return response;
    }

    /**
     * telnet as well; main purpose to check that port on remote server is open
     * http://localhost:8080/rad/curl?url=https://www.theglobeandmail.com
     */
    @GET
    @Path("/telnet/{ip}/{port}")
    @Produces({MediaType.TEXT_HTML})
    public Response telnet(@PathParam("ip") String ip, @PathParam("port") String port) {

        // prepare payload
        StringBuilder result = new StringBuilder();

        String cmd = "telnet " + ip + " " + port;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            int i = 0;
            while ((inputLine = in.readLine()) != null) {
                result.append("<br/>");
                result.append(inputLine);

                // just in case
                i++;
                if (i > 100) {
                    break;
                }
            }
            in.close();

        } catch (IOException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorPayload(ex.getMessage())).build();
        }

        // plain text - console like
        Response response = Response.ok(result.toString(), MediaType.TEXT_HTML).build();
        return response;
    }

}
