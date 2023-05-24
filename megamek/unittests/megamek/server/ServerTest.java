package megamek.server;
import megamek.common.Game;
import megamek.common.IGame;
import megamek.common.IPlayer;
import megamek.common.Player;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;

@RunWith(JUnit4.class)
public class ServerTest {

    @Test
    public void testGetPlayer() throws IOException {
        Server server = new Server("password", 483);
        IGame game = new Game();
        IPlayer player1 = new Player(1, "tester1");
        game.addPlayer(player1.getId(), player1);
        server.setGame(game);
        Assert.assertEquals(server.getPlayer(1), player1);
    }
}
