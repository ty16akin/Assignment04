/**
 * File:  TestStudentEntity.java
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

import acmecollege.entity.Student;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestStudentEntity {
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
    public void stuTest01_createStudent_adminAuth() throws JsonMappingException, JsonProcessingException {
        Student student = new Student();
        student.setFirstName("Sam");
        student.setLastName("Hall");

        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(student));
        assertThat(response.getStatus(), is(200));
        }
    
    @Test
    public void stuTest02_createStudent_userAuth() throws JsonMappingException, JsonProcessingException {
        Student student = new Student();
        student.setFirstName("Mark");
        student.setLastName("Antony");
        
        Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(student));
            assertThat(response.getStatus(), is(403));
    }
    
    //Read Operations
    @Test
    public void stuTest03_getAll_adminAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void stuTest04_getAll_userAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void stuTest05_getStudentById_adminAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path("student/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void stuTest06_getStudentById_userAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path("student/1")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }
    
    //Update Operations
    @Test
    public void stuTest07_updateStudentById_adminAuth() throws JsonMappingException, JsonProcessingException {
    	Student update = new Student();
    	update.setFirstName("John");
    	update.setLastName("Doe");
    	Response response = webTarget
                .register(adminAuth)
                .path("student/1")
                .request()
                .put(Entity.json(update));
        assertThat(response.getStatus(), is(405));
    }
    
    @Test
    public void stuTest08_updateStudentById_userAuth() throws JsonMappingException, JsonProcessingException {
    	Student update = new Student();
    	update.setFirstName("John");
    	update.setLastName("Doe");
    	Response response = webTarget
                .register(userAuth)
                .path("student/1")
                .request()
                .put(Entity.json(update));
        assertThat(response.getStatus(), is(405));
    }
    
    //Delete Operations
    @Test
    public void stuTest09_deleteStudentById_adminAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path("student/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void stuTest10_deleteStudentById_userAuth() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path("student/2")
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }
}