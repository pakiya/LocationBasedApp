package singh.pk.locationbasedapp.pojo_image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photos {


    @SerializedName("items")
    @Expose
    private List<ItemImage> items = null;


    public List<ItemImage> getItems() {
        return items;
    }

    public void setItems(List<ItemImage> items) {
        this.items = items;
    }

}
