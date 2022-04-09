package helper;

import javax.swing.JLabel;

/**
 * 
 * @author fgroupindonesia
 * @project FGI Parent Remote Client 
 * for desktop platform (pc & laptop)
 * @file Blinker.java
 * @usage a thread process with a timer as JLabel Animator
 * 
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
