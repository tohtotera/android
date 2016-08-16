package jp.auone_net.ab.tohtotera.httpwww.maprefuge;

/**
 * Created by Weider on 2015/11/20.
 */
public class Refuge {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private boolean near;
    private int distance;

    public Refuge(String name, String address, double latitude, double longitude){
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.near = false;
        this.distance = 0;
    }

    public String getName(){
        return this.name;
    }
    public String getAddress(){
        return this.address;
    }
    public double getLat(){
        return this.latitude;
    }
    public double getLng(){
        return this.longitude;
    }
    public boolean isNear(){
        return this.near;
    }
    public int getDistance(){
        return this.distance;
    }
    public void setNear(boolean near){
        this.near = near;
    }
    public void setDistance(float distance){
        this.distance = (int)distance;
    }
}
