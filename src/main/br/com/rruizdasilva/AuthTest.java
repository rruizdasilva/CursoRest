package br.com.rruizdasilva;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

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
}
