package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.QualitativeValuesDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.BiometricsTypeBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("biometricsType") // url
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class BiometricsTypeService {
    @EJB
    BiometricsTypeBean biometricsTypeBean;
    AdministratorBean administratorBean;
    private BiometricsTypeDTO toDTO(BiometricsType biometricsType) {
        //AdministratorDTO administratorDTO = new AdministratorDTO(biometricsType.getAdministrator().getUsername(), biometricsType.getAdministrator().getName(), biometricsType.getAdministrator().getEmail(), biometricsType.getAdministrator().getPhoneNumber());
        List<QualitativeValuesDTO> qualitativeValuesDTOSList=new LinkedList<>();
        for (Map.Entry<Integer, String> entry : biometricsType.getListOfQualitativeValues().entrySet()) {
            QualitativeValuesDTO qualitativeValuesDTO=new QualitativeValuesDTO(entry.getKey(),entry.getValue());
            qualitativeValuesDTOSList.add(qualitativeValuesDTO);
        }

        return new BiometricsTypeDTO(biometricsType.getCode(), biometricsType.getName(),biometricsType.getDescription(), biometricsType.getValueMax(), biometricsType.getValueMin(), biometricsType.getUnity(), biometricsType.getAdministrator().getUsername(),biometricsType.isDeleted(),qualitativeValuesDTOSList);
    }

    private List<BiometricsTypeDTO> toDTOs(List<BiometricsType> biometricsType) {
        return biometricsType.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<BiometricsTypeDTO> getAllBiometricsTypesWS() {
        return toDTOs(biometricsTypeBean.getAllBiometricsTypes());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/nonDeleted") // means: the relative url path is “/api/students/”
    public List<BiometricsTypeDTO> getAllBiometricsTypesNonDeletedWS() {
        return toDTOs(biometricsTypeBean.getAllBiometricsTypesNonDeleted());
    }

    @GET
    @Path("{code}")
    public Response getBiometricsTypeDetails(@PathParam("code") int code) throws MyEntityNotFoundException {
        BiometricsType biometricsType = biometricsTypeBean.find(code);
        if (biometricsType != null) {
            return Response.ok(toDTO(biometricsType)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_BIOMETRIC_TYPE")
                .build();
    }

    @POST // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response createBiometricsTypesWS(BiometricsTypeDTO biometricsTypeDTO) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
        System.out.println(biometricsTypeDTO);
        BiometricsType biometricsType=biometricsTypeBean.create(
                biometricsTypeDTO.getName(),
                biometricsTypeDTO.getDescription(),
                biometricsTypeDTO.getValueMax(),
                biometricsTypeDTO.getValueMin(),
                biometricsTypeDTO.getUnity(),
                biometricsTypeDTO.getAdministrator(),
                biometricsTypeDTO.getQualitatives()
        );
        if (biometricsType == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();}
        return Response.status(Response.Status.CREATED)
                .entity(toDTO(biometricsType))
                .build();
    }

    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") int code) throws MyEntityNotFoundException{
        BiometricsType biometricsType= biometricsTypeBean.find(code);
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
    public Response Update(@PathParam("code") int code,BiometricsTypeDTO biometricsTypeDTO) throws MyEntityNotFoundException,MyIllegalArgumentException {
        boolean updated=biometricsTypeBean.update(code,biometricsTypeDTO);
        //FUNCIONAAA
        if(!updated) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
        return Response.status(Response.Status.OK)
                .build();
    }

}
