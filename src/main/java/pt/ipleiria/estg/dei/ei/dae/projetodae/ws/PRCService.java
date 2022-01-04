package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PRCDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PRCBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.PRC;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Path("prcs")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PRCService {

    @EJB
    PRCBean prcBean;
    @Context
    private SecurityContext securityContext;

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
    public List<PRCDTO> getAllPRCS() throws MyEntityNotFoundException {

        List<PRC> listAux = prcBean.getAllprcs();

        updateActivePRCs(listAux);

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
    public Response getPRCDetails(@PathParam("prc") int prcId) throws MyEntityNotFoundException {
        PRC prc = prcBean.findPRC(prcId);

        if (prc == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PRC")
                    .build();
        }

        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Patient") && !principal.getName().equals(prc.getPatient().getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<PRC> prcs = new LinkedList<>();
        prcs.add(prc);
        updateActivePRCs(prcs);

        return Response.ok(toDTO(prc)).build();
    }

    @GET
    @Path("{prc}/withPrescriptions")

    public Response getPRCDetailsWithPrescriptions(@PathParam("prc") int prcId) throws MyEntityNotFoundException {
        PRC prc = prcBean.findPRC(prcId);
        if (prc == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PRC")
                    .build();
        }

        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Patient") && !principal.getName().equals(prc.getPatient().getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<PRC> prcs = new LinkedList<>();
        prcs.add(prc);
        updateActivePRCs(prcs);

        return Response.ok(toDTOWithPrescriptions(prc)).build();
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

    private void updateActivePRCs(List<PRC> prcs) throws MyEntityNotFoundException {
        if (prcs == null) {
            return;
        }
        for (PRC prc : prcs) {
            if (prc.isActive()) {
                String[] split = prc.getEndDate().split("-");
                Calendar prcDate = Calendar.getInstance();
                prcDate.set(Integer.parseInt(split[0]), Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]));
                if (Calendar.getInstance().after(prcDate)) {

                    prcBean.updateActive(prc.getId(), false);
                }
            }

        }
    }
}
