
 /**
 * @author Thaovy
 * The Spirit class extends from NPC, they are roamers
 * which can be caught by players.
 */
public class Spirit extends NPC {
    
    Spirits spiritType;

    // Spirit constructor
    Spirit(GameCore gameCore, String name, int currentRoom, long aiPeriodSeconds, Spirits spiritType) {
        super(gameCore, name, currentRoom, aiPeriodSeconds);
        this.spiritType = spiritType;
    }
}