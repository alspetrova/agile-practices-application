package com.acme.dbo;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static com.acme.dbo.DefaultEndPoint.*;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


public class JdbcAccountTests {
    private RequestSpecification request;
    private Connection connection;

    @BeforeEach
    public void setUpDbConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/dbo-db");
    }

    @AfterEach
    public void closeDbConnection() throws SQLException {
        connection.close();
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
    public void shouldGetAllAccounts() throws SQLException {
        int accountsCount;
        try (final PreparedStatement countAccounts = connection.prepareStatement("Select count(*) from account");
             final ResultSet resultSet = countAccounts.executeQuery()) {
            assumeTrue(resultSet.next());
            accountsCount = resultSet.getInt(1);
        }

        request.when().get("account")
                .then().statusCode(200)
                .body("size()", is(accountsCount)).statusCode(SC_OK);
    }

}
