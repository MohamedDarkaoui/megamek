package megamek.server;

import megamek.common.IPlayer;
import megamek.common.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *  The RatingManager is responsible for keeping and maintaining the rating for each player.
 *  It operates under the assumption that each player has a unique username.
 *  While the current version of the game does not enforce signing in before playing,
 *  this class is designed with the assumption that it will be supported in the future.
 */
@XmlRootElement
public class RatingManager {
    private final Map<String, Integer> playerRatings; // stores the name (instead of connID) and the rating
    @XmlTransient
    public static final Integer DEFAULT_PLAYER_RATING = 300;

    @XmlTransient
    public static final String FILENAME = "mmconf/playerRatings.xml";

    public RatingManager() {
        playerRatings = new HashMap<>();
    }

    @XmlElement
    public Map<String, Integer> getPlayerRatings() {
        return playerRatings;
    }

    /**
     * Adds the name of a new player and the default player rating to the playerRatings map.
     * @param player the new player
     */
    public void addNewPlayer(IPlayer player) {
        String name = player.getName();
        if (!playerRatings.containsKey(name)){
            playerRatings.put(name, DEFAULT_PLAYER_RATING);
            player.setRating(DEFAULT_PLAYER_RATING);
        }
    }

    /**
     * Fetches the rating of the player with the given name.
     * If the player's rating isn't found, returns the default rating.
     * @param playerName The name of the player for whom the rating is being requested
     * @return The rating of the player with the specified name, or the default rating.
     */
    public int getPlayerRating(String playerName) {
        Integer rating = playerRatings.get(playerName);
        if (rating != null){
            return rating;
        }
        return DEFAULT_PLAYER_RATING;
    }

    /**
     * This function updates the ratings of each player after victory has been established.
     * For now, it picks a random number between 8 and 12 and adds/subtracts it from the current rating.
     * This serves as a simple placeholder and may be replaced with a more complex ELO rating algorithm in the future.
     * @param player The player.
     * @param won Indicates whether the player won or lost the game.
     */
    public void updatePlayerRating(IPlayer player, boolean won) {
        String name = player.getName();
        Integer currentRating = playerRatings.get(name);
        if (currentRating != null) {
            int ratingChange = new Random().nextInt(5) + 8;  // Random number in [8, 12]
            int newRating = won ? currentRating + ratingChange : Math.max(0, currentRating - ratingChange);
            playerRatings.put(name, newRating);
            player.setRating(newRating);
        }
    }

    /**
     * Updates the rating of all winning and losing players.
     * @param wonPlayers the winning players.
     * @param lostPlayers the losing players.
     */
    public void updatePlayersRating(List<IPlayer> wonPlayers, List<IPlayer> lostPlayers) {
        for (IPlayer player : wonPlayers){
            updatePlayerRating(player, true);
        }
        for (IPlayer player : lostPlayers){
            updatePlayerRating(player, false);
        }
    }

    /**
     * Persists the current state of the RatingManager object to an XML file.
     */
    public void save(String filename) {
        try {
            JAXBContext context = JAXBContext.newInstance(RatingManager.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(this, new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * the state of a RatingManager object from an XML file created in a previous session.
     * If the file does not exist, it returns a new RatingManager object.
     * If an error occurs during the unmarshalling process, it returns null.
     * @return The restored RatingManager object, a new RatingManager object or null
     */
    public static RatingManager load(String filename) {
        File file = new File(filename);
        if (!file.exists()){
            return new RatingManager();
        }
        try {
            JAXBContext context = JAXBContext.newInstance(RatingManager.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (RatingManager) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
