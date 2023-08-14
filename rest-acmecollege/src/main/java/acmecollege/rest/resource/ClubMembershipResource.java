/**
 * File:  SecurityUser.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  Group 11
 *   040982118, Taiwo, Akinlabi (as from ACSIS)
 *   041043679, Ryan, WAng (as from ACSIS)
 *   041004792, Mohammad,  Alshaikhahmad (as from ACSIS)
 * 
 */
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.*;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.ClubMembership;

import acmecollege.entity.SecurityUser;
import acmecollege.entity.Student;

@Path(CLUB_MEMBERSHIP_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClubMembershipResource {
    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    //unsure if we really need this for courses
    @Inject
    protected SecurityContext sc;


    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getClubMemberships() {
        LOG.debug("retrieving all club memberships ...");
        List<ClubMembership> students = service.getAllClubMembership();
        Response response = Response.ok(students).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getClubMembershipById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("try to retrieve specific club membership " + id);
        Response response = null;
        Student student = null;

        ClubMembership clubMembership = service.getClubMembershipById(id);

        if (sc.isCallerInRole(ADMIN_ROLE)) {

            response = Response.status(clubMembership == null ? Status.NOT_FOUND : Status.OK).entity(clubMembership).build();
        } else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            student = sUser.getStudent();
            if (student != null && clubMembership != null && student.equals(clubMembership.getCard().getOwner())) {
                response = Response.status(Status.OK).entity(student).build();
            } else {
                throw new ForbiddenException("User trying to access resource it does not own (wrong userid)");
            }
        } else {
            response = Response.status(Status.BAD_REQUEST).build();
        }
        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addMembership(ClubMembership newMembership) {

        LOG.debug("Deleting club membership with id = {}", newMembership.getId());
        Response response = null;
        ClubMembership newClubMembership = service.persistClubMembership(newMembership);

        response = Response.ok(newClubMembership).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteClubMembershipById(int id) {
        LOG.debug("Deleting club membership with id = {}", id);
        service.deleteClubMembershipById(id);
        return Response.ok(id).build();
    }

}