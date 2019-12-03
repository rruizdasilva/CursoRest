package br.com.rruizdasilva;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class SerializationTest {

    @Test
    public void devoSalvarUsuarioUsandoMap() {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("name", "Usuario via map");
        params.put("age", 25);
        given()
            .log().all()
            .contentType("application/json")
            .body(params)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Usuario via map"))
            .body("age", is(25))
        ;
    }

    @Test
    public void devoSalvarUsuarioUsandoObjectJson() {
        User user = new User("Usuario via objeto", 35);
        given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Usuario via objeto"))
            .body("age", is(35))
        ;
    }

    @Test
    public void devoDeserealizarObjetoAoSalvarUsuarioJson() {
        User user = new User("Usuario deserealizado", 35);
        User usuarioInserido = given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class);

        System.out.println(usuarioInserido);
        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertThat(usuarioInserido.getName(), is("Usuario deserealizado"));
        Assert.assertEquals(new Integer(35), usuarioInserido.getAge());
    }

    @Test
    public void devoSerializarObjetoAoSalvarUsuarioXML() {
        User user = new User("Usuario XML", 40);
        given()
            .log().all()
            .contentType(ContentType.XML)
            .body(user)
            .when()
            .post("https://restapi.wcaquino.me/usersXML")
            .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", is(notNullValue()))
            .body("user.name", is("Usuario XML"))
            .body("user.age", is("40"))
        ;
    }
}
