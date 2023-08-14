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

import acmecollege.ejb.ACMECollegeService;
import acmecollege.entity.Course;
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

//Dustyn
@Path(COURSE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMECollegeService service;

    //unsure if we really need this for courses
    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getCourses() {
        LOG.debug("retrieving all courses ...");
        List<Course> course = service.getAllCourses();
        return Response.ok(course).build();
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    public Response deleteCourseById(int id) {
        LOG.debug("Deleting course with id = {}", id);
        service.deleteCourseById(id);
        return Response.ok(id).build();
    }


    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addCourse(Course newCourse) {

        Course course = service.persistCourse(newCourse);
        // Build a SecurityUser linked to the new student
        return Response.ok(course).build();
    }

    @GET
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getCourseById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("retrieving course with id = {}", id);
        Course course = service.getById(Course.class, Course.COURSE_BY_ID, id);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            Student student = sUser.getStudent();
            if (student == null || !student.getCourseRegistrations().contains(course)) {
                throw new ForbiddenException("User trying to access resource it does not own (wrong userid)");
            }
        }
        return Response.ok(course).build();
    }


}