package br.com.rruizdasilva;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class RequestAndResponseSpecificationTest {
    public static RequestSpecification requestSpec;
    public static ResponseSpecification responseSpec;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";
        RestAssured.port = 443; // como este é o valor padrão, este comando pode ser omitido
        RestAssured.basePath = ""; // como o valor é vazio, este comando pode ser omitido

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpec = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(200);
        responseSpec = responseSpecBuilder.build();
    }

    @Test
    public void deveTrabalharComXML() {
        given()
                .spec(requestSpec)
                .when()
                .get("/usersXML/3")
                .then()
                // .statusCode(200)
                .spec(responseSpec)
        ;
    }
}
