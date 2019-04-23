package com.fixpapa.ffixpapa.EngineerPart;

public class RealTimeDBGetterSetter {
    public String destination;
    public String source;
    public  double lat;
    public  double lng;
    public String rem_distance;
    public String rem_duration;
    public long  timestamp;

    public RealTimeDBGetterSetter() {
    }

    public RealTimeDBGetterSetter(String destination, String source,double lat,double lng,String rem_distance,String rem_duration,long timestamp) {
        this.destination = destination;
        this.source = source;
        this.lat = lat;
        this.lng = lng;
        this.rem_distance = rem_distance;
        this.rem_duration = rem_duration;
        this.timestamp = timestamp;
    }
}
