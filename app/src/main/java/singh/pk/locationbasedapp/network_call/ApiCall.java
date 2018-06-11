package singh.pk.locationbasedapp.network_call;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import singh.pk.locationbasedapp.pojo.NLPResponce;
import singh.pk.locationbasedapp.pojo_image.ImagePojo;
import singh.pk.locationbasedapp.search.pojo_search_place.LocationName;


public interface ApiCall {

    @GET("venues/search")
    Call<LocationName> getPlaceInfoForSearchTab(@Query("ll") String location,
                                       @Query("oauth_token") String token,
                                       @Query("v") String v);

    @GET("venues/explore")
    Call<NLPResponce>  getPlaceInfo(@Query("near") String location,
                                    @Query("section") String placeType,
                                    @Query("oauth_token") String token,
                                    @Query("v") String v);

    @GET
    Call<ImagePojo> getUserImage(@Url String userId);
}
