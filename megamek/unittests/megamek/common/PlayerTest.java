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
        IPlayer Player = new Player(501, "tester");
        Assert.assertEquals(Player.getName(),"tester" );
        Assert.assertEquals(Player.getId(), 501);
    }

    @Test
    public void PlayerUpdate(){
        IPlayer Player = new Player(501, "tester");
        Player.setName("admin");
        Assert.assertEquals(Player.getName(), "admin");
    }
}
