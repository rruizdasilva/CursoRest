package br.com.rruizdasilva;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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

    @Test
    public void devoEnviarValorViaQueryViaParam() {
        given()
            .log().all()
            .queryParam("format", "xml")
            .queryParam("outra", "coisa")
        .when()
            .get("https://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML)
            .contentType(containsString("utf-8"))
        ;
    }
}
