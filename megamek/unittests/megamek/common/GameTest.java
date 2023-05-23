package megamek.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import megamek.common.Game;

import java.util.List;

@RunWith(JUnit4.class)
public class GameTest {

    @Test
    public void testAddPlayer(){
        IPlayer testPlayer1 = new Player(1, "tester1");
        IPlayer testPlayer2 = new Player(2, "tester2");
        IPlayer testPlayer3 = new Player(2, "tester3");

        testPlayer1.setTeam(5);
        testPlayer2.setTeam(4);
        testPlayer3.setTeam(4);

        IGame testGame = new Game();

        testGame.addPlayer(1, testPlayer1);
        testGame.addPlayer(2, testPlayer2);
        testGame.addPlayer(3, testPlayer3);

        Assert.assertEquals(testGame.getPlayer(1), testPlayer1);
        Assert.assertEquals(testGame.getPlayer(2), testPlayer2);
        Assert.assertEquals(testGame.getPlayer(3), testPlayer3);

        List<Team> teams = testGame.getTeamsVector();

        Assert.assertEquals(teams.size(), 2);
        Assert.assertEquals(testGame.getTeamForPlayer(testPlayer1).getId(), 5);
        Assert.assertEquals(testGame.getTeamForPlayer(testPlayer2).getId(), 4);
        Assert.assertEquals(testGame.getTeamForPlayer(testPlayer2).getId(), 4);
    }
}
