package singh.pk.locationbasedapp.pojo_location_name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("cc")
    @Expose
    private String cc;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("crossStreet")
    @Expose
    private String crossStreet;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;


    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCrossStreet() {
        return crossStreet;
    }

    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
