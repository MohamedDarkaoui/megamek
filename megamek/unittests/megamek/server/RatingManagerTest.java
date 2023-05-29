package megamek.server;

import megamek.common.Game;
import megamek.common.IPlayer;
import megamek.common.Player;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;


@RunWith(JUnit4.class)
public class RatingManagerTest {
    @Test
    public void testUpdate(){
        IPlayer victor = new Player(1, "tester1");
        IPlayer defeated = new Player(2, "tester2");
        Game game = new Game();
        game.addPlayer(1, victor);
        game.addPlayer(2, defeated);

        RatingManager rm = new RatingManager();
        rm.addNewPlayer(victor);
        rm.addNewPlayer(defeated);
        Assert.assertEquals((int) RatingManager.DEFAULT_PLAYER_RATING, rm.getPlayerRating(victor.getName()));
        Assert.assertEquals((int) RatingManager.DEFAULT_PLAYER_RATING, rm.getPlayerRating(defeated.getName()));
        game.setVictoryPlayerId(1);
        rm.updatePlayersRating(game.getAllWinningPlayers(), game.getAllLosingPlayers());
        Assert.assertTrue(rm.getPlayerRating(victor.getName()) > RatingManager.DEFAULT_PLAYER_RATING);
        Assert.assertTrue(rm.getPlayerRating(defeated.getName()) < RatingManager.DEFAULT_PLAYER_RATING);
    }
    @Test
    public void testRatingPersistence(){
        IPlayer player1 = new Player(1, "tester1");
        RatingManager rm = new RatingManager();

        String filename = "mmconf/playerRatingsTest.xml";
        rm.addNewPlayer(player1);
        rm.updatePlayerRating(player1, true);
        rm.save(filename);

        RatingManager rm2 = RatingManager.load(filename);

        Assert.assertNotNull(rm2);
        Assert.assertTrue(rm2.getPlayerRating(player1.getName()) > 300);

        // delete created file
        File file = new File(filename);
        assert(file.delete());
    }
}