package megamek.common;

import megamek.server.victory.Victory;
import megamek.server.victory.VictoryResult;
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
        IPlayer player1 = new Player(1, "tester1");
        IPlayer player2 = new Player(2, "tester2");
        IPlayer player3 = new Player(2, "tester3");

        player1.setTeam(5);
        player2.setTeam(4);
        player3.setTeam(4);

        IGame game = new Game();

        game.addPlayer(1, player1);
        game.addPlayer(2, player2);
        game.addPlayer(3, player3);

        Assert.assertEquals(game.getPlayer(1), player1);
        Assert.assertEquals(game.getPlayer(2), player2);
        Assert.assertEquals(game.getPlayer(3), player3);

        List<Team> teams = game.getTeamsVector();

        Assert.assertEquals(teams.size(), 2);
        Assert.assertEquals(game.getTeamForPlayer(player1).getId(), 5);
        Assert.assertEquals(game.getTeamForPlayer(player2).getId(), 4);
        Assert.assertEquals(game.getTeamForPlayer(player2).getId(), 4);
    }


    @Test
    public void testVictoryPlayer(){
        IPlayer player1 = new Player(1, "tester1");
        IPlayer player2 = new Player(2, "tester2");

        player1.setTeam(4);
        player2.setTeam(3);

        IGame game = new Game();

        game.addPlayer(1,player1);
        game.addPlayer(2, player2);

        game.setVictoryPlayerId(1);
        int winnerID = game.getVictoryPlayerId();

        Assert.assertEquals(winnerID, 1);
    }

    @Test
    public void testVictoryTeam(){
        IPlayer player1 = new Player(1, "tester1");
        IPlayer player2 = new Player(2, "tester2");

        player1.setTeam(4);
        player2.setTeam(3);

        IGame game = new Game();

        game.addPlayer(1,player1);
        game.addPlayer(2, player2);

        game.setVictoryTeam(3);
        int winnerTeamID = game.getVictoryTeam();

        Assert.assertEquals(winnerTeamID, 3);
    }
}
