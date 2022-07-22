package com.kate;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.path.json.JsonPath.from;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("User flow API tests")
@Feature("Validation for the emails in the user's post comments")
public class TestUserFlow extends BasicTest {

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
}
