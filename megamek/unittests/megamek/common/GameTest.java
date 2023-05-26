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
        IPlayer Player1 = new Player(1, "tester1");
        IPlayer Player2 = new Player(2, "tester2");
        IPlayer Player3 = new Player(2, "tester3");

        Player1.setTeam(5);
        Player2.setTeam(4);
        Player3.setTeam(4);

        IGame game = new Game();

        game.addPlayer(1, Player1);
        game.addPlayer(2, Player2);
        game.addPlayer(3, Player3);

        Assert.assertEquals(game.getPlayer(1), Player1);
        Assert.assertEquals(game.getPlayer(2), Player2);
        Assert.assertEquals(game.getPlayer(3), Player3);

        List<Team> teams = game.getTeamsVector();

        Assert.assertEquals(teams.size(), 2);
        Assert.assertEquals(game.getTeamForPlayer(Player1).getId(), 5);
        Assert.assertEquals(game.getTeamForPlayer(Player2).getId(), 4);
        Assert.assertEquals(game.getTeamForPlayer(Player2).getId(), 4);
    }

    @Test
    public void testVictoryPlayer(){
        IPlayer Player1 = new Player(1, "tester1");
        IPlayer Player2 = new Player(2, "tester2");

        Player1.setTeam(4);
        Player2.setTeam(3);

        IGame game = new Game();

        game.addPlayer(1,Player1);
        game.addPlayer(2, Player2);

        game.setVictoryPlayerId(1);
        int winnerID = game.getVictoryPlayerId();

        Assert.assertEquals(winnerID, 1);
    }

    @Test
    public void testVictoryTeam(){
        IPlayer Player1 = new Player(1, "tester1");
        IPlayer Player2 = new Player(2, "tester2");

        Player1.setTeam(4);
        Player2.setTeam(3);

        IGame game = new Game();

        game.addPlayer(1,Player1);
        game.addPlayer(2, Player2);

        game.setVictoryTeam(3);
        int winnerTeamID = game.getVictoryTeam();

        Assert.assertEquals(winnerTeamID, 3);
    }

    @Test
    public void testLastManStandingVictory(){
        IGame game = new Game();

        IPlayer player1 = new Player(1, "tester1");
        IPlayer player2 = new Player(2, "tester2");
        player1.setTeam(1);
        player2.setTeam(2);

        game.addPlayer(1, player1);
        game.addPlayer(2, player2);

        // we need to assign some entities to players in order to keep them "alive"
        Entity gun = new GunEmplacement();
        Entity squadron = new FighterSquadron();

        gun.setOwner(player1);
        squadron.setOwner(player2);
        game.addEntity(gun);
        game.addEntity(squadron);

        game.createVictoryConditions();
        Assert.assertFalse(game.getVictory().checkForVictory(game, game.getVictoryContext()).victory());

        // If we destroy player1's gun, which is their only entity,
        // player2 will be the last man standing, thus player2 wins
        gun.setDestroyed(true);
        Assert.assertTrue(game.getVictory().checkForVictory(game, game.getVictoryContext()).victory());

        gun.setDestroyed(false);    // give him back his gun
        Assert.assertFalse(game.getVictory().checkForVictory(game, game.getVictoryContext()).victory());
    }

    @Test
    public void testForcedVictory(){
        IGame game = new Game();

        IPlayer player1 = new Player(1, "tester1");
        IPlayer player2 = new Player(2, "tester2");
        player1.setTeam(1);
        player2.setTeam(2);

        game.addPlayer(1, player1);
        game.addPlayer(2, player2);

        Entity gun = new GunEmplacement();
        Entity squadron = new FighterSquadron();

        gun.setOwner(player1);
        squadron.setOwner(player2);
        game.addEntity(gun);
        game.addEntity(squadron);

        game.createVictoryConditions();
        Assert.assertFalse(game.getVictory().checkForVictory(game, game.getVictoryContext()).victory());

        game.setForceVictory(true);
        Assert.assertTrue(game.getVictory().checkForVictory(game, game.getVictoryContext()).victory());
        game.setForceVictory(false);
        Assert.assertFalse(game.getVictory().checkForVictory(game, game.getVictoryContext()).victory());
    }

}
