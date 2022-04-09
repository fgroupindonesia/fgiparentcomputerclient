package helper;

import java.io.File;

/**
 * 
 * @author fgroupindonesia
 * @project FGI Parent Remote Client 
 * for desktop platform (pc & laptop)
 * @file PathHelper.java
 * @usage a helper function that solving directories references
 * 
 */
public class PathHelper {

    public static String getLogoPath(){
        return new File("C:\\ProgramData\\FGroupIndonesia\\FGIParentRemoteClient\\logo.png").getAbsolutePath();
    }
    
}
