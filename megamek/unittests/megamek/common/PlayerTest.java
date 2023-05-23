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
        IPlayer testPlayer = new Player(501, "tester");
        Assert.assertEquals(testPlayer.getName(),"tester" );
        Assert.assertEquals(testPlayer.getId(), 501);
    }

    @Test
    public void testPlayerUpdate(){
        IPlayer testPlayer = new Player(501, "tester");
        testPlayer.setName("admin");
        Assert.assertEquals(testPlayer.getName(), "admin");
    }
}
