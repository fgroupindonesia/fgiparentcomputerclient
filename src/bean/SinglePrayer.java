/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author asus
 */
public class SinglePrayer {
    int hour, minute;
    
    public SinglePrayer(String timeIn){
        hour = Integer.parseInt(timeIn.split(":")[0]);
        minute = Integer.parseInt(timeIn.split(":")[1]);
    }
    
   
    
    public boolean isHourMatched(int h){
        if(hour == h){
            return true;
        }
        
        return false;
    }
    
    public boolean isMinuteMatched(int m){
        if(minute == m){
            return true;
        }
        
        return false;
    }
}
