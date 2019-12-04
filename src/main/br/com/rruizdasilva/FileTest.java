package br.com.rruizdasilva;

import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FileTest {

    @Test
    public void devoObrigarEnvioArquivo(){
        given()
            .log().all()
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .statusCode(404)
            .body("error", is("Arquivo não enviado"))
        ;
    }
}
