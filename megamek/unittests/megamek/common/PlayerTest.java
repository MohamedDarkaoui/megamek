package megamek.common;

import megamek.client.ui.swing.util.PlayerColour;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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

    @Test
    public void testGetColourString(){
        IPlayer player1 = new Player(1, "tester");
        player1.setColour(PlayerColour.YELLOW);
        Assert.assertTrue(player1.getColourString().contains(PlayerColour.YELLOW.getHexString(0x00F0F0F0)));
        player1.setColour(PlayerColour.CYAN);
        Assert.assertFalse(player1.getColourString().contains(PlayerColour.YELLOW.getHexString(0x00F0F0F0)));
        Assert.assertTrue(player1.getColourString().contains(PlayerColour.CYAN.getHexString(0x00F0F0F0)));

        IPlayer player2 = new Player(2, "tester2");
        Assert.assertTrue(player2.getColourString().contains(PlayerColour.BLUE.getHexString(0x00F0F0F0)));
    }
}
