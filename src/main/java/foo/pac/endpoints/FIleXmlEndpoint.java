package foo.pac.endpoints;

import foo.pac.domains.ErrorPayload;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class FIleXmlEndpoint {

    /**
     * read text file
     *
     * @param p
     * @return
     */
    @GET
    @Path("/xmlfile")
    @Produces(MediaType.TEXT_PLAIN)
    public Response file(@QueryParam("path") String p) {

        // prepare some object for reply
        StringBuilder result = new StringBuilder();
        result.append("<textarea rows=\"50\" cols=\"150\" style=\"border:none;\">");

        // do some work
        File f;
        try {

            // create new file
            f = new File(p);

            BufferedReader br = null;
            FileReader fr = null;

            try {

                fr = new FileReader(p);
                br = new BufferedReader(fr);

                String sCurrentLine;

                br = new BufferedReader(new FileReader(p));

                while ((sCurrentLine = br.readLine()) != null) {
                    result.append("\n");
                    result.append(sCurrentLine);
                }

            } catch (IOException ex) {
                return getInternalServerReply(ex.getMessage());
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (fr != null) {
                        fr.close();
                    }
                } catch (IOException ex) {
                    return getInternalServerReply(ex.getMessage());
                }

            }
        } catch (Exception ex) {
            return getInternalServerReply(ex.getMessage());
        }

        // show result
        result.append("</textarea>");
        Response response = Response.ok(result.toString(), MediaType.TEXT_HTML).build();
        return response;
    }

    // format return message reply in case of error
    private Response getInternalServerReply(String message) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorPayload(message)).build();
    }
}
