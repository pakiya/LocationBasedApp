package singh.pk.locationbasedapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item_ {

    @SerializedName("venue")
    @Expose
    private Venue venue;
    @SerializedName("tips")
    @Expose
    private List<Tip> tips = null;
    @SerializedName("referralId")
    @Expose
    private String referralId;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Tip> getTips() {
        return tips;
    }

    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

    public String getReferralId() {
        return referralId;
    }

    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }
}
