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

public class RequestAndResponseSpecificationRefactoredTest {
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

        /* com essas declarações, as validações que foram definidas em requestSpecBuilder
        e responseSpecBuilder ficam implícitas */
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }

    @Test
    public void deveTrabalharComXML() {
        given()
                .when()
                // .spec(requestSpec) - não é mais necessário devido a declaração RestAssured.requestSpecification = requestSpec
                .get("/usersXML/3")
                .then()
                // .spec(responseSpec) - não é mais necessário devido a declaração RestAssured.requestSpecification = responseSpec
        ;
    }
}
