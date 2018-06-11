package singh.pk.locationbasedapp.explore;

import java.util.List;

import singh.pk.locationbasedapp.pojo.Item_;
import singh.pk.locationbasedapp.pojo_image.ItemImage;

public interface ExploreContrect {

    interface Views {

        void showPlaceInfo(List<Item_> placeInfo);

        void getItemClickValue(Item_ placeDetails);

        void getImageUser(Item_ placeDetails);

        void showImages(List<ItemImage> images, Item_ userInfo);

        void showErrorMessage(String error);


    }

    interface Actions {

        void getPlaceInfo(List<Item_> placeInfo);

        void getPlaceInfoApiCall(String location, String placeType);

        void getImageApiCAll(Item_ userId);

        void getImages(List<ItemImage> images, Item_ userInfo);

        void getError(String error);
    }
}
