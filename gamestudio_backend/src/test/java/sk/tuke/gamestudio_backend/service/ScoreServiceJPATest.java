package sk.tuke.gamestudio_backend.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.tuke.gamestudio_backend.entity.Score;
import sk.tuke.gamestudio_backend.repository.ScoreRepository;
import sk.tuke.gamestudio_backend.service.exceptions.ScoreException;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceJPATest {

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks ScoreServiceJPA scoreServiceJPA;

    private Score score;
    private List<Score> testScores;

    @BeforeEach
    public void setUp() {
        score = new Score("game", "player", 100, new Timestamp(System.currentTimeMillis()));

        testScores = new ArrayList<>();
        testScores.add(new Score("game", "player1", 100, new Timestamp(System.currentTimeMillis())));
        testScores.add(new Score("game", "player2", 90, new Timestamp(System.currentTimeMillis())));
        testScores.add(new Score("game", "player3", 80, new Timestamp(System.currentTimeMillis())));
    }

    @DisplayName("Test addScore")
    @Test
    public void testAddScore() throws ScoreException {
        // Arrange
        given(scoreRepository.save(score)).willReturn(score);

        // Act
        try {
            scoreServiceJPA.addScore(score);
        } catch (ScoreException e) {
            fail("ScoreException should not have been thrown: " + e.getMessage());
        }

        // Assert
        verify(scoreRepository).save(score);
    }

    @DisplayName("Test getTopScores")
    @Test
    public void testGetTopScores() {
        // Arrange
        given(scoreRepository.findTop10ByGameOrderByPointsDesc("game")).willReturn(testScores);

        // Act
        List<Score> topScores = null;
        try {
            topScores = scoreServiceJPA.getTopScores("game");
        } catch (ScoreException e) {
            fail("ScoreException should not have been thrown: " + e.getMessage());
        }

        // Assert
        assertEquals(testScores.size()+5, topScores.size());
        assertEquals(testScores, topScores);
        verify(scoreRepository).findTop10ByGameOrderByPointsDesc("game");
    }

}