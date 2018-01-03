package foo.pac.endpoints;

import foo.pac.domains.ErrorPayload;
import foo.pac.domains.ReplyPayload;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.boot.context.embedded.LocalServerPort;

@Path("/")
public class DirEndpoint {

    @LocalServerPort
    int port;

    private final String folderBase = "http://localhost:777/rad/dir/?path=";
    private final String fileBase = "http://localhost:777/rad/file/?path=";

    /**
     * get list of files in specific folder
     *
     * @param p
     * @return
     */
    // http://localhost:8088/rad/dir/?path=/home/avator/dev
    @GET
    @Path("/dir")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Response dir(@QueryParam("path") String p,
            @QueryParam("format") String format) throws UnknownHostException {

        // input validation
        if (p == null || p.isEmpty()) {
            String message = "it should be a path";
            Response response = Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorPayload(message)).build();
            return response;
        }

        // get list of files / folder
        File f;
        File[] paths;

        try {

            // create new file
            f = new File(p);

            // returns pathnames for files and directory
            paths = f.listFiles();

        } catch (Exception ex) {
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorPayload(ex.getMessage())).build();
            return response;
        }

        // create reply message
        if (format != null && !format.isEmpty() && format.equals("json")) {
            // json format output
            ArrayList<String> res = new ArrayList();

            for (File path : paths) {
                res.add(path.toString());
            }

            ReplyPayload rp = new ReplyPayload(res);
            Response response = Response.ok(rp, MediaType.APPLICATION_JSON).build();
            return response;
        } else {
            // plain text - console like
            StringBuilder result = new StringBuilder();
            result.append("get list of files for dir -> ");
            result.append("\n");
            result.append(p);

            // for each pathname in pathname array
            try {
                for (File path : paths) {
                    result.append("\n</br>");
                    if (path.isDirectory()) {
                        result.append("<a href=\"")
                                .append(folderBase.replace("localhost", this.getIp())
                                        .replace("777", getPort()))
                                .append(path).append("\">")
                                .append(path).append("</a>");
                    } else {
                        result.append("<a href=\"")
                                .append(fileBase.replace("localhost", this.getIp())
                                        .replace("777", getPort()))
                                .append(path).append("\">")
                                .append(path).append("</a>");
                    }

                }
            } catch (UnknownHostException ex) {
                Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("houston, we got problems").build();
                return response;

            }

            Response response = Response.ok(result.toString(), MediaType.TEXT_HTML).build();
            return response;
        }

    }

    /**
     *
     * get ip
     *
     * @return
     * @throws UnknownHostException
     */
    private String getIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     *
     * get port
     *
     * @return
     * @throws UnknownHostException
     */
    private String getPort() {
        // return  this.port;
        return "8088";
    }
}
