package br.com.rruizdasilva;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
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

    @Test
    public void deveAcessarAplicacaoWeb(){
        // login
        String cookie = given()
                .log().all()
                .formParam("email", "RogerSilva@roger.com")
                .formParam("senha", "123456")
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
            .when()
                .post("http://seubarriga.wcaquino.me/logar")
            .then()
                .log().all()
                .statusCode(200)
                .extract().header("set-cookie")
                ;

        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);

        // obter conta

        String body = given()
                .log().all()
                .cookie("connect.sid", cookie)
            .when()
                .get("http://seubarriga.wcaquino.me/contas")
            .then()
                .log().all()
                .statusCode(200)
                // .body("html.body.table.tbody.tr[0].td[0]", is("Conta de teste"))
                // .extract().path("html.body.table.tbody.tr[0].td[0]") - retorna XMLPath
                .extract().body().asString();
        ;

        System.out.println("---------------");
        XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
    }
}
