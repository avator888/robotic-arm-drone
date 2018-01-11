package foo.pac.endpoints;

import foo.pac.domains.ErrorPayload;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class DirEndpoint {

    private final String folderBase = "https://localhost:{port}/rad/dir/?path=";
    private final String fileTxtBase = "https://localhost:{port}/rad/txtfile/?path=";
    private final String fileXmlBase = "https://localhost:{port}/rad/xmlfile/?path=";

    /**
     * get list of files in specific folder
     *
     */
    // http://localhost:8080/rad/dir/?path=/home
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
                            .append(folderBase.replace("localhost", this.getIp()).replace("{port}", getPort()))
                            .append(path).append("\">")
                            .append(path).append("</a>");
                } else {
                    String extention = this.getFileExtension(path);

                    if (extention.equals("log")
                            || extention.equals("txt")
                            || extention.equals("bash")) {
                        // text file
                        result.append("<a href=\"")
                                .append(fileTxtBase.replace("localhost", this.getIp()).replace("{port}", getPort()))
                                .append(path).append("\">")
                                .append(path).append("</a>");
                    } else if (extention.equals("xml")) {
                        // xml file
                        result.append("<a href=\"")
                                .append(fileXmlBase.replace("localhost", this.getIp()).replace("{port}", getPort()))
                                .append(path).append("\">")
                                .append(path).append("</a>");
                    } else if (extention.equals("gz")) {
                        // arc file
//                        result.append("<a href=\"")
//                                //.append(fileXmlBase.replace("localhost", this.getIp()).replace("{port}", getPort()))
//                                .append(path)
//                                .append("\" ")
//                                .append("download=\"w3logo\"")
//                                .append("\">")
//                                .append(path).append("</a>");
                        result.append(path);
                    } else {
                        result.append(path);
                    }

                }

            }
        } catch (UnknownHostException ex) {
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("houston, we got problems: " + ex.getMessage()).build();
            return response;
        }

        Response response = Response.ok(result.toString(), MediaType.TEXT_HTML).build();
        return response;

    }

    /**
     *
     * get ip or translate internal ip to external
     *
     */
    private String getIp() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        if (ip.equals("localhost")) {
            return "127.0.0.1";
        }
        return ip;
    }

    private String getPort() {
        return "8080";
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
