package com.ecore.roles.api;

import com.ecore.roles.client.model.User;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.ecore.roles.utils.MockUtils.mockGetAllUsers;
import static com.ecore.roles.utils.MockUtils.mockGetUserById;
import static com.ecore.roles.utils.RestAssuredHelper.getAllUsers;
import static com.ecore.roles.utils.RestAssuredHelper.getUserById;
import static com.ecore.roles.utils.TestData.GIANNI_USER;
import static com.ecore.roles.utils.TestData.GIANNI_USER_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public UsersApiTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldGetUserById() {
        User expectedUser = GIANNI_USER();
        mockGetUserById(mockServer, expectedUser.getId(), expectedUser);

        UserDto response = getUserById(GIANNI_USER_UUID)
                .statusCode(200)
                .extract().as(UserDto.class);

        assertNotNull(response);
        assertEquals(response, UserDto.fromModel(expectedUser));
    }

    @Test
    void shouldGetAllUsers() {
        User expectedUser = GIANNI_USER();
        mockGetAllUsers(mockServer, List.of(expectedUser, expectedUser));

        UserDto[] actualUsers = getAllUsers()
                .statusCode(200)
                .extract().as(UserDto[].class);

        assertNotNull(actualUsers);
        assertThat(actualUsers.length).isEqualTo(2);
        assertThat(actualUsers[0]).isEqualTo(UserDto.fromModel(expectedUser));
    }

}
