
package stellarclicker.levelgen.util;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**========================================================================================================================== 
 * @file JSONCreator.java
 * --------------------------------------------------------------------------------------------------------------------------
 * @author Angela Gross
 * --------------------------------------------------------------------------------------------------------------------------
 * @description This creates the JSON file that will be used to level up the components after loading a game
 *///========================================================================================================================

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class JSONCreator
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // --------------------------------------------------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // --------------------------------------------------------------------------------------------------------------------------------------------
    
    private String outputPath;
    private final String LEVEL = "LEVEL";
    private final String TIME_ELAPSED = "TIME_ELAPSED";
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // --------------------------------------------------------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------------------------------------------------------------------------------------------------
    
    public JSONCreator(String outputPath)
    {
        this.outputPath = outputPath;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**========================================================================================================================== 
    * @name GENERATE LEVEL FILE
    * 
    * @description Creates the JSON file for the component given the min level, max level, and base experience time
    * 
    * @param baseExpTime The base experience time for the component 
    * @param minLevel The minimum level for the component
    * @param maxLevel The maximum level for the component
    * @param fileName The name of the JSON file
    *///=========================================================================================================================
    public void generateLevelFile(int baseExpTime, int minLevel, int maxLevel, String fileName)
    {
        String fullPath = outputPath + "/" + fileName;
        JSONObject levelObj = buildJSONObject(baseExpTime, minLevel, maxLevel);
        createJSONFile(levelObj, fullPath);
    }
    
    /**========================================================================================================================== 
    * @name BUILD JSON OBJECT
    * 
    * @description Build the JSON object that stores all of the level and time information for the component
    * 
    * @param baseExpTime The base experience time for the component 
    * @param minLevel The minimum level for the component
    * @param maxLevel The maximum level for the component
    * 
    * @return JSONObject The newly generated JSON object containing all level information
    *///=========================================================================================================================
    private JSONObject buildJSONObject(int baseExpTime, int minLevel, int maxLevel)
    {   
        // root object
        JSONObject obj = new JSONObject();
        // child object
        JSONObject level;
        
        // add in information for all other levels
        for(int iLevel = minLevel; iLevel <= maxLevel; iLevel++)
        {
            level = new JSONObject();
            level.put(LEVEL, iLevel); // Level of component
            level.put(TIME_ELAPSED, getTimeTaken(iLevel, baseExpTime) ); // Time it takes to level up component
            
            obj.put(iLevel, level);
        }
        
        return obj;
    }
    
    /**========================================================================================================================== 
    * @name CREATE JSON FILE
    * 
    * @description Returns the name of the picture based on its tier
    * 
    * @param obj The JSON object to save to file
    * @param fullPath The fully qualified path to save the JSON file
    *///=========================================================================================================================
    private void createJSONFile(JSONObject obj, String fullPath)
    {
        try (FileWriter file = new FileWriter(fullPath)) 
        {
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File: " + fullPath);
        }
        catch(Exception ex)
        {
            Logger.getLogger(JSONCreator.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }
    
    /**========================================================================================================================== 
    * @name GET TIME TAKEN
    * 
    * @description Returns the amount of time it will take to level up the component
    * 
    * @param level The current level of the component
    * @param baseExpTime The base experience time for the component 
    * 
    * @return float The time taken to level it up
    *///=========================================================================================================================
    private float getTimeTaken(int level, int baseExpTime)
    {
        float timeTaken = (float) baseExpTime / ( ((float)level/10.00f) + 1.00f);
        
        return timeTaken;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
