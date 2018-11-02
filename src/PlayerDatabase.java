import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.lang.StringBuilder;

import javax.annotation.processing.FilerException;

/**
 * This class will handle all player database interactions
 
 * NOTE: All methods in this class are static, do not attempt to
 *       create an instance of this class
 *
 * Authors: Cody Kidwell and Joseph Saah
 */
public class PlayerDatabase {

   /*
    * File name of player database 
    */
   public static final String DATABASE_FILE = "player_database.csv";
   public static final String LOG_FILE = "login_logout_log.txt";

   /**
    * Adds a player's username and password to the database
    * if the username does not already exist in the system
    *
    * @param name Player's username
    * @param password Player's password
    * @return true if player is added to database, false otherwise 
    */
	public static boolean addPlayer(String name, String password){
   
      //build comma-seperated inputs for database with user's name and password
      try(FileOutputStream fos = new FileOutputStream(DATABASE_FILE, true)) {
         StringBuilder sb = new StringBuilder(name);
         
         sb.append(",");
         sb.append(password);
         sb.append("\n");
         
         //write inputs to database
         fos.write(sb.toString().getBytes());
      }
      catch(IOException e) {
         e.printStackTrace();
         return false;
      }

		return true;
	}
	
   /**
    * Checks if username already exists in the database
    *
    * @param name The username to check for in the database
    * @return true if username exists, false otherwise
    */
	public static boolean isPlayer(String name) {
      try(FileInputStream fis = new FileInputStream(DATABASE_FILE);
          InputStreamReader isr = new InputStreamReader(fis);
          BufferedReader br = new BufferedReader(isr)) {
          
          //reads database line by line
          String line;
			 while((line = br.readLine()) != null) {
             //stores username and password from current line into an array
				 String[] info = line.split(",");
             
             //checks if the username on this line is equal to the given username
				 if(info[0].equals(name)) {
                    return true;
             }
			 }
		}
      catch(IOException e) {
         return false;
      }
      return false;
    }
    
    /**
     * Checks if the password for the given user matches the the password put in
     * @param name
     * @param password
     * @return true if the passwords match
     */
    public static boolean isPassword(String name, String password){
        try(FileInputStream fis = new FileInputStream(DATABASE_FILE);
          InputStreamReader isr = new InputStreamReader(fis);
          BufferedReader br = new BufferedReader(isr)) {
          
          //reads database line by line
          String line;
			 while((line = br.readLine()) != null) {
             //stores username and password from current line into an array
				 String[] info = line.split(",");
             
             //checks if the username on this line is equal to the given username
			if(info[0].equals(name)) {
                if(info[1].equals(password))
                    return true;
             }
			 }
		}catch(IOException e) {
         return false;
      }
      return false;
    }

	/**
	 * Removes a player's username and password from the database if the username
	 * does already exist in the system
	 * 
	 * @param name
	 *            The username to be removed
	 * @return true if username has been removed, false if otherwise
	 */
	public static boolean removePlayer(String name) {
		// first checks if player exists
		if (!isPlayer(name)) {
			System.out.println(name + " does not exist in database.");
			return false;
		}
		// removal process here
		else {
			String line;
			StringBuilder lines = new StringBuilder();
			try (FileInputStream fis = new FileInputStream(DATABASE_FILE);
					InputStreamReader isr = new InputStreamReader(fis);
					BufferedReader br = new BufferedReader(isr)) {

				// reads database line by line adding lines in to a collective string of all the lines
				while ((line = br.readLine()) != null) {
					
					String[] info = line.split(",");

					// if username does not match to the one read it will rewrite it to the file
					if (!info[0].equals(name)) lines.append(line + "\n");
				
				}
			} catch (IOException e) {
				return false;
			}
			try (FileOutputStream fos = new FileOutputStream(DATABASE_FILE)){
				
				//overwrite the old file
				fos.write(lines.toString().getBytes());
				return true;
			
			} catch (FilerException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
    }

    /**
    * Writes to a log file everytime a player logs in/out
    *
    * @param name Player name
    * @param isLoggingIn Whether the player is logging in or logging out
    * @return true if log message is written successfully, false otherwise
    */
   public static boolean loginLog(String name, boolean isLoggingIn) {
    String log = name;
    
    try(FileOutputStream fos = new FileOutputStream(LOG_FILE, true)) {
       if(isLoggingIn)
          log += " logged in.\n";
       else
          log += " logged out.\n"; 
       
       //write log message to log file
       fos.write(log.getBytes());
    }
    catch(IOException e) {
       e.printStackTrace();
       return false;
    }
    
    return true;
 }
}
