package foo.pac.endpoints;

import foo.pac.domains.ErrorPayload;
import java.io.File;
import java.net.UnknownHostException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class DirEndpoint {

    private final String folderBase = "/rad/dir/?path=";
    private final String fileTxtBase = "/rad/txtfile/?path=";
    private final String fileXmlBase = "/rad/xmlfile/?path=";

    /**
     * get list of files in specific folder
     *
     */
    @GET
    @Path("/dir")
    @Produces({MediaType.TEXT_HTML})
    public Response dir(@QueryParam("path") String p) throws UnknownHostException {

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
                            .append(folderBase.toLowerCase())
                            .append(path).append("\">")
                            .append(path).append("</a>");
                } else {
                    String extention = this.getFileExtension(path);

                    if (extention.equals("log")
                            || extention.equals("txt")
                            || extention.equals("bash")) {
                        // text file
                        result.append("<a href=\"")
                                .append(fileTxtBase.toLowerCase())
                                .append(path).append("\">")
                                .append(path).append("</a>");
                    } else if (extention.equals("xml")) {
                        // xml file
                        result.append("<a href=\"")
                                .append(fileXmlBase.toLowerCase())
                                .append(path).append("\">")
                                .append(path).append("</a>");
                    } else if (extention.equals("gz")) {
                        // arc file
                        result.append(path);
                    } else {
                        result.append(path);
                    }

                }

            }
        } catch (Exception ex) {
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("houston, we got problems: " + ex.getMessage()).build();
            return response;
        }

        Response response = Response.ok(result.toString(), MediaType.TEXT_HTML).build();
        return response;

    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }
}
