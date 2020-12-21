package org.acme.config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GreetingResourceTest {

    // Default Tests
    /* @Test
    void testHelloEndpoint() {
        given()
                .when().get("/config-yaml/greeting")
                .then()
                .statusCode(200)
                .body(is("hello quarkus!"));
    } */

}
