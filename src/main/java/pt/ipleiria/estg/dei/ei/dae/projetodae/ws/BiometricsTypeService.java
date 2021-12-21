package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import org.jboss.resteasy.annotations.Body;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.BiometricsTypeBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("biometricsType") // url
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class BiometricsTypeService {
    @EJB
    BiometricsTypeBean biometricsTypeBean;
    AdministratorBean administratorBean;
    private BiometricsTypeDTO toDTO(BiometricsType biometricsType) {
        AdministratorDTO administratorDTO = new AdministratorDTO(biometricsType.getAdministrator().getUsername(), biometricsType.getAdministrator().getName(), biometricsType.getAdministrator().getEmail(), biometricsType.getAdministrator().getPhoneNumber());
        return new BiometricsTypeDTO(biometricsType.getCode(), biometricsType.getType(), biometricsType.getMax(), biometricsType.getMin(), biometricsType.getUnity(), administratorDTO.getUsername());
    }

    private List<BiometricsTypeDTO> toDTOs(List<BiometricsType> biometricsType) {
        return biometricsType.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<BiometricsTypeDTO> getAllBiometricsTypesWS() {
        return toDTOs(biometricsTypeBean.getAllBiometricsTypes());
    }

    @POST // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response createBiometricsTypesWS(BiometricsTypeDTO biometricsTypeDTO) {
        BiometricsType biometricsType=biometricsTypeBean.create(
                biometricsTypeDTO.getType(),
                biometricsTypeDTO.getMax(),
                biometricsTypeDTO.getMin(),
                biometricsTypeDTO.getUnity(),
                biometricsTypeDTO.getAdministrator()
        );
        if (biometricsType == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();}
        return Response.status(Response.Status.CREATED)
                .entity(toDTO(biometricsType))
                .build();
    }

    @DELETE
    @Path("delete/{code}")
    public Response delete(@PathParam("code") int code){
        BiometricsType biometricsType= biometricsTypeBean.find(code);
        System.out.println(biometricsType);
        if(biometricsType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        biometricsTypeBean.delete(biometricsType);
        return Response.status(Response.Status.OK)
                .entity(toDTO(biometricsType))
                .build();
    }

    @PATCH
    @Path("update/{code}")
    public Response Update(@PathParam("code") int code,BiometricsTypeDTO biometricsTypeDTO){
        System.out.println(biometricsTypeDTO.getType());
        System.out.println(biometricsTypeDTO.getMax());
        //FUNCIONAAA
        return Response.status(Response.Status.CREATED)
                .build();
    }

}
