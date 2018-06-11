package singh.pk.locationbasedapp.pojo_location_name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import singh.pk.locationbasedapp.pojo.Meta;

public class LocationName {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("response")
    @Expose
    private Response response;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
