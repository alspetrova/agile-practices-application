package com.acme.dbo.retrofit;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT")
@JsonInclude(JsonInclude.Include.NON_NULL)

@Generated("jsonschema2pojo")
public class Client {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("login")
    @JsonPropertyDescription("Client login")
    private String login;

    @JsonProperty("salt")
    @JsonPropertyDescription("Client salt")
    private String salt;

    @JsonProperty("secret")
    @JsonPropertyDescription("Client secret")
    private String secret;



    @JsonProperty("created")
    @JsonPropertyDescription("Client created")
    private LocalDateTime created;

    @JsonProperty("enabled")
    @JsonPropertyDescription("Client enabled")
    private Boolean enabled;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Client(@JsonProperty("login") String login, @JsonProperty("salt") String salt, @JsonProperty("secret") String secret,
                  LocalDateTime created, Boolean enabled) {
        this.login = login;
        this.salt = salt;
        this.secret = secret;
        this.created = created;
        this.enabled = enabled;
    }

    @JsonProperty("login")
    public String getLogin() {
        return login;
    }

    @JsonProperty("login")
    public void setLogin(String login) {
        this.login = login;
    }

    @JsonProperty("salt")
    public String getSalt() {
        return salt;
    }

    @JsonProperty("salt")
    public void setSalt(String salt) {
        this.salt = salt;
    }

    @JsonProperty("secret")
    public String getSecret() {
        return secret;
    }

    @JsonProperty("secret")
    public void setSecret(String secret) {
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

    @Override
    public String toString() {
        return "Client{" +
                ", id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", salt='" + salt + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
