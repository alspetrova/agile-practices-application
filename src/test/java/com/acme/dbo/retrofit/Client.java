package com.acme.dbo.retrofit;


import com.fasterxml.jackson.annotation.*;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
@JsonInclude(JsonInclude.Include.NON_NULL)

@Entity
@Table(name = "CLIENT")
@Generated("jsonschema2pojo")
public class Client {


    public Client(@JsonProperty("login") String login, @JsonProperty("salt") String salt, @JsonProperty("secret") String secret) {
        this.login = login;
        this.salt = salt;
        this.secret = secret;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Client(String login, String secret, String salt, LocalDateTime created, boolean enabled) {
        this.login = login;
        this.salt = salt;
        this.secret = secret;
        this.created=created;
        this.enabled=enabled;
    }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @JsonPropertyDescription("Client id for auth")
    private int id;
    private String login;
    private String salt;
    private String secret;
    private LocalDateTime created;
    private boolean enabled;

    /*@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    */

    public Client() {
    }
}



