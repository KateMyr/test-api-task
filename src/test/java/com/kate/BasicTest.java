package com.kate;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

/**
 * Root class to extend from. Contains all paths and utility functions.
 */
public class BasicTest {

    /* Paths could be placed in the configuration file. For the sake of the simplicity there are here as for now. */
    protected static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    protected static final String USERS_PATH = "users";
    protected static final String POSTS_PATH = "posts";
    protected static final String COMMENTS_PATH = "comments";
    protected static final String USER_NAME_TO_FIND = "Delphine";

    @Step("Get posts by userId: {userId}")
    protected List<Integer> getPostIdsByUserId(Integer userId) {
        Response response = requestByPath(POSTS_PATH);
        return from(response.asString()).getList(String.format("findAll {it.userId == %s}.id", userId));
    }

    @Step("Get emails from comments by posts' ids")
    protected List<String> getEmailsByListOfPostIds(List<Integer> postIds) {
        Response response = requestByPath(COMMENTS_PATH);
        List<String> emails = new ArrayList<>();
        postIds.forEach(postId -> emails.addAll(from(response.asString()).getList(String.format("findAll {it.postId == %s}.email", postId))));
        return emails;
    }

    @Step("Request by path: {path}")
    protected Response requestByPath(String path) {
        return given()
                .filter(new AllureRestAssured())
                .when()
                .get(BASE_URL + path)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
    }

    /**
     * This is a validation which checks that email contains '@' symbol in the address by these rules:
     * - allow numeric values from 0 to 9.
     * - allow both uppercase and lowercase letters from a to z.
     * - hyphen “-” and dot “.” aren't allowed at the start and end of the domain part.
     * - no consecutive dots allowed.
     *
     * @param email to check
     * @return true if validation passed, otherwise returns false
     */
    @Step("Check that email is valid. Email: {email}")
    protected boolean checkEmailIsValid(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
