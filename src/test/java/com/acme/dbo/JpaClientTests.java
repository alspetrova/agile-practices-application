package com.acme.dbo;

import com.acme.dbo.retrofit.Client;
import com.acme.dbo.retrofit.ClientService;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import retrofit2.Response;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.acme.dbo.DefaultEndPoint.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class JpaClientTests {
    private RequestSpecification request;
    private static EntityManagerFactory entityManagerFactory;
    private ClientService service;

    @BeforeAll
    public static void setUpJpa(){
        entityManagerFactory= Persistence.createEntityManagerFactory("dbo");
    }

    @AfterAll
    public static void tearDownJpa(){
        entityManagerFactory.close();
    }

    @BeforeEach
    public void setUpRestAssured(){
        request= given()
                .baseUri(BASE_URL)
                .port(PORT)
                .basePath(DBO_API)
                .header("X-API-VERSION", 1)
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void shouldGetAllClients(){
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final List<Client> clients = entityManager.createQuery("SELECT c FROM testClient c", Client.class).getResultList();
        MatcherAssert.assertThat(clients, Matchers.allOf(
                hasItem(hasProperty("login", is("admin@acme.com"))),
                hasItem(hasProperty("login", is("account@acme.com"))),
                hasItem(hasProperty("login", is("disabled@acme.com")))
        ));
    }

    @Test
    public void shouldGetClientById(){
        final String newLogin="login" +new Random().nextInt();
        final Client client = new Client(newLogin,"secret","salt",LocalDateTime.now(),true);

        final EntityManager em= entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(client);
        em.getTransaction().commit();

        request
                .when()
                .get(CLIENT_ID,client.getId())
                .then()
                .statusCode(SC_OK)
                .body("id",is(client.getId()),
                        "login",is(client.getLogin()));
        em.getTransaction().begin();
        final Client clientSaved = em.find(Client.class,client.getId());
        em.remove(clientSaved);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void shouldPost() throws IOException {
        String email = UUID.randomUUID() + "@mail.ru";
        String salt = "some-salt";
        String secret = "749f09bade8aca7556749f09bade8aca7556";
        Response<Client> execute = service.createClient(new Client(email, salt, secret,LocalDateTime.now(),true)).execute();
        assertEquals(SC_CREATED, execute.code());
        assertEquals(email, execute.body().getLogin());
        assertEquals(salt, execute.body().getSalt());
        assertEquals(secret, execute.body().getSecret());

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final Client clientSaved = entityManager.find(Client.class, execute.body().getId());
        entityManager.remove(clientSaved);
        entityManager.getTransaction().commit();

        entityManager.close();

    }

}

