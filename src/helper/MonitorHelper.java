package helper;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file MonitorHelper.java
 * @usage a switcher for dual monitor usage, thus enabling the warning blinking
 * message appeared in the correct monitor.
 *
 */
public class MonitorHelper {

    GraphicsDevice[] gd;
    
    public void showOnScreen(int screen, JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         gd = ge.getScreenDevices();
        if (screen > -1 && screen < gd.length) {
            frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
        } else if (gd.length > 0) {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
        } else {
            throw new RuntimeException("No Screens Found");
        }
    }

    public void setWider(int screen, JFrame frame) {
        DisplayMode dm = gd[screen].getDisplayMode();
        frame.setSize(dm.getWidth(), (int) frame.getSize().getHeight());
    }

    public boolean hasTwoMonitors() {
        GraphicsDevice[] screens = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getScreenDevices();

        if (screens.length > 1) {
            return true;
        }

        return false;
    }
}
