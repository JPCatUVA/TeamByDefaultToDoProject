package teambydefault.todo.rest_assured_tests;

// The REST Assured team recommends including these static imports for ease of framework use
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import teambydefault.todo.entity.User;
import teambydefault.todo.service.UserService;
import teambydefault.todo.repo.UserRepo;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class RestAssuredRegistAndLogin {
    
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setup(){
        // we can specify where we want all of our requests to be sent
        RestAssured.baseURI = "http://localhost";
        // we can specify the port for the requests
        RestAssured.port = port;
    }

    @Test
    void registerValidUserTest() {
        
        // test data
        User testUser = new User();
        testUser.setEmail("dood@chadness.com");
        testUser.setPassword("P@ssw0rd");

        // the given() let's us set up the headers, body, cookies, any sort of pre-conditions needed for us to make the request
        given()
            .contentType(ContentType.JSON)
            .body(testUser)
                // the when() is where we decide what type of request we are actually making and the endpoint we are hitting
            .when()
            .post("/register")
                // the then() is where we perform our validation
            .then()
            .statusCode(201);

        
    }

    @Test
    void registerUserVerifyDatabaseStateTest() {
        String testEmail = "statecheck@test.com";

        User testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword("P@ssw0rd");

        // register the user via the API
        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // verify the user now exists in the database
        Optional<User> savedUser = userRepo.findByEmail(testEmail);
        assertTrue(savedUser.isPresent(), "User should be persisted in the database after registration");
        assertEquals(testEmail, savedUser.get().getEmail());
        assertNotNull(savedUser.get().getUserId(), "User should have a generated UUID");
    }

    @Test
    void rejectInvalidPasswordTest() {
        User testUser = new User();
        testUser.setEmail("dood@chadness.com");
        testUser.setPassword("Password");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
            .when()
            .post("/register")
            .then()
            .statusCode(400);
    }

    @Test
    void rejectInvalidEmailTest1() {
        User testUser = new User();
        testUser.setEmail("dood");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
            .when()
            .post("/register")
            .then()
            .statusCode(400);
    }

    @Test
    void rejectInvalidEmailTest2() {
        User testUser = new User();
        testUser.setEmail("dood@");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
            .when()
            .post("/register")
            .then()
            .statusCode(400);
    }

    @Test
    void rejectInvalidEmailTest3() {
        User testUser = new User();
        testUser.setEmail("@");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
            .when()
            .post("/register")
            .then()
            .statusCode(400);
    }

    @Test
    void rejectInvalidEmailTest4() {
        User testUser = new User();
        testUser.setEmail("@blab.com");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
            .when()
            .post("/register")
            .then()
            .statusCode(400);
    }

    @Test
    void loginValidUserTest() {
        // first register the user so they exist in the DB
        User testUser = new User();
        testUser.setEmail("logintest@valid.com");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // now login with the same credentials
        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body(not(emptyString())); // should return a JWT token
    }

    @Test
    void rejectUnknownUserLoginTest() {
        User testUser = new User();
        testUser.setEmail("ghost@doesnotexist.com");
        testUser.setPassword("P@ssw0rd");

        given()
            .contentType(ContentType.JSON)
            .body(testUser)
        .when()
            .post("/login")
        .then()
            .statusCode(401);
    }

}
