package com.kate;


import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * There are basic flows with happy path tests.
 */
@Epic("Basic API tests")
@Feature("Basic flows with happy path tests")
public class TestBasicFlows extends BasicTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @Story("Get a post by post id")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPost(int postId) {
        Response response = requestByPath(POSTS_PATH + "/" + postId);
        response.then().assertThat().body("id", equalTo(postId));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @Story("Get a comment by comment id")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetComment(int commentId) {
        Response response = requestByPath(COMMENTS_PATH + "/" + commentId);
        response.then().assertThat().body("id", equalTo(commentId));
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    @Story("Get a post by user id")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPostsByUser(int userId) {
        List<Integer> postIdsByUserId = getPostIdsByUserId(userId);
        assertNotEquals(0, postIdsByUserId.size(), "Expected to have some posts by userId");
    }
}
