package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("patients")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PatientService {

    @EJB
    PatientBean patientBean;

    @GET
    @Path("/")
    public List<PatientDTO> getAllPatients() {
        return toDTOs(patientBean.getAllPatients());
    }

    @GET
    @Path("{patient}")
    public Response getPatientDetails(@PathParam("patient") String username) {
        Patient patient = patientBean.findPatient(username);
        if (patient != null) {
            return Response.ok(toDTO(patient)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @POST
    @Path("/")
    public Response createNewPatient(PatientDTO patientDTO) throws MyConstraintViolationException, MyEntityExistsException {
        patientBean.create(
                patientDTO.getUsername(),
                patientDTO.getPassword(),
                patientDTO.getName(),
                patientDTO.getBirthDate(),
                patientDTO.getEmail(),
                patientDTO.getPhoneNumber()
        );
        Patient newPatient = patientBean.findPatient(patientDTO.getUsername());
        if (newPatient == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTO(newPatient))
                .build();
    }

    @DELETE
    @Path("{patient}")
    public Response deletePatient(@PathParam("patient") String username) {

        patientBean.deletePatient(username);

        if (patientBean.findPatient(username) == null) {
            return Response.ok().build();

        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_ADMINISTRATOR")
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updatePatient(@PathParam("username") String username, PatientDTO patientDTO) {
        Patient patient = patientBean.findPatient(username);
        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_STUDENT")
                    .build();
        }

        patientBean.updatePatient(username,
                patientDTO.getPassword(),
                patientDTO.getName(),
                patientDTO.getBirthDate(),
                patientDTO.getEmail(),
                patientDTO.getPhoneNumber()
        );

        Patient newPatient = patientBean.findPatient(username);
        if (newPatient == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTO(newPatient))
                .build();
    }

    PatientDTO toDTO(Patient patient) {
        return new PatientDTO(
                patient.getUsername(),
                patient.getPassword(),
                patient.getName(),
                patient.getBirthDate(),
                patient.getEmail(),
                patient.getPhoneNumber()
        );
    }

    private List<PatientDTO> toDTOs(List<Patient> patients) {
        return patients.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
