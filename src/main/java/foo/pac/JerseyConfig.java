package foo.pac;

import foo.pac.endpoints.DirEndpoint;
import foo.pac.endpoints.FIleTextEndpoint;
import foo.pac.endpoints.FIleXmlEndpoint;
import foo.pac.endpoints.NetworkEndpoint;
import foo.pac.endpoints.PingEndpoint;
import foo.pac.endpoints.ServiceEndpoint;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/rad")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        registerEndpoints();
        register(LoggingFilter.class);
    }

    private void registerEndpoints() {
        register(PingEndpoint.class);
        register(DirEndpoint.class);
        register(FIleTextEndpoint.class);
        register(FIleXmlEndpoint.class);
        register(ServiceEndpoint.class);
        register(NetworkEndpoint.class);
    }

}
