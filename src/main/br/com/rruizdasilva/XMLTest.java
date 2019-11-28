package br.com.rruizdasilva;

import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class XMLTest {

    @Test
    public void deveTrabalharComXML(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/usersXML/3")
                .then()
                .statusCode(200)
                .body("user.name", is("Ana Julia"))
                // para atributo, utiliza-se o @
                // para XML, id é string e não número
                .body("user.@id", is("3"))
                .body("user.filhos.name.size()", is(2))
                .body("user.filhos.name[0]", is("Zezinho"))
                .body("user.filhos.name[1]", is("Luizinho"))
                .body("user.filhos.name", hasItem("Luizinho"))
                .body("user.filhos.name", hasItems("Luizinho", "Zezinho"))
        ;
    }

    @Test
    public void deveTrabalharComXMLeRootPath(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/usersXML/3")
                .then()
                .statusCode(200)
                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id", is("3"))

                .rootPath("user.filhos")
                .body("name.size()", is(2))

                .detachRootPath("filhos")
                .body("filhos.name[0]", is("Zezinho"))

                .appendRootPath("filhos")
                .body("name[1]", is("Luizinho"))
                .body("name", hasItem("Luizinho"))
                .body("name", hasItems("Luizinho", "Zezinho"))
        ;
    }

    @Test
    public void devoFazerPesquisasAvancadasComXML(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/usersXML")
                .then()
                .statusCode(200)
                .body("users.user.size()", is(3))
                .body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2)) // toInteger é necessário pois XML só entende string
                .body("users.user.@id", hasItems("1", "2", "3")) // toInteger é necessário pois XML só entende string
                .body("users.user.find{it.age == 25}.name", is("Maria Joaquina")) // toInteger é necessário pois XML só entende string
                .body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
                .body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
                .body("users.user.age.collect{it.toInteger() * 2}", hasItems(40, 50, 60))
                .body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}",
                        is("MARIA JOAQUINA"))
        ;
    }
}
