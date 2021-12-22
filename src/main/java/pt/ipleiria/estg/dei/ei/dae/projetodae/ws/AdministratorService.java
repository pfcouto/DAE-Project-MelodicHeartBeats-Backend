package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.DoctorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Doctor;
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

@Path("administrators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdministratorService {
    @EJB
    AdministratorBean administratorBean;

    @GET
    @Path("/")
    public List<AdministratorDTO> getAllAdministrators() {
        return toDTOsNoBiometricsType(administratorBean.getAllAdministrators());
    }

    @GET
    @Path("{administrator}")
    public Response getAdministratorDetails(@PathParam("administrator") String username) {
        Administrator administrator = administratorBean.findAdministrator(username);
        if (administrator != null) {
            return Response.ok(toDTOWithBiometricsTypes(administrator)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @POST
    @Path("/")
    public Response createNewAdministrator(AdministratorDTO administratorDTO) throws MyConstraintViolationException, MyEntityExistsException {
        administratorBean.create(administratorDTO.getUsername(),
                administratorDTO.getPassword(),
                administratorDTO.getName(),
                administratorDTO.getBirthDate(),
                administratorDTO.getEmail(),
                administratorDTO.getPhoneNumber()
        );
        Administrator newAdministrator = administratorBean.findAdministrator(administratorDTO.getUsername());
        if (newAdministrator == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTONoBiometricsType(newAdministrator))
                .build();
    }

    @DELETE
    @Path("{administrator}")
    public Response deleteAdministrator(@PathParam("administrator") String username) {

        administratorBean.deleteAdministrator(username);

        if (administratorBean.findAdministrator(username) == null) {
            return Response.ok().build();

        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_ADMINISTRATOR")
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateAdministrator(@PathParam("username") String username, AdministratorDTO administratorDTO) throws MyEntityNotFoundException {
        Administrator administrator = administratorBean.findAdministrator(username);
        if (administrator == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_STUDENT")
                    .build();
        }

        administratorBean.updateAdministrator(
                username,
                administratorDTO.getPassword(),
                administratorDTO.getName(),
                administratorDTO.getBirthDate(),
                administratorDTO.getEmail(),
                administratorDTO.getPhoneNumber()
        );

        Administrator newAdministrator = administratorBean.findAdministrator(username);
        if (newAdministrator == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.CREATED)
                .entity(toDTONoBiometricsType(newAdministrator))
                .build();
    }


    AdministratorDTO toDTONoBiometricsType(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getUsername(),
                administrator.getPassword(),
                administrator.getName(),
                administrator.getBirthDate(),
                administrator.getEmail(),
                administrator.getPhoneNumber()
        );
    }

    private List<AdministratorDTO> toDTOsNoBiometricsType(List<Administrator> administrators) {
        return administrators.stream().map(this::toDTONoBiometricsType).collect(Collectors.toList());
    }

    AdministratorDTO toDTOWithBiometricsTypes(Administrator administrator) {
        List<BiometricsTypeDTO> biometricsTypeDTOS = biometricsTypesToDTOs(administrator.getBiometricsTypes());
        AdministratorDTO administratorDTO = new AdministratorDTO(
                administrator.getUsername(),
                null,
                administrator.getName(),
                administrator.getBirthDate(),
                administrator.getEmail(),
                administrator.getPhoneNumber()
        );
        administratorDTO.setBiometricsTypeDTOS(biometricsTypeDTOS);
        return administratorDTO;
    }

    private List<AdministratorDTO> toDTOsWithBiometricsTypes(List<Administrator> administrators) {
        return administrators.stream().map(this::toDTOWithBiometricsTypes).collect(Collectors.toList());
    }

    BiometricsTypeDTO toDTO(BiometricsType biometricsType) {
        return new BiometricsTypeDTO(
                // TODO
        );
    }

    private List<BiometricsTypeDTO> biometricsTypesToDTOs(List<BiometricsType> biometricsTypes) {
        return biometricsTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
