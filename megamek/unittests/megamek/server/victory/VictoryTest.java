package megamek.server.victory;

import megamek.common.*;
import megamek.common.options.GameOptions;
import megamek.common.options.OptionsConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

@RunWith(JUnit4.class)
public class VictoryTest {

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

    @Test
    public void testOptionalVictory(){
        IPlayer player1 = new Player(1, "tester1");
        IPlayer player2 = new Player(2, "tester2");
        player1.setTeam(1);
        player2.setTeam(2);

        Vector<IPlayer> players = new Vector<>(Arrays.asList(player1, player2));
        Entity gun = new GunEmplacement();
        Entity squadron = new FighterSquadron();
        gun.setOwner(player1);
        squadron.setOwner(player2);

        List<Entity> entities = Arrays.asList(gun, squadron);

        // --- Mock game behaviour
        IGame mockGame = Mockito.mock(Game.class);
        player1.setGame(mockGame);
        player2.setGame(mockGame);
        Mockito.when(mockGame.getPlayer(1)).thenReturn(player1);
        Mockito.when(mockGame.getPlayer(2)).thenReturn(player2);
        Mockito.when(mockGame.getOptions()).thenReturn(new GameOptions());
        Mockito.when(mockGame.getEntities()).thenReturn(entities.iterator());
        Mockito.when(mockGame.getEntitiesVector()).thenReturn(entities);
        Mockito.when(mockGame.getVictoryContext()).thenReturn(null);
        Mockito.when(mockGame.getPlayersVector()).thenReturn(players);
        Mockito.when(mockGame.getLiveDeployedEntitiesOwnedBy(player1)).thenReturn(5);
        Mockito.when(mockGame.getLiveDeployedEntitiesOwnedBy(player2)).thenReturn(5);
        // ---

        // Normal conditions where all players have at least one entity
        Victory victory = new Victory(mockGame.getOptions());
        VictoryResult vr = victory.checkForVictory(mockGame, mockGame.getVictoryContext());

        Assert.assertFalse(vr.victory());

        // Enable optional victory by also looking at BV ratio
        GameOptions gameOptions = mockGame.getOptions();
        gameOptions.getOption(OptionsConstants.VICTORY_USE_BV_RATIO).setValue(true);

        victory = new Victory(gameOptions);
        vr = victory.checkForVictory(mockGame, mockGame.getVictoryContext());
        Assert.assertTrue(vr.victory());

        // make the threshold for achieving victory higher => no more victory by default
        mockGame.getOptions().getOption(OptionsConstants.VICTORY_ACHIEVE_CONDITIONS).setValue(2);

        victory =  new Victory(mockGame.getOptions());
        vr = victory.checkForVictory(mockGame, mockGame.getVictoryContext());
        Assert.assertFalse(vr.victory());

        // force a draw by expiring the game time
        mockGame.getOptions().getOption(OptionsConstants.VICTORY_ACHIEVE_CONDITIONS).setValue(1);
        Mockito.when(mockGame.gameTimerIsExpired()).thenReturn(true);

        victory =  new Victory(mockGame.getOptions());
        vr = victory.checkForVictory(mockGame, mockGame.getVictoryContext());
        Assert.assertTrue(vr.isDraw());
        Assert.assertTrue(vr.victory());
    }
}
