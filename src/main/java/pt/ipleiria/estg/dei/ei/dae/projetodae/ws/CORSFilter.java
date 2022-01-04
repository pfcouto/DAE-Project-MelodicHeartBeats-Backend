package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept, Accept-Language");

        if (requestContext.getMethod().equals("OPTIONS"))
            responseContext.setStatus(200);
    }
}
