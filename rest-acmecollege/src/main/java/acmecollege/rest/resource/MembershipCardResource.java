
/**
 * File:  SecurityUser.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  Group NN
 *   040982118, Taiwo, Akinlabi (as from ACSIS)
 *   041043679, Ryan, WAng (as from ACSIS)
 *   041004792, Mohammad,  Alshaikhahmad (as from ACSIS)
 * 
 */
package acmecollege.rest.resource;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static acmecollege.utility.MyConstants.*;
//dustyn
@Path(MEMBERSHIP_CARD_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembershipCardResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    //unsure if we really need this for courses
    @Inject
    protected SecurityContext sc;


    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getMembershipCards() {
        LOG.debug("retrieving all courses ...");
        List<MembershipCard> card = service.getAllCards();
        return Response.ok(card).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getMembershipCardById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id){
        LOG.debug("try to retrieve specific club membership " + id);
        Response response = null;
        Student student = null;
        MembershipCard membershipCard = service.getCardById(id);
        if (sc.isCallerInRole(ADMIN_ROLE)) {

            response = Response.status(membershipCard == null ? Response.Status.NOT_FOUND : Response.Status.OK).entity(membershipCard).build();
        } else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            student = sUser.getStudent();
            if (student != null && membershipCard != null && student.equals(membershipCard.getOwner())) {
                response = Response.status(Response.Status.OK).entity(student).build();
            } else {
                throw new ForbiddenException("User trying to access resource it does not own (wrong userid)");
            }
        } else {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }
        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addMembershipCard(MembershipCard newCard) {
        Response response = null;
        MembershipCard card = service.persistCard(newCard);
        // Build a SecurityUser linked to the new student
        response = Response.ok(card).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteCardById(int id) {
        LOG.debug("Deleting course with id = {}", id);
        service.deleteCardById(id);
        return Response.ok(id).build();
    }


}
