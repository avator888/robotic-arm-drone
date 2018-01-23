package foo.pac.endpoints;

import foo.pac.domains.ErrorPayload;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/")
public class FIleArcEndpoint {

    /**
     * read archive file
     *
     * @param p
     * @return
     */
    @GET
    @Path("/arcfile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response file(@QueryParam("path") String p) throws FileNotFoundException {

        // prepare some object for reply
        StringBuilder result = new StringBuilder();
        result.append("hello world");

        File file = new File(p);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                stream.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            return getInternalServerReply(ex.getMessage());
        }
        ResponseBuilder response = Response.ok((Object) stream.toByteArray());
        response.header("Content-Disposition", "attachment; filename=" + file.getName());
        return response.build();
    }

    // format return message reply in case of error
    private Response getInternalServerReply(String message) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorPayload(message)).build();
    }
}
