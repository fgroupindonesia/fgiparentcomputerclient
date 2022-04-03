package helper;

import bean.Entry;
import bean.Reply;
import com.google.gson.Gson;
import frames.Main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;

/**
 *
 * @author asus
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
    }

    public void reading(ServerSocket providerSocket) {

        String message = null;
        boolean keepReading = true;

        try {
            connection = providerSocket.accept();

            if (jtx != null) {
                jtx.append("connection success!\n");
            }

            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            output = new PrintWriter(connection.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            output.println(createReplyAsJSONString("success", null));

            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println("We got " + line);

                // we got entry object
                Entry dataEntry = new Gson().fromJson(line, Entry.class);

                if (jtx != null) {
                    jtx.append("client : " + dataEntry.getCommand() + "\n");
                    scrollDown();
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

    }

    private void recreateBack() {
        if (shp != null) {
            shp.closing(input, output, connection);
        }

        shp.opening();
    }

    private void detectTheMessage(Entry msg) {

        String cmd = msg.getCommand();

        switch (cmd) {
            case "mute_audio":
                CMDCaller.muteSystem();
                break;
            case "unmute_audio":
                CMDCaller.unMuteSystem();
                break;
            case "shutdown_pc":
                CMDCaller.shutdown();
                break;
            case "restart_pc":
                CMDCaller.restart();
                break;
            case "list_app":
                CMDCaller.listRunningApp();
                break;
            case "kill_app":
                CMDCaller.kill(msg.getData());
                break;
            case "message_add":
                showWarning(msg.getData());
                break;
            case "message_delete":
                disableWarning();
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

}
