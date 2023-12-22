package rmit.ad.spotrefresh;
import java.util.Date;

public class SpotInfo {

    private String title;
    private double latitude;
    private double longitude;
    private Date date;

    public SpotInfo() {
        // Default constructor required for Firebase
    }

    public SpotInfo(String title, double latitude, double longitude, Date date) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    // Getter and setter methods for each field

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
