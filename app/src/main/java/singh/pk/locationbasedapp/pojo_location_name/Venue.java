package singh.pk.locationbasedapp.pojo_location_name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import singh.pk.locationbasedapp.pojo.BeenHere;
import singh.pk.locationbasedapp.pojo.Contact;
import singh.pk.locationbasedapp.pojo.HereNow;

public class Venue {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("venueChains")
    @Expose
    private List<Object> venueChains = null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Object> getVenueChains() {
        return venueChains;
    }

    public void setVenueChains(List<Object> venueChains) {
        this.venueChains = venueChains;
    }

}
