package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.UserPasswordsDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
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
                .entity("ERROR_FINDING_ADMINISTRATOR")
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

    @PATCH
    @Path("{administrator}")
    public Response blockOrUnblockAdministrator(@PathParam("administrator") String username) {

        administratorBean.blockOrUnBlockAdministrator(username);

        Administrator administratorBlockedOrUnblocked = administratorBean.findAdministrator(username);

        return Response.ok().build();

    }

    @PATCH
    @Path("{administrator}/changePassword")
    public Response changePasswordAdministrator(@PathParam("administrator") String username, UserPasswordsDTO userPasswordsDTO) throws MyEntityNotFoundException {
        if (administratorBean.changePasswordAdministrator(username, userPasswordsDTO.getPasswordOld(), userPasswordsDTO.getPasswordNew())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_CHANGING_PASSWORD")
                .build();

    }

    @PUT
    @Path("{username}")
    public Response updateAdministrator(@PathParam("username") String username, AdministratorDTO administratorDTO) throws MyEntityNotFoundException {
        Administrator administrator = administratorBean.findAdministrator(username);
        if (administrator == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_ADMINISTRATOR")
                    .build();
        }

        administratorBean.updateAdministrator(
                username,
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
                null,
                administrator.getName(),
                administrator.getBirthDate(),
                administrator.getEmail(),
                administrator.getPhoneNumber(),
                administrator.isBlocked()
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
                administrator.getPhoneNumber(),
                administrator.isBlocked()
        );
        administratorDTO.setBiometricsTypeDTOS(biometricsTypeDTOS);
        return administratorDTO;
    }

    private List<AdministratorDTO> toDTOsWithBiometricsTypes(List<Administrator> administrators) {
        return administrators.stream().map(this::toDTOWithBiometricsTypes).collect(Collectors.toList());
    }

    BiometricsTypeDTO toDTO(BiometricsType biometricsType) {
        return new BiometricsTypeDTO(
                biometricsType.getCode(),
                biometricsType.getName(),
                biometricsType.getDescription(),
                biometricsType.getValueMax(),
                biometricsType.getValueMin(),
                biometricsType.getUnity(),
                biometricsType.getAdministrator().getUsername(),
                biometricsType.isDeleted()
        );
    }

    private List<BiometricsTypeDTO> biometricsTypesToDTOs(List<BiometricsType> biometricsTypes) {
        return biometricsTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
