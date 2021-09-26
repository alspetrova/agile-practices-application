package com.acme.dbo.retrofit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.*;


public class RetrofitClientTests {
    private ClientService service;

    @BeforeEach
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("http://localhost:8080/dbo/api/")
                .build();

        service = retrofit.create(ClientService.class);
    }

    @Test
    @DisplayName("POST client")
    public void shouldPost() throws IOException {

        String email = UUID.randomUUID() + "@mail.ru";
        String salt = "some-salt";
        String secret = UUID.randomUUID().toString();

        Response<Client> execute = service.createClient(new Client(email, salt, secret)).execute();
        assertEquals(SC_CREATED, execute.code());
        assertEquals(email, execute.body().getLogin());
        assertEquals(salt, execute.body().getSalt());
        assertEquals(secret, execute.body().getSecret());

    }

    @Test
    @DisplayName("GET all clients")
    public void shouldGetAllClients() throws IOException {
        service.getClients().execute().body().forEach(System.out::println);
        Response<List<Client>> execute = service.getClients().execute();
        assertEquals(SC_OK, execute.code());
        int size = service.getClients().execute().body().size();
        assertTrue(size > 3);
    }
}
