package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("prescriptions" )
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PrescriptionService {
    @EJB
    PrescriptionBean prescriptionBean;
    @EJB
    EmailBean emailBean;
    @EJB
    PatientBean patientBean;

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

    private List<PrescriptionDTO> toDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/" )

    public List<PrescriptionDTO> getAllPrescriptions() {
        return toDTOs(prescriptionBean.getAllPrescriptions());
    }

    @GET
    @Path("{prescription}")
    public Response getPrescriptionDetails(@PathParam("prescription") int prescriptionId) {
        Prescription prescription = prescriptionBean.findPrescription(prescriptionId);
        if (prescription != null) {
            return Response.ok(toDTO(prescription)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRESCRIPTION" )
                .build();

    }

    @POST
    @Path("/" )
    public Response createNewPrescription(PrescriptionDTO prescriptionDTO) throws MyEntityNotFoundException, InvalidAttributesException {
        int prescriptionId = prescriptionBean.create(
                prescriptionDTO.getDoctor(),
                prescriptionDTO.getPatient(),
                prescriptionDTO.getDescription(),
                prescriptionDTO.getStartDate(),
                prescriptionDTO.getEndDate()
        );
        prescriptionBean.assignPrescriptionToPatientAndDoctor(
                prescriptionDTO.getDoctor(),
                prescriptionDTO.getPatient(),
                prescriptionId);

        Patient patient = patientBean.findPatient(prescriptionDTO.getPatient());
        emailBean.sendPrescription(patient.getEmail(), prescriptionId);

        return Response.status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{prescription}" )
    public Response createNewPrescription(@PathParam("prescription" ) int prescriptionId, PrescriptionDTO presciptionDTO) throws MyEntityNotFoundException {
        Prescription prescription = prescriptionBean.findPrescription(prescriptionId);
        if (prescription == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PRESCRIPTION" )
                    .build();
        }
        prescriptionBean.updatePrescription(
                prescriptionId,
//                presciptionDTO.getDoctor(),
//                presciptionDTO.getPatient(),
                presciptionDTO.getDescription(),
                presciptionDTO.getStartDate(),
                presciptionDTO.getEndDate()
        );

        Prescription newPrescription = prescriptionBean.findPrescription(prescriptionId);
        if (newPrescription == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK)
                .entity(toDTO(newPrescription))
                .build();
    }

    @DELETE
    @Path("{prescription}" )
    public Response deleteDoctor(@PathParam("prescription" ) int prescriptionId) throws MyEntityNotFoundException {

        prescriptionBean.deletePrescription(prescriptionId);

        if (prescriptionBean.findPrescription(prescriptionId) == null) {
            return Response.ok().build();

        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRESCRIPTION" )
                .build();
    }
}
