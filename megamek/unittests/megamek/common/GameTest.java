package megamek.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class GameTest {

    private IGame createGame(int nrPlayers, int... teams){
        IGame game = new Game();
        for (int i = 1; i <= nrPlayers; i++){
            IPlayer player = new Player(i, "tester" + i);
            if (teams.length > 0){
                player.setTeam(teams[i-1]);
            }
            game.addPlayer(player.getId(), player);
        }
        return game;
    }
    @Test
    public void testAddPlayer(){
        IGame game = createGame(3, 5,4,4);

        Assert.assertEquals(game.getPlayer(1).getId(), 1);
        Assert.assertEquals(game.getPlayer(2).getId(), 2);
        Assert.assertEquals(game.getPlayer(3).getId(), 3);
    }

    @Test
    public void testRemovePlayer(){
        IGame game = createGame(2);

        Assert.assertEquals(game.getNoOfPlayers(), 2);

        game.removePlayer(2);
        Assert.assertEquals(game.getNoOfPlayers(), 1);

        game.removePlayer(1);
        Assert.assertEquals(game.getNoOfPlayers(), 0);

        game.removePlayer(1);
        Assert.assertEquals(game.getNoOfPlayers(), 0);

    }

    @Test
    public void testVictoryPlayer(){
        IGame game = createGame(2, 3,4);

        game.setVictoryPlayerId(1);
        int winnerID = game.getVictoryPlayerId();

        Assert.assertEquals(winnerID, 1);
    }

    @Test
    public void testVictoryTeam(){
        IGame game = createGame(2, 4, 3);

        game.setVictoryTeamId(3);
        int winnerTeamID = game.getVictoryTeamId();

        Assert.assertEquals(winnerTeamID, 3);
    }

    @Test
    public void testSetupTeams(){
        IGame game = createGame(5, 3,4,5,IPlayer.TEAM_NONE,IPlayer.TEAM_UNASSIGNED);

        Assert.assertEquals(game.getTeamForPlayer(game.getPlayer(1)).getId(), 3);
        Assert.assertEquals(game.getTeamForPlayer(game.getPlayer(2)).getId(), 4);
        Assert.assertEquals(game.getTeamForPlayer(game.getPlayer(3)).getId(), 5);
        Assert.assertEquals(game.getTeamForPlayer(game.getPlayer(4)).getId(), IPlayer.TEAM_NONE);
        Assert.assertNull(game.getTeamForPlayer(game.getPlayer(5)));
        Assert.assertEquals(game.getTeamsVector().size(), 4);

        // no changes
        game.setupTeams();
        Assert.assertEquals(game.getTeamsVector().size(), 4);

        // change the team of one player
        game.getPlayer(3).setTeam(3);
        Assert.assertEquals(game.getTeamsVector().size(), 4);   // before calling setupTeams
        game.setupTeams();
        Assert.assertEquals(game.getTeamsVector().size(), 3);   // after cleanup, only 3 teams left
        Assert.assertEquals(game.getTeamForPlayer(game.getPlayer(3)).getId(), 3);

        // At this point the teams left are  {team IPlayer.TEAM_NONE, team 3, team 4}

        game.getPlayer(2).setTeam(IPlayer.TEAM_UNASSIGNED);
        Assert.assertEquals(game.getTeamsVector().size(), 3);
        game.setupTeams();
        Assert.assertEquals(game.getTeamsVector().size(), 2);
        Assert.assertNull(game.getTeamForPlayer(game.getPlayer(2)));
    }

    @Test
    public void testGetAllWinningPlayers() {
        IGame game = createGame(2);

        // game without teams
        game.setVictoryPlayerId(1);
        List<IPlayer> winners = game.getAllWinningPlayers();
        Assert.assertEquals(winners.size(), 1);
        Assert.assertEquals(winners.get(0).getId(), 1);

        // game with teams
        game = createGame(4, 1,1,2,2);  // team1 : {player1, player2}, team2 : {player3, player4}
        game.setVictoryTeamId(2);
        game.setVictoryPlayerId(1);
        winners = game.getAllWinningPlayers();
        Assert.assertEquals(winners.size(), 2);
        Assert.assertTrue(winners.contains(new Player(3, "tester3")));
        Assert.assertTrue(winners.contains(new Player(4, "tester4")));

        // no winners
        game.cancelVictory();
        Assert.assertEquals(game.getAllWinningPlayers().size(), 0);
    }

    @Test
    public void testGetAllLosingPlayers(){
        IGame game = createGame(4);

        // no victory
        Assert.assertTrue(game.getAllLosingPlayers().isEmpty());

        // game without teams
        game.setVictoryPlayerId(2);
        List<IPlayer> losers =  game.getAllLosingPlayers();
        Assert.assertEquals(3, losers.size());
        Assert.assertTrue(losers.contains(new Player(1, "tester1")));
        Assert.assertTrue(losers.contains(new Player(3, "tester2")));
        Assert.assertTrue(losers.contains(new Player(4, "tester4")));

        // game with teams
        game = createGame(8, 1,1,1,1,2,2,3,3);
        game.setVictoryTeamId(3);
        losers = game.getAllLosingPlayers();
        Assert.assertEquals(6, losers.size());
        Assert.assertTrue(losers.contains(new Player(1, "tester1")));
        Assert.assertFalse(losers.contains(new Player(7, "tester7")));
    }
}
