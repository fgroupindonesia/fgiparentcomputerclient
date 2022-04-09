package helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 
 * @author fgroupindonesia
 * @project FGI Parent Remote Client 
 * for desktop platform (pc & laptop)
 * @file CMDCaller.java
 * @usage a background process for executing CommandPrompt (as a daemon no GUI appeared)
 * 
 */

public class CMDCaller {

    private static ArrayList<String> data = new ArrayList<String>();

    public static ArrayList<String> getData() {
        return data;
    }
    
    public static String getDataAsString(){
        StringBuffer stb = new StringBuffer();
        
        for(String s: data){
            stb.append(s.trim()+";");
        }
        
       return stb.toString();
    }

    public static void muteSystem(){
        execute("nircmd.exe mutesysvolume 1", false);
    }
    
    public static void unMuteSystem(){
        execute("nircmd.exe mutesysvolume 0", false);
    }
    
    public static void kill(String filename) {
        // filename is with extension
        execute("taskkill.exe /F /IM " + filename, false);
    }

    public static void listRunningApp() {
        data.clear();
        //execute("wmic.exe process list", true);
        //execute("wmic.exe process get ProcessId,Description,ParentProcessId,ReadOperationCount,WriteOperationCount", true);
        execute("wmic.exe process get Description", true);
    }

    public static void shutdown() {
        execute("shutdown.exe -s -f -t 0", false);
    }
    
    public static void restart() {
        execute("shutdown.exe -r -f -t 0", false);
    }

    private static void execute(String cmdCommand, boolean needData) {

        System.out.println("Executing " + cmdCommand);

        Runtime runtime = Runtime.getRuntime();
        try {
            String line = null;
            Process proc = runtime.exec(cmdCommand);
            //proc.waitFor();
            if (needData) {
                BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                while ((line = input.readLine()) != null) {
                    //System.out.println(line); //<-- Parse data here.
                    if (line.contains(".exe")) {
                        if(!data.contains(line)){
                        data.add(line);
                        }
                    }
                }
                input.close();
            }
        } catch (Exception ex) {
            LogMe.write(ex);
            System.err.print(ex.getMessage());
        }
        // System.exit(0);
    }

}
