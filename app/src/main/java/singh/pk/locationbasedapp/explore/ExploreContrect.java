package singh.pk.locationbasedapp.explore;

import java.util.List;

import singh.pk.locationbasedapp.pojo.Item_;
import singh.pk.locationbasedapp.pojo_image.ItemImage;

public interface ExploreContrect {

    interface Views {

        void showLocationName(String locationName);

        void showPlaceInfo(List<Item_> placeInfo);

        void getItemClickValue(Item_ placeDetails);

        void getImageUser(Item_ placeDetails);

        void showImages(List<ItemImage> images);

        void showErrorMessage(String error);


    }

    interface Actions {

        void getLocationName(String locationName);

        void getPlaceInfo(List<Item_> placeInfo);

        void getPlaceInfoApiCall(String location, String placeType);

        void getImageApiCAll(String userId);

        void getImages(List<ItemImage> images);

        void getError(String error);
    }
}
