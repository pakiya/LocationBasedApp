package singh.pk.locationbasedapp.explore;

import android.content.Context;
import android.util.Log;

import com.fatboyindustrial.gsonjodatime.DateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.util.List;

import javax.inject.Inject;


import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import singh.pk.locationbasedapp.BuildConfig;
import singh.pk.locationbasedapp.network_call.ApiCall;
import singh.pk.locationbasedapp.network_call.di.NetworkModule;
import singh.pk.locationbasedapp.pojo.Item_;
import singh.pk.locationbasedapp.pojo.NLPResponce;
import singh.pk.locationbasedapp.pojo_image.ImagePojo;
import singh.pk.locationbasedapp.pojo_image.ItemImage;
import singh.pk.locationbasedapp.pojo_location_name.LocationName;

public class ExploreRepository extends BaseRepository<ExplorePresenter>{


    private static final String BASE_URL = "https://api.foursquare.com/v2/venues/";
    private static final String SECTION = "section";
    private static final String AUTH_TOKEN = "RAQPQRL20GP3EDSJPUH2YEZACXQB21OGUZSBP4T24PITASQ0";



    FragmentExplore fragmentExplore;
    Retrofit retrofit;
    ApiCall apiCall;

    ExplorePresenter explorePresenter;

    @Inject
    public ExploreRepository(FragmentExplore fragmentExplore) {
    this.fragmentExplore = fragmentExplore;
//    this.explorePresenter = explorePresenter;
        retrofitInstance();
        apiCall= retrofit.create(ApiCall.class);

    }

    private void retrofitInstance() {
        OkHttpClient okHttpClient = okHttpClient(loggingInterceptor(),cache(cacheFile(fragmentExplore.getContext())));
        retrofit = retrofit(okHttpClient, gson());
    }

    public Retrofit retrofit (OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BuildConfig.LOCATION_BASED_APP_BASE_URL)
                .build();
    }

    public Gson gson () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        return gsonBuilder.create();
    }

    public HttpLoggingInterceptor loggingInterceptor () {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(NetworkModule.class.getSimpleName(), message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public Cache cache (File cacheFile) {
        return new Cache(cacheFile, 10*1000 * 1000); //10MB Cache
    }

    public File cacheFile(Context context) {
        return new File(context.getCacheDir(), "okhttp_cache");
    }

    public OkHttpClient okHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }

    // Get location Name
    public void getLocationName(String latitude, String longitude){

        Call<LocationName> call = apiCall.getLocationName(latitude+","+longitude, AUTH_TOKEN,String.valueOf(20180611));
        call.enqueue(new Callback<LocationName>() {
            @Override
            public void onResponse(Call<LocationName> call, Response<LocationName> response) {
                if (response.code() == 200) {
                    LocationName locationName = response.body();
                    if (locationName != null){
                        String location = locationName.getResponse().getVenues().get(0).getLocation().getCity();
                        if (location !=null){
                            getActions().getLocationName(location);
                        } else {
                            getActions().getLocationName(locationName.getResponse().getVenues().get(0).getLocation().getCc());
                        }
                    } else {
                        getActions().getError("Data fetching problem");
                    }
                }else {
                    getActions().getError("No result fetched");
                }
            }

            @Override
            public void onFailure(Call<LocationName> call, Throwable t) {

            }
        });

    }

    // food Api hit
    public void getPlaceInfoApiCall(String location, String placeType){

        Call<NLPResponce> call = apiCall.getPlaceInfo(location, placeType, AUTH_TOKEN, String.valueOf(20180611));
        call.enqueue(new Callback<NLPResponce>() {
            @Override
            public void onResponse(Call<NLPResponce> call, Response<NLPResponce> response) {
                if (response.code() == 200){
                    if (response.body() != null){
                        List<Item_> Items = response.body().getResponse().getGroups().get(0).getItems();
                        getActions().getPlaceInfo(Items);
                    } else {
                        getActions().getError("No result fetched");
                    }
                }else {
                    getActions().getError("No result fetched");
                }
            }

            @Override
            public void onFailure(Call<NLPResponce> call, Throwable t) {

                Log.e("Explore Repo ::", t.getMessage());
            }
        });
    }

    // user Image Api Hit
    public void getUserImageApiCall(String userId){

        String url = BASE_URL+userId+"/photos?&oauth_token="+AUTH_TOKEN+"&v=20180610";
        Call<ImagePojo> call = apiCall.getUserImage(url);
        call.enqueue(new Callback<ImagePojo>() {
            @Override
            public void onResponse(Call<ImagePojo> call, Response<ImagePojo> response) {
                if (response.code() == 200){
                 if (response.body() !=null){
                     List<ItemImage> imagePojo = response.body().getResponse().getPhotos().getItems();
                     getActions().getImages(imagePojo);
                 }else {
                     getActions().getError("No result fetched");
                 }
                } else {
                    getActions().getError("No result fetched");
                }
            }

            @Override
            public void onFailure(Call<ImagePojo> call, Throwable t) {

            }
        });

    }


}
