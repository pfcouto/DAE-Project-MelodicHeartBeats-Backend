package pt.ipleiria.estg.dei.ei.dae.projetodae.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodae.dtos.RuleDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodae.ejbs.RuleBean;
import pt.ipleiria.estg.dei.ei.dae.projetodae.entities.Rule;
import pt.ipleiria.estg.dei.ei.dae.projetodae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

@Path("rules")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class RuleService {
    @EJB
    RuleBean ruleBean;
    @Context
    private SecurityContext securityContext;

    RuleDTO toDTO(Rule rule) {
        return new RuleDTO(
                rule.getId(),
                rule.getBiometricType().getCode(),
                rule.getBiometricType().getName(),
                rule.getExp(),
                rule.getValue(),
                rule.getDescription(),
                rule.getDays()
        );
    }

    private List<RuleDTO> toDTOs(List<Rule> rules) {
        return rules.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")

    public List<RuleDTO> getAllRules() {
        return toDTOs(ruleBean.getAllRules());
    }

    @POST
    @Path("/")
    public Response createNewRule(RuleDTO ruleDTO) throws InvalidAttributesException, MyEntityNotFoundException {
        ruleBean.create(
                ruleDTO.getBiometricTypeCode(),
                ruleDTO.getExp(),
                ruleDTO.getValue(),
                ruleDTO.getDescription(),
                ruleDTO.getDays());

        return Response.status(Response.Status.CREATED)
                .build();
    }


    @GET
    @Path("{rule}")
    public Response getPrescriptionDetails(@PathParam("rule") int ruleId) {

        Rule rule = ruleBean.findRule(ruleId);
        if (rule == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_RULE")
                    .build();
        }

        return Response.ok(toDTO(rule)).build();
    }


    @PUT
    @Path("{rule}")
    public Response createNewPrescription(@PathParam("rule") int ruleId, RuleDTO ruleDTO) throws MyEntityNotFoundException {
        ruleBean.updateRule(
                ruleId,
                ruleDTO.getBiometricTypeCode(),
                ruleDTO.getExp(),
                ruleDTO.getValue(),
                ruleDTO.getDescription(),
                ruleDTO.getDays()
        );

        Rule newRule = ruleBean.findRule(ruleId);
        if (newRule == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK)
                .entity(toDTO(newRule))
                .build();
    }

    @DELETE
    @Path("{rule}")
    public Response deleteDoctor(@PathParam("rule") int ruleId) throws MyEntityNotFoundException {

        ruleBean.deleteRule(ruleId);

        if (ruleBean.findRule(ruleId) == null) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.EXPECTATION_FAILED)
                .entity("ERROR_DELETING_RULE")
                .build();
    }
}
