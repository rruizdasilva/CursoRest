package br.com.rruizdasilva;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class AuthTest {

    public static RequestSpecification reqSpec;

    @BeforeClass
    public static void setup(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpecBuilder.setPort(80);
        requestSpecBuilder.setUrlEncodingEnabled(false);
        reqSpec = requestSpecBuilder.build();
    }

    @Test
    public void deveAcessarSWAPI() {
        given()
            .log().all()
        .when()
            .get("https://swapi.co/api/people/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Luke Skywalker"))
                ;
    }

    @Test
    public void deveObterClima(){
        given()
            .spec(reqSpec)
            .log().all()
            .queryParam("q", "Sao+Paulo")
            .queryParam("appid", "67aeb962099868f850e143c3fa0be8e8")
            .queryParam("units", "metric")
        .when()
            .get("http://api.openweathermap.org/data/2.5/weather")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Sao Paulo"))
            .body("coord.lon", is(-46.64f))
            .body("main.temp", greaterThan(15f))
        ;
    }

    @Test
    public void naoDeveAcessarSemSenha(){
        given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(401)
        ;

    }

    @Test
    public void deveFazerAutenticacaoBasica(){
        given()
                .log().all()
                .when()
                .get("https://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica2(){
        given()
                .log().all()
                .auth().basic("admin", "senha")
                .when()
                .get("https://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaChallenge(){
        given()
                .log().all()
                .auth().preemptive().basic("admin", "senha")
                .when()
                .get("https://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoComTokenJWT(){
        Map<String, String> login = new HashMap<String, String>();
        login.put("email",  "RogerSilva@roger.com");
        login.put("senha", "123456");

        // login na api
        // receber o token
        String token = given()
                .log().all()
                .body(login)
                .contentType(ContentType.JSON)
            .when()
                .post("http://barrigarest.wcaquino.me/signin")
            .then()
                .log().all()
                .statusCode(200)
                .extract().path("token");

        // obter as rotas
        given()
            .log().all()
            .header("Authorization", "JWT " + token)
        .when()
            .get("http://barrigarest.wcaquino.me/contas")
        .then()
            .log().all()
            .statusCode(200)
            .body("nome", hasItem("Conta de teste"));
    }
}
