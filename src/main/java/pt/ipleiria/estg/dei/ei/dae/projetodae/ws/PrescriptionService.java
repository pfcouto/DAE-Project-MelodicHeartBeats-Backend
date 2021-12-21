package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PresciptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("prescriptions")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PrescriptionService {
    @EJB
    PrescriptionBean prescriptionBean;

    PresciptionDTO toDTO(Prescription prescription) {
        return new PresciptionDTO(
                prescription.getId(),
                prescription.getDoctor().getUsername(),
                prescription.getPatient().getUsername(),
                prescription.getDescription(),
                prescription.getStartDate(),
                prescription.getEndDate()
        );
    }
//    DoctorDTO toDTO(Doctor doctor){
//        return new DoctorDTO(
//                doctor.getUsername(),
//                doctor.getPassword(),
//                doctor.getName(),
//                doctor.getBirthDate(),
//                doctor.getEmail(),
//                doctor.getPhoneNumber(),
//                doctor.getOffice()
//        );
//    }
//
//    PatientDTO toDTO(Patient patient){
//        return new PatientDTO(
//                patient.getUsername(),
//                patient.getPassword(),
//                patient.getName(),
//                patient.getBirthDate(),
//                patient.getEmail(),
//                patient.getPhoneNumber()
//        );
//    }

    private List<PresciptionDTO> toDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<PresciptionDTO> getAllPrescriptions() {
        return toDTOs(prescriptionBean.getAllPrescriptions());
    }

    @GET
    @Path("{prescription}")
    public Response getDoctorDetails(@PathParam("prescription") int prescriptionId) {
        Prescription prescription = prescriptionBean.findPrescription(prescriptionId);
        if (prescription != null) {
            return Response.ok(toDTO(prescription)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PRESCRIPTION")
                .build();
    }

    @POST
    @Path("/")
    public Response createNewPrescription(PresciptionDTO presciptionDTO) {
        prescriptionBean.create(
                presciptionDTO.getDoctor(),
                presciptionDTO.getPatient(),
                presciptionDTO.getDescription(),
                presciptionDTO.getStartDate(),
                presciptionDTO.getEndDate()
        );
        return Response.status(Response.Status.CREATED)
                .build();
    }
}
