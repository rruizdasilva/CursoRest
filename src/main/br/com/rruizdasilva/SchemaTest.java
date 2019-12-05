package br.com.rruizdasilva;

import io.restassured.matcher.RestAssuredMatchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class SchemaTest {

    @Test
    public void deveValidarSchemaXML(){
        given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(200)
            .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
        ;
    }
}
