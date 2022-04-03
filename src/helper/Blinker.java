package helper;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author asus
 */
public class Blinker extends Thread {

    JLabel textLabel;

    public Blinker(JLabel jl) {
        textLabel = jl;
    }

    final int WAIT_TIME = 1000;
    boolean keepWorking = true;

    public void end() {
        keepWorking = false;
    }

    private void animation() {
        try {
            while (keepWorking) {
                Thread.sleep(WAIT_TIME);
                textLabel.setVisible(false);
                Thread.sleep(WAIT_TIME);
                textLabel.setVisible(true);
                Thread.sleep(WAIT_TIME);
                textLabel.setVisible(false);
                Thread.sleep(WAIT_TIME);
                textLabel.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {

        animation();

    }

}
