package helper;

import java.io.File;

/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file PathHelper.java
 * @usage a helper function that solving directories references
 *
 */
public class PathHelper {

    static String dir = "C:\\ProgramData\\FGroupIndonesia\\FGIParentRemoteClient\\";

    public static String getLogoPath() {
        return new File(dir + "logo.png").getAbsolutePath();
    }

    public static String getWaitingGIFPath() {
        return new File(dir + "waiting.gif").getAbsolutePath();
    }
    
    public static String getConnectedGIFPath() {
        return new File(dir + "connected.gif").getAbsolutePath();
    }

}
