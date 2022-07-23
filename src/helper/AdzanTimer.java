/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file AdzanTimer.java
 * @usage for running several execution to check the time and action given
 * (before & after) the adzan time
 *
 */
package helper;

import bean.Prayers;
import bean.SinglePrayer;
import frames.Main;
import frames.Settings;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author asus
 */
public class AdzanTimer extends TimerTask {

    Prayers dataPrayer;
    String option;

    public void setPrayerData(Prayers dat) {
        dataPrayer = dat;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    int hour, minute;

    StringBuffer stb = null;

    public String refreshCurrentTime() {
        stb = new StringBuffer();
        stb.append(sdf.format(new Date()));

        hour = Integer.parseInt(stb.toString().split(":")[0]);
        minute = Integer.parseInt(stb.toString().split(":")[1]);

        //System.out.println("Sekarang itu " + hour + ":" + minute);
        return stb.toString();
    }

    public boolean adzanTimeMatched() {
        //if(hour)

        if (isTimeMatched(dataPrayer.getFajr())) {
            return true;
        } else if (isTimeMatched(dataPrayer.getDhuhr())) {
            return true;
        } else if (isTimeMatched(dataPrayer.getAsr())) {
            return true;
        } else if (isTimeMatched(dataPrayer.getMaghrib())) {
            return true;
        } else if (isTimeMatched(dataPrayer.getIsha())) {
            return true;
        }

        return false;
    }

    private boolean isTimeMatched(String timePrayer) {
        // "17:45 (WIB)" example format used
        // refreshing date now
        refreshCurrentTime();

        String timeCheck = timePrayer.split(" ")[0];
        SinglePrayer sp = new SinglePrayer(timeCheck);

        System.out.println("Apakah " + timeCheck + " sesuai....?");

        if (sp.isHourMatched(hour) && sp.isMinuteMatched(minute)) {
            return true;
        }

        return false;

    }

    String audioPath = System.getenv("APPDATA") + File.separator + "fgroupindonesia" + File.separator + "fgpcc";
    String adzanFileLocation;

    public AdzanTimer() {
        new File(audioPath).mkdirs();
        audioPath = audioPath + File.separator + "azan.wav";

        this.setAdzanLocation(audioPath);
    }

    public void setAdzanLocation(String n) {
        adzanFileLocation = n;
    }
    AudioInputStream audioInputStream;
    Clip clip;

    private void playAdzan() {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(adzanFileLocation).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent arg0) {

                    LineEvent.Type type = arg0.getType();
                    if (type == type.STOP) {
                        clip.close();
                        clip = null;
                    }

                }
            });

            clip.start();
        } catch (Exception ex) {
            System.err.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    Main mainFrame;

    public void setMainFrameRef(Main mf) {
        mainFrame = mf;
    }

    @Override
    public void run() {

        System.out.println("Checking adzan time...");

        if (adzanTimeMatched()) {
            System.out.println("cocok...!");

            // get the settings data
            option = Settings.getOptionChosen();

            if (option.equalsIgnoreCase(Settings.MODE_CLOSE_BROWSER)) {
                String browserNames[] = {"opera.exe", "firefox.exe", "chrome.exe", "edge.exe"};
                for (String n : browserNames) {
                    CMDCaller.kill(n);
                }

                mainFrame.writeRecentActivity("Killing Browser - success!");
            } else if (option.equalsIgnoreCase(Settings.MODE_MUTE_AUDIO)) {
                CMDCaller.muteSystem();

                mainFrame.writeRecentActivity("Mute System - success!");
            } else if (option.equalsIgnoreCase(Settings.MODE_PLAY_ADZAN)) {
                // just play the audio once only
                if (clip == null) {
                    this.playAdzan();
                    mainFrame.writeRecentActivity("Play Adzan - success!");
                }
            }
        } else {
            System.out.println("blm cocok...");

        }

    }

}
