package megamek.server.victory;

import megamek.common.*;
import megamek.common.options.GameOptions;
import megamek.common.options.OptionsConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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

        GameOptions gameOptions = game.getOptions();

        Victory victory = new Victory(gameOptions);
        VictoryResult vr = victory.checkForVictory(game, game.getVictoryContext());

        Assert.assertFalse(vr.victory());

        // changes to gameOptions to enable checking for optional victory
        gameOptions.getOption(OptionsConstants.VICTORY_USE_BV_RATIO).setValue(true);

        victory = new Victory(gameOptions);
        vr = victory.checkForVictory(game, game.getVictoryContext());

        Assert.assertTrue(vr.victory());
        gameOptions.getOption(OptionsConstants.VICTORY_ACHIEVE_CONDITIONS).setValue(2);

        victory =  new Victory(gameOptions);
        vr = victory.checkForVictory(game, game.getVictoryContext());
        Assert.assertFalse(vr.victory());
    }
}
