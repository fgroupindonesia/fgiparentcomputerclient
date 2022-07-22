/**
 *
 * @author fgroupindonesia
 * @project FGI Parent Remote Client for desktop platform (pc & laptop)
 * @file MyIP.java
 * @usage is a bean for a respond of URL Call from json format to java 
 *
 */
package bean;

 /*{
    "ip":"103.247.197.2",
    "hostname":"a103-247-197-2.bdo.starnet.net.id",
    "city":"Bandung",
    "region":"West Java",
    "country":"ID",
    "loc":"-6.9222,107.6069",
    "org":"AS55699 PT. Cemerlang Multimedia",
    "timezone":"Asia\/Jakarta",
    "readme":"https:\/\/ipinfo.io\/missingauth"
    }
    */
public class MyIP {
   
    private String ip;
    private String hostname;
    private String city;
    private String region;
    private String country;
    private String loc;
    private String org;
    private String timezone;
    private String readme;

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the loc
     */
    public String getLoc() {
        return loc;
    }

    /**
     * @param loc the loc to set
     */
    public void setLoc(String loc) {
        this.loc = loc;
    }

    /**
     * @return the org
     */
    public String getOrg() {
        return org;
    }

    /**
     * @param org the org to set
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return the readme
     */
    public String getReadme() {
        return readme;
    }

    /**
     * @param readme the readme to set
     */
    public void setReadme(String readme) {
        this.readme = readme;
    }
    
    String hiddenMessage;
    public void setHiddenMessage(String n){
        hiddenMessage = n;
    }
    
    public String toString(){
        return hiddenMessage;
    }
    
}
