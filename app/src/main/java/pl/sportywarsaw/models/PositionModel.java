package pl.sportywarsaw.models;

import java.io.Serializable;

/**
 * Created by Marcin Chudy on 17/01/2016.
 */
public class PositionModel implements Serializable{
    private double longitude;
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
