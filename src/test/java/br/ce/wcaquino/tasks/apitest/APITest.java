package br.ce.wcaquino.tasks.apitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class APITest {

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTarefas(){
        RestAssured.given()
                .when()
                    .get("/todo")
                .then()
                .statusCode(200);

    }

    @Test
    public void deveAdicionarTarefaComSucesso(){
        RestAssured.given()
                .body("\n" +
                        "{\"task\" : \"Task Teste API\", \"dueDate\" : \"2023-12-01\"}")
                .contentType(ContentType.JSON)
                .when()
                    .post("/todo")
                .then()
                .statusCode(201);


    }

    @Test
    public void naoDeveAdicionarTarefaInvalida(){
        RestAssured.given()
                .body("\n" +
                        "{\"task\" : \"Task Teste API\", \"dueDate\" : \"2010-12-01\"}")
                .contentType(ContentType.JSON)
                .when()
                    .post("/todo")
                .then()
                .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));


    }

    @Test
    public void deveRemoverTarefaComSucesso(){
        //Inserir
        Integer id = RestAssured.given()
                .body("\n" +
                        "{\"task\" : \"Tarefa para delete\", \"dueDate\" : \"2022-12-01\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/todo")
                .then().statusCode(201)
                .extract().path("id");

        RestAssured.given().when().delete("/todo/"+id).then().statusCode(204);

    }


}
