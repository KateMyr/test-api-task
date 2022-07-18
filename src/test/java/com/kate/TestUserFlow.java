package com.kate;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("User flow API tests")
@Feature("Validation for the emails in the user's post comments")
public class TestUserFlow {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String USERS_PATH = "users";
    private static final String USER_NAME_TO_FIND = "Delphine";

    @Test
    @Story("User id could be found by username")
    @Severity(SeverityLevel.CRITICAL)
    public void validateThereIsAUserWithCertainName() {
        Response response = given()
                .filter(new AllureRestAssured())
                .when()
                .get(BASE_URL + USERS_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        List<String> listOfUserIdsByUsername = from(response.asString()).getList(String.format("findAll {it.username == '%s'}.id", USER_NAME_TO_FIND));
        assertEquals(1, listOfUserIdsByUsername.size(), "Expected user hasn't been found or we found more users with such name");
    }
}
