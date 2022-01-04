package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.UserPasswordsDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
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

@Path("doctors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class DoctorService {

    @EJB
    DoctorBean doctorBean;
    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public List<DoctorDTO> getAllDoctors() {
        return toDTOsNoPrescriptions(doctorBean.getAllDoctors());
    }

    @GET
    @Path("{doctor}")
    public Response getDoctorDetails(@PathParam("doctor") String username) {

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Administrator") ||
                securityContext.isUserInRole("Doctor") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Doctor doctor = doctorBean.findDoctor(username);
        if (doctor != null) {
            return Response.ok(toDTOWithPrescriptions(doctor)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_DOCTOR")
                .build();
    }

    @GET
    @Path("{doctor}/prescriptions")
    public Response getDoctorPrescriptions(@PathParam("doctor") String username) {
        Doctor doctor = doctorBean.findDoctor(username);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_DOCTOR")
                    .build();
        }
        return Response.ok(toDTOs(doctorBean.getPrescriptions(doctor))).build();

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
    public Response createNewDoctor(DoctorDTO doctorDTO) throws MyConstraintViolationException, MyEntityExistsException {
        doctorBean.create(
                doctorDTO.getUsername(),
                doctorDTO.getPassword(),
                doctorDTO.getName(),
                doctorDTO.getBirthDate(),
                doctorDTO.getEmail(),
                doctorDTO.getPhoneNumber(),
                doctorDTO.getOffice()
        );
        Doctor newDoctor = doctorBean.findDoctor(doctorDTO.getUsername());
        if (newDoctor == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTONoPrescriptions(newDoctor))
                .build();
    }

    @DELETE
    @Path("{doctor}")
    public Response deleteDoctor(@PathParam("doctor") String username) {

        doctorBean.deleteDoctor(username);

        if (doctorBean.findDoctor(username) == null) {
            return Response.ok().build();

        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_DOCTOR")
                .build();
    }

    @PATCH
    @Path("{doctor}")
    public Response blockOrUnblockDoctor(@PathParam("doctor") String username) {

        doctorBean.blockOrUnBlockDoctor(username);

        Doctor doctorDeletedOrUndeleted = doctorBean.findDoctor(username);

        return Response.ok().build();

    }

    @PUT
    @Path("{username}")
    public Response updateDoctor(@PathParam("username") String username, DoctorDTO doctorDTO) throws MyEntityNotFoundException {
        Doctor doctor = doctorBean.findDoctor(username);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_DOCTOR")
                    .build();
        }
        doctorBean.updateDoctor(
                username,
                doctorDTO.getName(),
                doctor.getBirthDate(),
                doctorDTO.getEmail(),
                doctorDTO.getPhoneNumber(),
                doctorDTO.getOffice()
        );

        Doctor newDoctor = doctorBean.findDoctor(username);
        if (newDoctor == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTONoPrescriptions(newDoctor))
                .build();
    }

    @PATCH
    @Path("{doctor}/changePassword")
    public Response changePasswordPatient(@PathParam("doctor") String username, UserPasswordsDTO userPasswordsDTO) throws MyEntityNotFoundException {

        if (doctorBean.changePasswordDoctor(username, userPasswordsDTO.getPasswordOld(), userPasswordsDTO.getPasswordNew())) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_CHANGING_PASSWORD")
                .build();
    }

    DoctorDTO toDTONoPrescriptions(Doctor doctor) {
        return new DoctorDTO(
                doctor.getUsername(),
                null,
                doctor.getName(),
                doctor.getBirthDate(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getOffice(),
                doctor.isBlocked()
        );
    }

    private List<DoctorDTO> toDTOsNoPrescriptions(List<Doctor> doctors) {
        return doctors.stream().map(this::toDTONoPrescriptions).collect(Collectors.toList());
    }

    DoctorDTO toDTOWithPrescriptions(Doctor doctor) {
        List<PrescriptionDTO> prescriptionsDTOS = prescriptionsToDTOs(doctor.getPrescriptions());
        DoctorDTO doctorDTO = new DoctorDTO(
                doctor.getUsername(),
                null,
                doctor.getName(),
                doctor.getBirthDate(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getOffice(),
                doctor.isBlocked()
        );
        doctorDTO.setPrescriptionDTOS(prescriptionsDTOS);
        return doctorDTO;
    }

    private List<DoctorDTO> toDTOsWithPrescriptions(List<Doctor> doctors) {
        return doctors.stream().map(this::toDTOWithPrescriptions).collect(Collectors.toList());
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
