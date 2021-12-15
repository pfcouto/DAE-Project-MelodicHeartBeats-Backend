package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PresciptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
                prescription.getDescription()
        );
    }
    DoctorDTO toDTO(Doctor doctor){
        return new DoctorDTO(
                doctor.getUsername(),
                doctor.getPassword(),
                doctor.getName(),
                doctor.getBirthDate(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getOffice()
        );
    }

    PatientDTO toDTO(Patient patient){
        return new PatientDTO(
                patient.getUsername(),
                patient.getPassword(),
                patient.getName(),
                patient.getBirthDate(),
                patient.getEmail(),
                patient.getPhoneNumber()
        );
    }

    private List<PresciptionDTO> toDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response createNewPrescription(PresciptionDTO presciptionDTO) {
        prescriptionBean.create(
                presciptionDTO.getDoctor(),
                presciptionDTO.getPatient(),
                presciptionDTO.getDescription()
        );
        return Response.status(Response.Status.CREATED)
                .build();
    }
}
