package singh.pk.locationbasedapp.search;

import java.util.List;

import singh.pk.locationbasedapp.pojo.Item_;
import singh.pk.locationbasedapp.pojo_image.ItemImage;
import singh.pk.locationbasedapp.search.pojo_search_place.Venue;

public interface SearchContrect {

    interface Views {

        void showPlaceInfoResponse(List<Venue> placeInfo);

        void getItemClickValue(Venue placeDetails);

        void getImageUser(Venue placeDetails);

        void showImagesResponse(List<ItemImage> images, Venue userInfo);

        void showErrorResponse(String error);

    }

    interface Actions {

        void getPlaceInfoApiCall(String location);

        void getImageApiCAll(Venue userId);

        void getPlaceInfoApiResponse(List<Venue> placeInfo);

        void getImagesResponse(List<ItemImage> images, Venue userInfo);

        void getErrorResponse(String error);
    }
}
