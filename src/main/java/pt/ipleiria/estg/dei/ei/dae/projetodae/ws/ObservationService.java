package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.BiometricsTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.ObservationDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.BiometricsType;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyIllegalArgumentException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("observations") // url
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ObservationService {
    @EJB
    ObservationBean observationBean;

    private ObservationDTO toDTO(Observation observation) {
        return new ObservationDTO(observation.getId(),observation.getDate().toString(),observation.getPatient().getUsername(),observation.getBiometricsType().getCode(),observation.getQuantitativeValue(),observation.getQualitativeValue(),observation.getWhat(),observation.getLocal());
    }

    private List<ObservationDTO> toDTOs(List<Observation> observations) {
        return observations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<ObservationDTO> getAllObservationsWS() {
        return toDTOs(observationBean.getAllObservations());
    }


    @GET
    @Path("{code}")
    public Response getObservationDetails(@PathParam("code") int code) throws MyEntityNotFoundException {
        Observation observation = observationBean.find(code);
        if (observation != null) {
            return Response.ok(toDTO(observation)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_BIOMETRIC_TYPE")
                .build();
    }

    @POST // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response createObservationWS(ObservationDTO observationDTO) throws MyEntityNotFoundException,MyIllegalArgumentException {
        Observation observation=observationBean.create(observationDTO.getDate(),
                observationDTO.getPatient(),
                observationDTO.getBiometricsType(),
                observationDTO.getQuantitativeValue(),
                observationDTO.getWhat(),
                observationDTO.getLocal()
        );
        if (observation == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();}
        return Response.status(Response.Status.CREATED)
                .entity(toDTO(observation))
                .build();
    }

    @PATCH
    @Path("update/{code}")
    public Response Update(@PathParam("code") int code,ObservationDTO observationDTO) throws MyEntityNotFoundException, MyIllegalArgumentException {
        boolean updated=observationBean.update(code,observationDTO);
        //FUNCIONAAA
        if(!updated) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
        return Response.status(Response.Status.OK)
                .build();
    }
}
