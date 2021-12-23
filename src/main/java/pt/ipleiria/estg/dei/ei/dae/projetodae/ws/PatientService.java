package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

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
    @EJB
    DoctorBean doctorBean;

    @GET
    @Path("/")
    public List<PatientDTO> getAllPatientsNotDeleted() {
        System.out.println(patientBean.getAllPatientsNotDeleted());
        return toDTOsNoPrescriptions(patientBean.getAllPatientsNotDeleted());
    }

    @GET
    @Path("/all")
    public List<PatientDTO> getAllPatients() {
        return toDTOsNoPrescriptions(patientBean.getAllPatients());
    }

    @GET
    @Path("{patient}")
    public Response getPatientDetails(@PathParam("patient") String username) {
        Patient patient = patientBean.findPatient(username);
        if (patient != null) {
            return Response.ok(toDTOWithPrescriptions(patient)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PATIENT")
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
                .entity(toDTONoPrescriptions(newPatient))
                .build();
    }

    @DELETE
    @Path("{patient}")
    public Response deletePatient(@PathParam("patient") String username) {

        patientBean.deletePatient(username);

        Patient patientDeleted = patientBean.findPatient(username);

        if (patientDeleted.isDeleted()) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PATIENT")
                .build();
    }

    @PATCH
    @Path("{patient}")
    public Response softDeleteOrUndeletePatient(@PathParam("patient") String username) {

        patientBean.softDeleteOrUndeletePatient(username);

        Patient patientDeletedOrUndeleted = patientBean.findPatient(username);

//        if (patientDeletedOrUndeleted.isDeleted()) {
        return Response.ok().build();
//        }

//        return Response.status(Response.Status.NOT_FOUND)
//                .entity("ERROR_FINDING_PATIENT")
//                .build();
    }

    @PUT
    @Path("{username}")
    public Response updatePatient(@PathParam("username") String username, PatientDTO patientDTO) throws MyEntityNotFoundException {
        Patient patient = patientBean.findPatient(username);
        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PATIENT")
                    .build();
        }

        patientBean.updatePatient(username,
                patientDTO.getName(),
                patientDTO.getBirthDate(),
                patientDTO.getEmail(),
                patientDTO.getPhoneNumber()
        );

        Patient newPatient = patientBean.findPatient(username);
        if (newPatient == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTONoPrescriptions(newPatient))
                .build();
    }

    PatientDTO toDTONoPrescriptions(Patient patient) {
        return new PatientDTO(
                patient.getUsername(),
                null,
                patient.getName(),
                patient.getBirthDate(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.isDeleted()
        );
    }

    private List<PatientDTO> toDTOsNoPrescriptions(List<Patient> patients) {
        return patients.stream().map(this::toDTONoPrescriptions).collect(Collectors.toList());
    }

    PatientDTO toDTOWithPrescriptions(Patient patient) {
        List<PrescriptionDTO> prescriptionsDTOS = prescriptionsToDTOs(patient.getPrescriptions());
        PatientDTO patientDTO = new PatientDTO(
                patient.getUsername(),
                null,
                patient.getName(),
                patient.getBirthDate(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.isDeleted()
        );
        patientDTO.setPrescriptionDTOS(prescriptionsDTOS);
        return patientDTO;
    }

    private List<PatientDTO> toDTOsWithPrescriptions(List<Patient> patients) {
        return patients.stream().map(this::toDTOWithPrescriptions).collect(Collectors.toList());
    }

    PrescriptionDTO toDTO(Prescription prescription) {
        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getDoctor().getName(),
                prescription.getPatient().getName(),
                prescription.getDescription(),
                prescription.getStartDate(),
                prescription.getEndDate()
        );
    }

    private List<PrescriptionDTO> prescriptionsToDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
