package br.com.rruizdasilva;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;

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

    @Test
    public void devoFazerUploadArquivo(){
        given()
        .log().all()
        .multiPart("arquivo", new File("src/main/resources/users.pdf"))
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
        .log().all()
        .statusCode(200)
        .body("name", is("users.pdf"))
        ;
    }
}
