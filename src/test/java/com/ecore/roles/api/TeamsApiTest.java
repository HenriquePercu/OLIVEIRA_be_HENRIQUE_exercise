package com.ecore.roles.api;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.TeamDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.ecore.roles.utils.MockUtils.mockGetAllTeams;
import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.RestAssuredHelper.getAllTeams;
import static com.ecore.roles.utils.RestAssuredHelper.getTeamById;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamsApiTest {
    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Autowired
    public TeamsApiTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void shouldGetAllTeams() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetAllTeams(mockServer, List.of(expectedTeam, expectedTeam));

        TeamDto[] actualTeams = getAllTeams()
                .statusCode(200)
                .extract().as(TeamDto[].class);

        assertNotNull(actualTeams);
        assertThat(actualTeams.length).isEqualTo(2);
        assertThat(actualTeams[0]).isEqualTo(TeamDto.fromModel(expectedTeam));
    }

    @Test
    void shouldGetTeamById() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetTeamById(mockServer, expectedTeam.getId(), expectedTeam);


        TeamDto actualTeam = getTeamById(expectedTeam.getId())
                .statusCode(200)
                .extract().as(TeamDto.class);

        assertNotNull(actualTeam);
        assertEquals(actualTeam, TeamDto.fromModel(expectedTeam));
    }

}
