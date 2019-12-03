package br.com.rruizdasilva;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class EnvioDadosTest {

    @Test
    public void devoEnviarValorViaQuery() {
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/v2/users?format=xml")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML);
    }
}
