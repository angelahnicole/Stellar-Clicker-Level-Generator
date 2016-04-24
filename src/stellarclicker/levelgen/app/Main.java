
package stellarclicker.levelgen.app;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**========================================================================================================================== 
 * @file Main.java
 * --------------------------------------------------------------------------------------------------------------------------
 * @author Angela Gross
 * --------------------------------------------------------------------------------------------------------------------------
 * @description This program generates JSON files that contain the level and how much time it takes to level it given a 
 * components JSON file (in assets) that contain base exp time, min level, and max level for each component.
 *///========================================================================================================================

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import stellarclicker.levelgen.util.JSONCreator;
import stellarclicker.levelgen.util.JSONLoader;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Main
{
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private static final JSONLoader myJSONLoader = new JSONLoader();
    private static JSONCreator myJSONCreator = null;
    private static ClassLoader myClassLoader;
    private static String outputDir = "output";
    private static String fullOutputPath = "";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**========================================================================================================================== 
    * @name MAIN
    * 
    * @description The main application that creates the JSON files
    * 
    * @param args The passed in arguments to the JAR file
    *///=========================================================================================================================
    public static void main(String[] args)
    {
        // BUILD OUTPUT PATH
        // -------------------------------------------------------------------------------------------
        try
        {
            fullOutputPath = getJarContainingFolder(Main.class) + "/" + outputDir;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // INITIALIZE THE JSON CREATOR
        // -------------------------------------------------------------------------------------------
        myJSONCreator = new JSONCreator(fullOutputPath);
        
        
        // INITIALIZE THE CLASS LOADER
        // -------------------------------------------------------------------------------------------
        myClassLoader = Thread.currentThread().getContextClassLoader();
        if (myClassLoader == null) myClassLoader = Class.class.getClassLoader();
        
        
        // BUILD THE SHIP COMPONENTS
        // -------------------------------------------------------------------------------------------
        if(createOutputDirectory())
        {
            buildShipComponents();
        }
        else
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Unable to create output directory- program will now close.");
        }

    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**========================================================================================================================== 
    * @name CREATE OUTPUT DIRECTORY
    * 
    * @description Creates the output directory for the JSON files to reside in. Will reside next to the JAR file.
    * 
    * @return boolean Whether or not the directory was/is created
    *///=========================================================================================================================
    private static boolean createOutputDirectory()
    {
        boolean directoryCreated = false;
        File outputDir = null;
        
        try
        {
            outputDir = new File(fullOutputPath);
            outputDir.mkdirs();
        } 
        catch(Exception ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex); 
        } 
        
        if(outputDir != null && outputDir.exists())
        {
            directoryCreated = true;
        }
        
        return directoryCreated;
    }
    
    /**========================================================================================================================== 
    * @name GET JAR CONTAINING FOLDER
    * 
    * @description Returns a path to the jar
    * 
    * @param aclass The class that we want the path of
    * 
    * @return String Path to the jar
    * 
    * @throws Exception
    *///=========================================================================================================================
    public static String getJarContainingFolder(Class aclass) throws Exception 
    {
        CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

        File jarFile;

        if (codeSource.getLocation() != null) 
        {
            jarFile = new File(codeSource.getLocation().toURI());
        }
        else 
        {
            String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
            String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
            jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
            jarFile = new File(jarFilePath);
        }
        
        return jarFile.getParentFile().getAbsolutePath();
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**========================================================================================================================== 
    * @name BUILD SHIP COMPONENTS
    * 
    * @description Using the components.json file, it will iterate through the components and create the level files for each 
    * one.
    *///=========================================================================================================================
    private static void buildShipComponents()
    {
        JSONObject components = null;
        try
        {
            components = (JSONObject) myJSONLoader.load( myClassLoader.getResourceAsStream("Configuration/components.json") );
        }
        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(components != null)
        {
            for(Iterator iterator = components.keySet().iterator(); iterator.hasNext();)
            {
                String name = (String) iterator.next();
                JSONObject info = (JSONObject) components.get(name);
                
                myJSONCreator.generateLevelFile
                (
                    Integer.parseInt( (String)info.get("BASE_TIME") ), 
                    Integer.parseInt( (String)info.get("MIN_LEVEL") ),
                    Integer.parseInt( (String)info.get("MAX_LEVEL") ), 
                    name + "_LEVELS.json"
                );
            }
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
