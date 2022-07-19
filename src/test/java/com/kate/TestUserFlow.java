package com.kate;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("User flow API tests")
@Feature("Validation for the emails in the user's post comments")
public class TestUserFlow {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String USERS_PATH = "users";
    private static final String POSTS_PATH = "posts";
    private static final String COMMENTS_PATH = "comments";
    private static final String USER_NAME_TO_FIND = "Delphine";

    @Test
    @Story("Emails in comments for user's posts are valid")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmailsInUserPostsComments() {
        Response response = requestByPath(USERS_PATH);

        List<Integer> listOfUserIdsByUsername = from(response.asString()).getList(String.format("findAll {it.username == '%s'}.id", USER_NAME_TO_FIND));
        assertEquals(1, listOfUserIdsByUsername.size(), "Expected user hasn't been found or we found more users with such name");

        List<Integer> postIdsByUserId = getPostIdsByUserId(listOfUserIdsByUsername.get(0));
        List<String> emailsByListOfPostIds = getEmailsByListOfPostIds(postIdsByUserId);
        emailsByListOfPostIds.forEach(email -> assertTrue(checkEmailIsValid(email), String.format("Email: %s is not valid", email)));
    }


    @Step("Get posts by userId: {userId}")
    private List<Integer> getPostIdsByUserId(Integer userId) {
        Response response = requestByPath(POSTS_PATH);
        return from(response.asString()).getList(String.format("findAll {it.userId == %s}.id", userId));
    }

    @Step("Get emails from comments by posts' ids")
    private List<String> getEmailsByListOfPostIds(List<Integer> postIds) {
        Response response = requestByPath(COMMENTS_PATH);
        List<String> emails = new ArrayList<>();
        postIds.forEach(postId -> emails.addAll(from(response.asString()).getList(String.format("findAll {it.postId == %s}.email", postId))));
        return emails;
    }

    @Step("Request by path: {path}")
    private Response requestByPath(String path) {
        return given()
                .filter(new AllureRestAssured())
                .when()
                .get(BASE_URL + path)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
    }

    /**
     * This is the simple validation which checks that email contains '@' symbol in the address
     * @param email to check
     * @return true if validation passed, otherwise returns false
     */
    @Step("Check that email is valid. Email: {email}")
    private boolean checkEmailIsValid(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
