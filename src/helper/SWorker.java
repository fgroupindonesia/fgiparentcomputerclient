/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file SWorker.java
 * @usage a thread for timeout (countdown) terminating current pc
 *
 */
package helper;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author asus
 */
public class SWorker extends SwingWorker {

    JLabel statusLabel;
    int endedMode = -1;

    static final int MODE_SHUTDOWN = 1, MODE_RESTART = 2;

    public void setLabel(JLabel labelIn) {
        statusLabel = labelIn;
    }

    int totalSeconds = -1;
    int minute, second;

    public void setMinuteLimit(int inMinute) {
        totalSeconds = inMinute * 60;

        minute = inMinute;
        second = 60;
    }

    final int TIME_WAIT = 1000;
    boolean stillWork = true;

    private void countDown() {
        if (second > 0) {
            second--;
        } else {
            // when it is already 0

            if (minute > 0) {
                second = 60;
                minute--;
            } else {
                // when minute is already ended
                second = 0;
                minute = 0;
                stillWork = false;
            }
        }

    }

    // Method
    @Override
    protected Object doInBackground()
            throws Exception {

        // Defining what thread will do here
        while (stillWork) {

            countDown();

            updateLabel();
            Thread.sleep(TIME_WAIT);
            //publish(i);
        }

        return null;
    }

    StringBuffer stb;

    private void updateLabel() {

        stb = new StringBuffer();

        stb.append(getNumber(minute));
        stb.append(":");
        stb.append(getNumber(second));

        statusLabel.setText(stb.toString());
        stb = null;

    }

    private String getNumber(int num) {

        if (num < 10) {
            return "0" + num;
        }

        return String.valueOf(num);
    }

    // Method
    @Override
    protected void process(List chunks) {

    }

    public void setMode(int modeChosen) {
        endedMode = modeChosen;

    }

    private int whatIsMode() {
        return endedMode;
    }

    // Method
    @Override
    protected void done() {
        // this method is called when the background
        // thread finishes execution
        try {
            if (whatIsMode() == MODE_SHUTDOWN) {
                CMDCaller.shutdown();
            } else {
                CMDCaller.restart();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
