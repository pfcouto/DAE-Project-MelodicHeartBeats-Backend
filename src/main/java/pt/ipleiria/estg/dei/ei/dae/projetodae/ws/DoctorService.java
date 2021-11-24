package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.DoctorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("doctors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class DoctorService {

    @EJB
    DoctorBean doctorBean;

    @GET
    @Path("/")
    public List<DoctorDTO> getAllDoctors() {
        return toDTOs(doctorBean.getAllDoctors());
    }

    @GET
    @Path("{doctor}")
    public Response getDoctorDetails(@PathParam("doctor") String username) {
        Doctor doctor = doctorBean.findDoctor(username);
        if (doctor != null) {
            return Response.ok(toDTO(doctor)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }


    //    String username, String password, String name, Date birthDate, String email, int phoneNumber, String office
    @POST
    @Path("/")
    public Response createNewDoctor(DoctorDTO doctorDTO) {
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
                .entity(toDTO(newDoctor))
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
                .entity("ERROR_FINDING_ADMINISTRATOR")
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateDoctor(@PathParam("username") String username, DoctorDTO doctorDTO) {
        Doctor doctor = doctorBean.findDoctor(username);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_DOCTOR")
                    .build();
        }
//        String username, String password, String name, Date birthDate, String email, int phoneNumber, String office
        doctorBean.updateDoctor(
                username,
                doctorDTO.getPassword(),
                doctorDTO.getName(),
                doctorDTO.getBirthDate(),
                doctorDTO.getEmail(),
                doctorDTO.getPhoneNumber(),
                doctorDTO.getOffice()
        );

        Doctor newDoctor = doctorBean.findDoctor(username);
        if (newDoctor == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTO(newDoctor))
                .build();
    }

    DoctorDTO toDTO(Doctor doctor) {
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

    private List<DoctorDTO> toDTOs(List<Doctor> doctors) {
        return doctors.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
