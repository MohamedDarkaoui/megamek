package megamek.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import megamek.common.Player;

@RunWith(JUnit4.class)
public class PlayerTest {

    @Test
    public void testPlayerInitialization(){
        IPlayer player = new Player(501, "tester");
        Assert.assertEquals(player.getName(),"tester" );
        Assert.assertEquals(player.getId(), 501);
    }

    @Test
    public void testPlayerUpdate(){
        IPlayer player = new Player(501, "tester");
        player.setName("admin");
        Assert.assertEquals(player.getName(), "admin");
    }

    @Test
    public void testPlayerEquals(){
        IPlayer player1 = new Player(501, "tester");
        IPlayer player2 = new Player(500, "tester");    // different id, same name
        IPlayer player3 = new Player(501, "engineer");  // same id, different name

        Assert.assertNotEquals(player1, player2);
        Assert.assertEquals(player1, player3);
    }
}
