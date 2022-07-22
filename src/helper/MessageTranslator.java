package helper;

import bean.Entry;
import bean.Reply;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import frames.Main;
import frames.TimerCountDownFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;

/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file MessageTranslator.java
 * @usage a translator between client and server communication
 *
 */
public class MessageTranslator {

    SocketHelper shp = null;
    Socket connection = null;
    PrintWriter output;
    BufferedReader input;
    JTextArea jtx;

    public void setTextArea(JTextArea jta) {
        jtx = jta;
    }

    public MessageTranslator(SocketHelper one) {
        shp = one;
    }

    private String createReplyAsJSONString(String stat, String data) {
        Reply en = new Reply();
        en.setStatus(stat);
        en.setData(data);
        String jsonInString = new Gson().toJson(en);
        return jsonInString;
    }

    private void scrollDown() {
        DefaultCaret caret = (DefaultCaret) jtx.getCaret();
        caret.setUpdatePolicy(ALWAYS_UPDATE);

        jtx.setCaretPosition(jtx.getDocument().getLength());
    }

    private String makePreviousTaskList() {
        // we are making a json array
        ArrayList<Entry> workPrev = new ArrayList<Entry>();

        if (blinkingPrevious) {
            workPrev.add(new Entry("message", null));
        }

        if (soundChanged) {
            workPrev.add(new Entry("sound_mix", String.valueOf(volPrevious)));
        }

        // JSONArray jsArray = new JSONArray(list);
        // Gson gson = new GsonBuilder().create();
        // JsonArray myCustomArray = gson.toJsonTree(myCustomList).getAsJsonArray();
        return new GsonBuilder().create().toJsonTree(workPrev).getAsJsonArray().toString();

    }

    private void writeInfo(String n) {
        if (jtx != null) {
            jtx.append(n + "\n");
            scrollDown();
        }
    }

    boolean blinkingPrevious = false;
    boolean soundChanged = false;

    public void reading(ServerSocket providerSocket) {

        boolean keepReading = true;
        while (keepReading) {

            try {
                System.out.println("Ready to accept another socket...");
                connection = providerSocket.accept();

                writeInfo("connection success!");

                if (mainFrame != null) {
                    mainFrame.setIconTray(Main.CONNECTED);
                }

                writeInfo("Connection received from " + connection.getInetAddress().getHostName());
                //3. get Input and Output streams
                output = new PrintWriter(connection.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // let the first object SHP also know the
                // way of closing later
                shp.setInputOutput(input, output);

                if (!soundChanged && !blinkingPrevious) {
                    writeInfo("We have new action...!");
                    output.println(createReplyAsJSONString("success", null));
                } else {
                    writeInfo("We have pending activities...");
                    // when we have work previously
                    output.println(createReplyAsJSONString("pending", makePreviousTaskList()));
                    writeInfo("sending..." + makePreviousTaskList());
                }

                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println("We got " + line);

                    // we got entry object
                    Entry dataEntry = new Gson().fromJson(line, Entry.class);

                    if (dataEntry.getData() != null) {
                        writeInfo("client : " + dataEntry.getCommand() + " with " + dataEntry.getData());
                    } else {
                        writeInfo("client : " + dataEntry.getCommand());

                    }

                    detectTheMessage(dataEntry);

                    if (dataEntry.getCommand().contains("list_app")) {
                        // wait for 3 seconds before proceed
                        Thread.sleep(7000);
                        output.println(createReplyAsJSONString("private", CMDCaller.getDataAsString()));
                    } else {
                        output.println(createReplyAsJSONString("okay", null));
                    }

                }

            } catch (Exception ex) {
                System.err.println("Data received in unknown format " + ex.getMessage());
                //keepReading = false;
                //recreateBack();
            }

            System.out.println("Connection ended! Thanks...");
            //disableWarning();
            if (mainFrame != null) {
                mainFrame.setIconTray(Main.DISCONNECTED);
            }

            writeInfo("client : disconnected!");

        }
    }

    private void recreateBack() {
        if (shp != null) {
            shp.closing();
        }

        shp.opening();
    }

    int volPrevious;

    private void detectTheMessage(Entry msg) {

        String cmd = msg.getCommand();

        switch (cmd) {
            case "mute_audio":
                CMDCaller.muteSystem();
                volPrevious = 0;
                soundChanged = true;
                break;
            case "unmute_audio":
                CMDCaller.unMuteSystem();
                volPrevious = 100;
                soundChanged = true;
                break;
            case "shutdown_pc":
                CMDCaller.shutdown();
                break;
            case "shutdown_pc_timer":
                terminateByTimer(Integer.parseInt(msg.getData()), SWorker.MODE_SHUTDOWN);
                break;
            case "restart_pc":
                CMDCaller.restart();
                break;
            case "restart_pc_timer":
                terminateByTimer(Integer.parseInt(msg.getData()), SWorker.MODE_RESTART);
                break;
            case "list_app":
                CMDCaller.listRunningApp();
                break;
            case "kill_app":
                CMDCaller.kill(msg.getData());
                break;
            case "message_add":
                showWarning(msg.getData());
                blinkingPrevious = true;
                break;
            case "message_delete":
                disableWarning();
                blinkingPrevious = false;
                break;
            case "sound_mix":
                soundChanged = true;
                // there must be either 0 to 100
                volPrevious = Integer.parseInt(msg.getData());
                CMDCaller.setSound(Integer.parseInt(msg.getData()));
                break;
        }
    }

    Main mainFrame;

    public void setMainFrame(Main mfa) {
        mainFrame = mfa;
    }

    private void showWarning(String message) {
        mainFrame.blinking(message);
    }

    private void disableWarning() {
        mainFrame.removeBlinking();
    }

    SWorker sw1;
    TimerCountDownFrame tmc;

    private void terminateByTimer(int minLimit, int aModeChosen) {

        if (tmc != null) {
            tmc.dispose();
        }

        tmc = new TimerCountDownFrame();
        tmc.setVisible(true);

        if (sw1 != null) {
            sw1.cancel(true);
        }

        sw1 = new SWorker();
        sw1.setMinuteLimit(minLimit);
        sw1.setLabel(tmc.getLabel());
        sw1.setMode(aModeChosen);

        // Executes the swingworker on worker thread
        sw1.execute();

    }

}
