/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import frames.Main;
import frames.TimerCountDownFrame;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author asus
 */
public class WorkingTimer extends TimerTask {

    int hour, min, sec;
    JLabel labelRef;
    String text;
    TimerCountDownFrame tframe;
    Main mframe;

    // for countdown purposes
    public void setTimeStarted(int h, int m) {
        hour = h;
        min = m;
        // default 
        sec = 0;
    }

    private String asText(int n) {
        StringBuffer x = new StringBuffer();

        if (n < 10) {
            x.append("0");
        }

        x.append(n);

        return x.toString();
    }

    public String getText() {

        StringBuffer all = new StringBuffer();
        all.append(asText(hour));
        all.append(":");
        all.append(asText(min));
        all.append(":");
        all.append(asText(sec));

        return all.toString();
    }

    public WorkingTimer(JLabel in, TimerCountDownFrame atframe, Main amframe) {
        mframe = amframe;
        tframe = atframe;
        labelRef = in;
    }

    public void run() {

        if (isStopwatchMode()) {
            increment();
        } else {
            decrement();
        }

        labelRef.setText(getText());
        //frame.revalidate();
        //frame.repaint();
        //stillWork = !isTimeFinished();
        if (isTimeFinished()) {
            mframe.hideTimerMenu(false);
            tframe.dispose();
            mframe.applyFutureAction();
            
            this.cancel();
        }
        //System.out.println(getText());

    }

    // private boolean stillWork = true;
    private boolean isTimeFinished() {
        if (min == 0 && hour == 0 && sec == 0) {
            return true;
        }

        return false;
    }

    private void increment() {
        if (sec < 59) {
            sec++;
        }

        if (sec == 60) {
            if(min<59)
            min++;
            sec = 0;
        }

        if (min == 60) {
            hour++;
            min = 0;
        }
    }

    private void decrement() {
        if (sec != 0) {
            sec--;
            
        }

        if (sec == 0 && !isTimeFinished()) {
            if (min != 0) {
                min--;
            }

            sec = 60;

            if (min == 0) {
                if (hour != 0) {
                    hour--;

                    min = 60;
                }
            }

        }

    }

    public static final int MODE_COUNTDOWN = 1;
    public static final int MODE_STOPWATCH = 2;

    private int modeChosen;

    public void setMode(int ops) {
        modeChosen = ops;
    }

    private boolean isStopwatchMode() {
        if (modeChosen == MODE_STOPWATCH) {
            return true;
        }

        return false;
    }

}
