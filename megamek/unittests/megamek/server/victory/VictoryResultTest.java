package megamek.server.victory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VictoryResultTest {

    @Test
    public void  testPlayerVictoryResult(){
        VictoryResult vr = new VictoryResult(false);

        vr.addPlayerScore(1,5);
        vr.addPlayerScore(2,6);

        Assert.assertEquals(vr.getWinningPlayer(), 2);
        Assert.assertTrue(vr.isWinningPlayer(2));
        Assert.assertFalse(vr.isDraw());

        vr.addPlayerScore(3,6);
        Assert.assertTrue(vr.isDraw());
    }

    @Test
    public void testTeamVictoryResult(){
        VictoryResult vr = new VictoryResult(false);

        vr.addTeamScore(1, 2);
        vr.addTeamScore(2, 0);

        Assert.assertEquals(vr.getWinningTeam(), 1);
        Assert.assertTrue(vr.isWinningTeam(1));
        Assert.assertFalse(vr.isDraw());

        vr.addTeamScore(3, 0);
        Assert.assertFalse(vr.isDraw());
        vr.addTeamScore(4,2);
        Assert.assertTrue(vr.isDraw());
    }

    @Test
    public void testVictory(){
        VictoryResult vr = new VictoryResult(false);
        vr.addTeamScore(1, 2);
        vr.addTeamScore(2, 1);

        vr.setVictory(true);
        Assert.assertTrue(vr.victory());
        Assert.assertTrue(vr.isWinningPlayer(1));
    }
}
