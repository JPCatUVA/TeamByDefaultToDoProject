package teambydefault.todo.rest_assured_tests.subtaskTests;

//Recommended imports from RESTAssured
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
//other imports
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

//Unsure why this needs to be imported twice but otherwise 
//there is a problem with RestAssured.baseURI
import io.restassured.RestAssured;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class positiveTests {
    
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http.localhost/subtask";
        RestAssured.port = port;
    }

    /**
    * Runs once AFTER ALL tests in a subclass finish. Resets REST Assured's static config so it
    * doesn't leak into other test classes.
    */
    @AfterAll
    static void tearDown() {
    RestAssured.reset();
    }




    //Test 1
    @Test
    public void addSubtaskTest(){
        
    }
}
