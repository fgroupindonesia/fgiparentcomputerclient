package helper;

import frames.Main;
import java.io.*;
import java.net.*;
import javax.swing.JTextArea;

/**
 * 
 * @author fgroupindonesia
 * @project FGI Parent Remote Client 
 * for desktop platform (pc & laptop)
 * @file SocketHelper.java
 * @usage a socket creator (ip address & port)
 * and also a main controller for several initial calls.
 * 
 */

public class SocketHelper extends Thread{

    ServerSocket providerSocket;
    Socket sock;
    BufferedReader input;
    PrintWriter output;
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
    
    public void setInputOutput(BufferedReader inputCome, PrintWriter outputCome){
        input = inputCome;
        output = outputCome;
    }
    
    final int OPENING_PORT = 2004;
    
    public void opening() {
        try {
            //1. creating a server socket
            providerSocket = new ServerSocket(OPENING_PORT);
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

    public void closing() {
        try {
            input.close();
            output.close();
            sock.close();
        } catch (Exception ex) {
            //ex.printStackTrace(); let it be
            System.out.println("Closed...");
        }

    }

}
