package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PRCDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PRCBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.PRC;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
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

    PRCDTO toDTOWithPrescriptions(PRC prc) {
        PRCDTO prcDTO = new PRCDTO(
                prc.getId(),
                prc.getPatient().getUsername(),
                prc.getStartDate(),
                prc.getEndDate(),
                prc.isActive()
        );
        LinkedList<Prescription> prescriptions = prc.getPrescriptions();
        if (prescriptions != null && prescriptions.size() > 0) {
            prcDTO.setPrescriptions(prescriptionToDTOs(prescriptions));
        }
        return prcDTO;
    }

    PrescriptionDTO toDTO(Prescription prescription) {
        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getDoctor().getUsername(),
                prescription.getPatient().getUsername(),
                prescription.getDescription(),
                prescription.getStartDate(),
                prescription.getEndDate()
        );
    }

    private List<PrescriptionDTO> prescriptionToDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
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

    @GET
    @Path("{prc}")
    public Response getPRCDetails(@PathParam("prc") int prcId) {
        PRC prc = prcBean.findPRC(prcId);
        if (prc != null) {
            return Response.ok(toDTO(prc)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRC")
                .build();

    }

    @GET
    @Path("{prc}/withPrescriptions")
    public Response getPRCDetailsWithDetails(@PathParam("prc") int prcId) {
        PRC prc = prcBean.findPRC(prcId);
        if (prc != null) {
            return Response.ok(toDTOWithPrescriptions(prc)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRC")
                .build();
    }

    @PATCH
    @Path("{code}/endDate")
    public Response Update(@PathParam("code") int code, PRCDTO prc) throws MyEntityNotFoundException {
        boolean updated = prcBean.updateEndDate(code, prc.getEndDate());
        if (!updated) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
        return Response.status(Response.Status.OK)
                .build();
    }

    @PATCH
    @Path("{code}/active")
    public Response Update(@PathParam("code") int code, Boolean active) throws MyEntityNotFoundException {
        boolean updated = prcBean.updateActive(code, active);
        if (!updated) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
        return Response.status(Response.Status.OK)
                .build();
    }
}
