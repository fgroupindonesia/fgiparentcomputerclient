package bean;

/**
 * 
 * @author fgroupindonesia
 * @project FGI Parent Remote Client 
 * for desktop platform (pc & laptop)
 * @file Reply.java
 * @usage from Server
 * 
 */
public class Reply {
    private String status;
    private String data;

    /**
     * @return the status
     */
    
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }
}
