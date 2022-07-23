/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file AdzanAnnouncer.java
 * @usage a URL Access for obtaining a time of prayers for current month only
 * and it will be displayed along with the audio will be muted once the adzan
 * time
 *
 */
package helper;

import bean.MyIP;
import bean.Prayers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import frames.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AdzanAnnouncer {

    final int TIME_MINUTE_LIMIT_WAIT = 30;
    final String URL_TEST = "http://webcode.me";
    final String URL_MY_IP = "https://fgipc.api.fgroupindonesia.com/client/myip";
    //final String URL_PRAYER_TIME  = "http://api.aladhan.com/v1/calendarByCity?city=London&country=United%20Kingdom&method=2&month=04&year=2017"
    final String URL_PRAYER_TIME = "http://api.aladhan.com/v1/calendarByCity?";

    String yearText, monthText;

    MyIP dataObtained;
    Prayers dataPrayer;

    public MyIP getData() {
        return dataObtained;
    }

    public Prayers getPrayerData() {
        return dataPrayer;
    }

    public MyIP getMyIP() {
        return dataObtained;
    }

    public void getCurrentDate() {
        // for making digit number purposes before calling
        // adhan API for prayers time

        Date nDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");

        String hasil = sdf.format(nDate);

        monthText = hasil.split("-")[0];
        yearText = hasil.split("-")[1];
    }

   
    Main mainFrame;
    public AdzanAnnouncer(Main mf) {
        HttpURLConnection con = null;

        firstURLCall(con);
        secondURLCall(con);

        mainFrame = mf;
        //System.out.println(audioPath);
    }


    private void firstURLCall(HttpURLConnection con) {
        String res = mainURLCall(con, URL_MY_IP);

        if (res != null) {
            dataObtained = new Gson().fromJson(res, MyIP.class);
            dataObtained.setHiddenMessage(res);
        }
    }

    private void secondURLCall(HttpURLConnection con) {

        // preparing the yearText & monthText
        getCurrentDate();

        // constructing new complete URL Call (valid)
        StringBuffer urlEdit = new StringBuffer();
        urlEdit.append(URL_PRAYER_TIME);
        urlEdit.append("city=");
        urlEdit.append(dataObtained.getCity());
        urlEdit.append("&country=");
        urlEdit.append(dataObtained.getCountry());
        urlEdit.append("&method=2");
        urlEdit.append("&month=");
        urlEdit.append(monthText);
        urlEdit.append("&year=");
        urlEdit.append(yearText);

        String res = mainURLCall(con, urlEdit.toString()).toLowerCase();

        if (res != null) {
            // this is a long json output obtained from the URL call
            JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            // get the first entry
            JsonElement jsonEl = jsonArray.get(0);
            JsonObject anotherObject = jsonEl.getAsJsonObject();
            //anotherObject.get("timings").toString();
            String timing = anotherObject.get("timings").toString();

            dataPrayer = new Gson().fromJson(timing, Prayers.class);

            // here it goes obtaining the prayer time
            int perDetik = 5;
            AdzanTimer adzTimer = new AdzanTimer();
            adzTimer.setPrayerData(dataPrayer);
            adzTimer.setMainFrameRef(mainFrame);
            
            new Timer().schedule(adzTimer, 0, perDetik * 1000);

        }
    }

    private String mainURLCall(HttpURLConnection con, String address) {
        StringBuffer allLines = new StringBuffer();

        try {

            var myurl = new URL(address);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");

            StringBuilder content;

            try ( BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            //System.out.println(content.toString());
            allLines = allLines.append(content.toString());

            System.out.println("Reporting only");
            System.out.println("===============\n===============");
            System.out.println(allLines);
            System.out.println("===============\n===============");

        } catch (Exception ex) {

            System.err.println("Error while requesting GET URL! [" + address + "]");
        } finally {

            con.disconnect();
        }

        return allLines.toString();
    }

}
