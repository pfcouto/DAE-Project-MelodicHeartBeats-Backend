package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PRCDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PRCBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.PRC;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("prcs")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PRCService {

    @EJB
    PRCBean prcBean;

    PRCDTO toDTO(PRC prc) {
        return new PRCDTO(
                prc.getId(),
                prc.getPatient().getUsername(),
                prc.getStartDate(),
                prc.getEndDate(),
                prc.isActive()
        );
    }

    private List<PRCDTO> toDTOs(List<PRC> prcs) {
        return prcs.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<PRCDTO> getAllPRCS() {
        return toDTOs(prcBean.getAllprcs());
    }

    @POST
    @Path("/")
    public Response createNewPRC(PRCDTO prcDTO) {
        prcBean.create(prcDTO.getPatient(), prcDTO.getStartDate(), prcDTO.getEndDate());
        return Response.status(Response.Status.CREATED)
                .build();
    }
}
