package com.acme.dbo;
import com.acme.dbo.retrofit.Client;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JPANewClientTests {

    private static EntityManagerFactory entityManagerFactory;
    private RequestSpecification request;

    @BeforeAll
    public static void setUpJpa() {
        entityManagerFactory = Persistence.createEntityManagerFactory("dbo");
    }

    @AfterAll
    public static void tearDownJpa() {
        entityManagerFactory.close();
    }

    @BeforeEach
    public void setUpRestAssured() {
        request = given()
                .baseUri("http://localhost")
                .port(8080)
                .basePath("/dbo/api/")
                .header("X-API-VERSION", 1)
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void shouldGetAccountByIdWhenExists() throws SQLException {
        final EntityManager em = entityManagerFactory.createEntityManager();

        final String newLogin = "login" + new Random().nextInt();
        final Client client = new Client(newLogin, "secret", "salt", LocalDateTime.now(), true);

        em.getTransaction().begin();
        em.persist(client);
        em.getTransaction().commit();

        request.
                when().
                get("client/{id}", client.getId())
                .then()
                .statusCode(HttpStatus.SC_OK).
                body("id", equalTo(client.getId()),
                        "login", equalTo(client.getLogin()));

        em.getTransaction().begin();
        final Client clientSaved = em.find(Client.class, client.getId());
        em.remove(clientSaved);
        em.getTransaction().commit();

        em.close();
    }

    @Test
    public void shouldDeleteAccountByIdWhenExists() throws SQLException {
        final EntityManager em = entityManagerFactory.createEntityManager();

        final String newLogin = "login" + new Random().nextInt();
        final Client client = new Client(newLogin, "secret", "salt", LocalDateTime.now(), true);

        em.getTransaction().begin();
        em.persist(client);
        em.getTransaction().commit();


        request.
                when()
                .delete("/client/{id}",client.getId())
                .then()
                .statusCode(200);

        em.getTransaction().begin();

        em.close();
    }

    @Test
    public void shouldGetAllClients(){
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final List<Client> clients = entityManager.createQuery("SELECT c FROM Client c", Client.class).getResultList();
        MatcherAssert.assertThat(clients, Matchers.allOf(
                hasItem(hasProperty("login", is("admin@acme.com"))),
                hasItem(hasProperty("login", is("account@acme.com"))),
                hasItem(hasProperty("login", is("disabled@acme.com")))
        ));
    }
}
