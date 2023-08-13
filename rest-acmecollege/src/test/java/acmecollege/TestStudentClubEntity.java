/**
 * File:  TestStudentClubEntity.java
 * CST8277 Group Assignment
 * @date 2023 08
 * @author Ryan Wang
 * 
 * Group 11
 * Ryan Wang 041043679
 * Mohammed Alshaikhahmad 041004792
 * Akinlabi Taiwo 040982118
 */
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.STUDENT_CLUB_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.STUDENT_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmecollege.entity.NonAcademicStudentClub;
import acmecollege.entity.Student;
import acmecollege.entity.StudentClub;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestStudentClubEntity {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

  //Create operations
    @Test
    public void clubTest01_createClub_adminAuth() throws JsonMappingException, JsonProcessingException {
    	NonAcademicStudentClub club = new NonAcademicStudentClub();
    	club.setName("Chess Club");

        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(club));
        assertThat(response.getStatus(), is(200));
        }
    
    @Test
    public void clubTest02_createClub_userAuth() throws JsonMappingException, JsonProcessingException {
    	NonAcademicStudentClub club = new NonAcademicStudentClub();
    	club.setName("Parachuting Club");
        
        try (Response response = webTarget
                .register(userAuth)
                .path(STUDENT_CLUB_RESOURCE_NAME)
                .request()
                .post(Entity.json(club))) {
            assertThat(response.getStatus(), is(403));
        }
    }
    
    //Read Operations
    @Test
    public void clubTest03_getAll_adminAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_CLUB_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void clubTest04_getAll_userAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(STUDENT_CLUB_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void clubTest05_getClubById_adminAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path("studentclub/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void clubTest06_getClubById_userAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path("studentclub/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }
    
  //Update Operations
    @Test
    public void clubTest07_updateClubById_adminAuth() throws JsonMappingException, JsonProcessingException {
    	StudentClub update = new NonAcademicStudentClub();
    	update.setName("Cooking Club");
    	
    	Response response = webTarget
                .register(adminAuth)
                .path("studentclub/1")
                .request()
                .put(Entity.json(update));
        assertThat(response.getStatus(), is(405));
    }
    
    @Test
    public void clubTest08_updateClubById_userAuth() throws JsonMappingException, JsonProcessingException {
    	StudentClub update = new NonAcademicStudentClub();
    	update.setName(" Club");
    	
    	Response response = webTarget
                .register(userAuth)
                .path("studentclub/1")
                .request()
                .put(Entity.json(update));
        assertThat(response.getStatus(), is(405));
    }
    
    //Delete Operations
    @Test
    public void clubTest09_deleteClubById_adminAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path("studentclub/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void stuTest10_deleteClubById_userAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path("studentclub/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }
}