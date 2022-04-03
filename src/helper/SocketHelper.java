package helper;

import frames.Main;
import java.io.*;
import java.net.*;
import javax.swing.JTextArea;

/**
 *
 * @author asus
 */
public class SocketHelper extends Thread{

    ServerSocket providerSocket;
    JTextArea textarea;
    String message;
    MessageTranslator msgTrans = new MessageTranslator(this);
    
    Main mfa;
    
    public void setMainFrame(Main mf){
        mfa = mf;
    }
    
    public void setOutputArea(JTextArea jt){
        textarea = jt;
    }
    
    public void run(){
        opening();
    }
    
    public void opening() {
        try {
            //1. creating a server socket
            providerSocket = new ServerSocket(2004);
            //2. Wait for connection
            System.out.println("Waiting for connection");

         
            // the part that will help reading in the input is started here
            msgTrans.setMainFrame(mfa);
            msgTrans.setTextArea(textarea);
            msgTrans.reading(providerSocket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closing(BufferedReader input, PrintWriter output, Socket sock) {
        try {
            input.close();
            output.close();
            sock.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
