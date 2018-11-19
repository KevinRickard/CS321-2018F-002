
import java.time.Instant;

/**
 * @author Thomas Washington
 * @author Kevin Rickard
 */

/**
 * Basic NPC, which moves on its own.
 */
abstract class NPC {

  private String name;
  private int currentRoomId;
  private int pastRoomId;
  private long lastAiTime;
  private long aiPeriodSeconds;
  protected final GameCore gameCore;

  // NPC constructor
  public NPC(GameCore gameCore, String name, int roomId, long aiPeriodSeconds) {
    this.name = name;
    this.currentRoomId = roomId;
    this.pastRoomId = 0;
    resetLastAiTime();
    this.aiPeriodSeconds = aiPeriodSeconds;
    this.gameCore = gameCore;
  }

  /**
   * @return current time
   */
  protected long getCurrentTime() {
      return Instant.now().getEpochSecond();
  }

  // Setter for the last action taken by the NPC
  protected void resetLastAiTime() {
      lastAiTime = getCurrentTime();
  }

  /**
   * @return the name of the NPC
   */
  public String getName(){
    return this.name;
  }
  
  /**
   * @return the current room ID the NPC is in
   */
  public int getCurrentRoomId(){
    return this.currentRoomId;
  }

  /**
   * @return the current room the NPC is in
   */
  public Room getCurrentRoom() {
      return gameCore.getMap().findRoom(currentRoomId);
  }
  
  /**
   * @return the last room this NPC was in.
   * If this is the first room the NPC has been in, returns 0.
   */
  public int getPastRoomId(){
    return this.pastRoomId;
  }

  // Current room setter
  protected void setCurrentRoomId(int newRoomId){
    synchronized (this) {
      pastRoomId = currentRoomId;
      currentRoomId = newRoomId;
    }
  }

  /**
   * @return how long its been since last action
   */
  protected long getLastAiTime() {
      return lastAiTime;
  }

  /**
   * @return the longest length an NPC will take before an action
   */
  protected long getAiPeriodSeconds() {
      return aiPeriodSeconds;
  }
  
  /**
   * @return the NPC name as a String
   */
  @Override
  public String toString() {
    return this.getName();
  }
  // -----------------------------------------

   /**
   * Allows the NPC to traverse randomly around the map
   */
  protected void moveRandomly() {
    synchronized (this) {
      Exit exit = getCurrentRoom().getRandomValidExit(getPastRoomId());
      if (exit == null) {
        System.err.println("Resetting " + getName() + " to its previous room");
        getCurrentRoom().broadcast(name + " teleported away using hax");
        setCurrentRoomId(getPastRoomId());
      } else {
        getCurrentRoom().broadcast(name + " walked off to the " + exit.getDirection());
        setCurrentRoomId(exit.getRoom());
      }
      getCurrentRoom().broadcast(name + " walked into the area");
    }
  }
  
  /**
   * @return if an NPC action was successful
   * Controller of the NPC and actions it can take
   */
  public boolean tryAi() {
    synchronized (this) {
        final long secondsSinceLastAi = getCurrentTime() - getLastAiTime();
        if (secondsSinceLastAi > getAiPeriodSeconds()) {
            doAi();
            resetLastAiTime();
            return true;
        } else
            return false;
    }
  }
  
  /**
   * All of the methods NPC may use should go here
   * but only methods that support the game actions
   * not internals
   */
  protected void doAi() {
    synchronized (this) {
        moveRandomly();
    }
  }
} //EOF NPC