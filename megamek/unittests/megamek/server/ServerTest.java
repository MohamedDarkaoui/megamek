package megamek.server;
import megamek.common.*;
import megamek.common.options.GameOptions;
import megamek.common.options.OptionsConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;

@RunWith(JUnit4.class)
public class ServerTest {

    private Game prepareGame(boolean isTeam){
        IPlayer victor = new Player(5, "Victor");
        IPlayer defeated = new Player(6, "Defeated");
        if (isTeam){
            victor.setTeam(1);
            defeated.setTeam(2);
        }

        Game game = new Game();

        Entity gun = new GunEmplacement();
        Entity squadron = new FighterSquadron();
        gun.setOwner(victor);
        squadron.setOwner(defeated);
        squadron.setId(101);
        game.addEntity(gun);
        game.addEntity(squadron);
        game.createVictoryConditions();
        game.addPlayer(victor.getId(), victor);
        game.addPlayer(victor.getId(), defeated);

        GameOptions gameOptions = new GameOptions();
        gameOptions.getOption(OptionsConstants.VICTORY_ACHIEVE_CONDITIONS).setValue(100);
        game.setOptions(gameOptions);
        return game;
    }

    @Test
    public void testGetPlayer() throws IOException {
        Server server = new Server("password", 483);
        IGame game = new Game();
        IPlayer player1 = new Player(1, "tester1");
        game.addPlayer(player1.getId(), player1);
        server.setGame(game);
        Assert.assertEquals(server.getPlayer(1), player1);
    }

    @Test
    public void testVictoryNoTeam() throws IOException {

        Game spyGame = Mockito.spy(prepareGame(false));
        IPlayer victor = spyGame.getPlayer(5);
        Entity squadron = spyGame.getEntity(101);

        Server server = new Server("password", 386);
        server.setGame(spyGame);

        // assert no victory when victory conditions have not meat
        int victorID = server.getGame().getVictoryPlayerId();
        Assert.assertNotEquals(victorID, victor.getId());
        Assert.assertFalse(server.victory());

        // force victory and check assert again
        server.forceVictory(victor);
        victorID = server.getGame().getVictoryPlayerId();
        Assert.assertEquals(victorID, victor.getId());
        Mockito.verify(spyGame).setForceVictory(true);
        // victory only returns true only if VictoryResult says so, forcing a victory doesn't help
        Assert.assertFalse(server.victory());
        Mockito.verify(spyGame).setForceVictory(false); // the victory method reverses the force victory

        // destroy defeated player's weapon to make victor the winner
        squadron.setDestroyed(true);
        Assert.assertTrue(server.victory());
    }

    @Test
    public void testVictoryWithTeam() throws IOException {
        Game spyGame = Mockito.spy(prepareGame(true));
        IPlayer victor = spyGame.getPlayer(5);
        Entity squadron = spyGame.getEntity(101);

        Server server = new Server("password", 322);
        server.setGame(spyGame);

        server.forceVictory(victor);
        Assert.assertNotEquals(server.getGame().getVictoryPlayerId(), victor.getId());
        Assert.assertEquals(server.getGame().getVictoryTeam(), victor.getTeam());
        Assert.assertFalse(server.victory());
        Mockito.verify(spyGame).setForceVictory(false);

        squadron.setDestroyed(true);
        Assert.assertTrue(server.victory());
    }

    @Test
    public void testCancelVictory() throws IOException {
        Game game = prepareGame(false);
        game.setVictoryPlayerId(5);
        Server server = new Server("password", 321);
        server.setGame(game);

        // make player with id 5 win
        game.setVictoryPlayerId(5);
        Assert.assertEquals(game.getVictoryPlayerId(), 5);
        server.cancelVictory();
        Assert.assertEquals(game.getVictoryPlayerId(), IPlayer.PLAYER_NONE);

        // make team 1 win
        game = prepareGame(true);
        server.setGame(game);
        game.setVictoryTeam(1);
        Assert.assertEquals(game.getVictoryTeam(), 1);
        server.cancelVictory();
        Assert.assertEquals(game.getVictoryTeam(), IPlayer.TEAM_NONE);

        // force victory
        server.forceVictory(game.getPlayer(5));
        Assert.assertTrue(game.isForceVictory());
        server.cancelVictory();
        Assert.assertFalse(game.isForceVictory());
    }
}
