package sk.tuke.gamestudio_backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sk.tuke.gamestudio_backend.entity.Score;
import sk.tuke.gamestudio_backend.service.interfaces.ScoreService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoreService scoreService;

    private List<Score> testScores;

    @BeforeEach
    void setUp() {
        testScores = new ArrayList<>();
        testScores.add(new Score("game", "player1", 100, new Timestamp(System.currentTimeMillis())));
        testScores.add(new Score("game", "player2", 90, new Timestamp(System.currentTimeMillis())));
        testScores.add(new Score("game", "player3", 80, new Timestamp(System.currentTimeMillis())));
    }

    @Test
    void testGetTopScores() throws Exception {
        // Arrange
        given(scoreService.getTopScores("game")).willReturn(testScores);

        // Act and Assert
        mockMvc.perform(get("/api/score/top/game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].game").value("game"))
                .andExpect(jsonPath("$[0].player").value("player1"))
                .andExpect(jsonPath("$[0].points").value(100))
                .andExpect(jsonPath("$[1].game").value("game"))
                .andExpect(jsonPath("$[1].player").value("player2"))
                .andExpect(jsonPath("$[1].points").value(90))
                .andExpect(jsonPath("$[2].game").value("game"))
                .andExpect(jsonPath("$[2].player").value("player3"))
                .andExpect(jsonPath("$[2].points").value(80));

        verify(scoreService).getTopScores("game");
    }

    @Test
    void testGetTopScoresNotFound() throws Exception {
        // Arrange
        given(scoreService.getTopScores("game")).willReturn(new ArrayList<>());

        // Act and Assert
        mockMvc.perform(get("/api/score/top/game"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No scores found for game game"));

        verify(scoreService).getTopScores("game");
    }
}