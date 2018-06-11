package singh.pk.locationbasedapp.search;

import java.util.List;

import javax.inject.Inject;

import singh.pk.locationbasedapp.pojo_image.ItemImage;
import singh.pk.locationbasedapp.search.pojo_search_place.Venue;

public class SearchPresenter extends BasePresenterSearch implements SearchContrect.Actions {

    private FragmentSearch fragmentSearch;
    private SearchRepository searchRepository;

    @Inject
    public SearchPresenter(FragmentSearch fragmentSearch, SearchRepository searchRepository) {
        this.fragmentSearch = fragmentSearch;
        this.searchRepository = searchRepository;
        searchRepository.onAttach(this);
    }


    @Override
    public void getPlaceInfoApiCall(String location) {
        searchRepository.getPlaceInfo(location);
    }

    @Override
    public void getPlaceInfoApiResponse(List<Venue> placeInfo) {
        fragmentSearch.showPlaceInfoResponse(placeInfo);
    }

    @Override
    public void getImageApiCAll(Venue userId) {
        searchRepository.getUserImageApiCall(userId);
    }

    @Override
    public void getImagesResponse(List<ItemImage> images, Venue userInfo) {
        fragmentSearch.showImagesResponse(images, userInfo);
    }

    @Override
    public void getErrorResponse(String error) {
        fragmentSearch.showErrorResponse(error);
    }
}
