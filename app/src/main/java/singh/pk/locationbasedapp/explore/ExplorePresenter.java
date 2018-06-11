package singh.pk.locationbasedapp.explore;


import java.util.List;

import javax.inject.Inject;

import singh.pk.locationbasedapp.pojo.Item_;
import singh.pk.locationbasedapp.pojo_image.ItemImage;

public class ExplorePresenter extends BasePresenter implements ExploreContrect.Actions {

    FragmentExplore fragmentExplore;
    ExploreRepository exploreRepository;

    @Inject
    public ExplorePresenter(FragmentExplore fragmentExplore, ExploreRepository exploreRepository) {
        this.fragmentExplore = fragmentExplore;
        this.exploreRepository = exploreRepository;
        exploreRepository.onAttach(this);
    }

    @Override
    public void getPlaceInfo(List<Item_> placeInfo) {
        fragmentExplore.showPlaceInfo(placeInfo);
    }

    @Override
    public void getPlaceInfoApiCall(String location, String placeType) {
        exploreRepository.getPlaceInfoApiCall(location, placeType);
    }

    @Override
    public void getImageApiCAll(Item_ placeInfo) {
        exploreRepository.getUserImageApiCall(placeInfo);
    }

    @Override
    public void getImages(List<ItemImage> images, Item_ userInfo) {
        fragmentExplore.showImages(images, userInfo);
    }

    @Override
    public void getError(String error) {
        fragmentExplore.showErrorMessage(error);
    }


}
