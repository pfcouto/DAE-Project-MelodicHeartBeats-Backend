package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.PRC;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
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
    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public List<PatientDTO> getAllPatientsNotDeleted() {
        System.out.println(patientBean.getAllPatientsNotDeleted());
        return toDTOsNoPrescriptions(patientBean.getAllPatientsNotDeleted());
    }

    PRCDTO prcToDTO(PRC prc) {
        return new PRCDTO(
                prc.getId(),
                prc.getPatient().getUsername(),
                prc.getStartDate(),
                prc.getEndDate(),
                prc.isActive()
        );
    }

    private List<PRCDTO> prcToDTOs(List<PRC> prcs) {
        return prcs.stream().map(this::prcToDTO).collect(Collectors.toList());
    }

    private ObservationDTO toDTO(Observation observation) {
        return new ObservationDTO(
                observation.getId(),
                observation.getDate(),
                observation.getPatient().getUsername(),
                observation.getBiometricsType().getCode(),
                observation.getBiometricsType().getName(),
                observation.getQuantitativeValue(),
                observation.getQualitativeValue(),
                observation.getWhat(),
                observation.getLocal());
    }

    private List<ObservationDTO> observationtosDTOs(List<Observation> observations) {
        return observations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{patient}/observations")
    public Response getObservationsOfPatient(@PathParam("patient") String username) {
        Patient patient = patientBean.findPatient(username);
        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PATIENT")
                    .build();
        }
        return Response.ok(observationtosDTOs(patientBean.getAllObservations(patient))).build();
    }

    @GET
    @Path("{patient}/prcs")
    public Response getPatientPRCs(@PathParam("patient") String username) {
        Patient patient = patientBean.findPatient(username);
        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PATIENT")
                    .build();
        }
        if (patient.getPrcs().size() < 1) {
            return Response.noContent().build();
        }
        return Response.ok(prcToDTOs(patient.getPrcs())).build();
    }

    @GET
    @Path("{patient}/prc")
    public Response getPatientActivePRC(@PathParam("patient") String username) {
        Patient patient = patientBean.findPatient(username);
        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PATIENT")
                    .build();
        }
        if (patient.getActivePRC() == null) {
            return Response.noContent().build();
        }
        return Response.ok(prcToDTO(patient.getActivePRC())).build();
    }

    @GET
    @Path("/all")
    public List<PatientDTO> getAllPatients() {
        return toDTOsNoPrescriptions(patientBean.getAllPatients());
    }

    @GET
    @Path("{patient}")
    public Response getPatientDetails(@PathParam("patient") String username) {

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Administrator") ||
                securityContext.isUserInRole("Doctor") ||
                securityContext.isUserInRole("Patient") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Patient patient = patientBean.findPatient(username);
        if (patient != null) {
            return Response.ok(toDTOWithPrescriptions(patient)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PATIENT")
                .build();
    }

    @GET
    @Path("{patient}/prescriptions")
    public Response getDoctorPrescriptions(@PathParam("patient") String username) {
        Patient patient = patientBean.findPatient(username);
        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PATIENT")
                    .build();
        }
        return Response.ok(toDTOs(patientBean.getPrescriptions(patient))).build();

    }

    private List<PrescriptionDTO> toDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::toPrescriptionDTO).collect(Collectors.toList());
    }

    PrescriptionDTO toPrescriptionDTO(Prescription prescription) {
        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getDoctor().getUsername(),
                prescription.getPatient().getUsername(),
                prescription.getDescription(),
                prescription.getStartDate(),
                prescription.getEndDate()
        );
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

        if (patientDeleted.isBlocked()) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PATIENT")
                .build();
    }

    @PATCH
    @Path("{patient}")
    public Response blockOrUnblockPatient(@PathParam("patient") String username) {

        patientBean.blockOrUnBlockPatient(username);

        Patient patientDeletedOrUndeleted = patientBean.findPatient(username);

//        if (patientDeletedOrUndeleted.isDeleted()) {
        return Response.ok().build();
//        }

//        return Response.status(Response.Status.NOT_FOUND)
//                .entity("ERROR_FINDING_PATIENT")
//                .build();
    }

    @PATCH
    @Path("{patient}/changePassword")
    public Response changePasswordPatient(@PathParam("patient") String username, UserPasswordsDTO userPasswordsDTO) throws MyEntityNotFoundException {

        if (patientBean.changePasswordPatient(username, userPasswordsDTO.getPasswordOld(), userPasswordsDTO.getPasswordNew())) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_CHANGING_PASSWORD")
                .build();
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
                patient.isBlocked()
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
                patient.isBlocked()
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
