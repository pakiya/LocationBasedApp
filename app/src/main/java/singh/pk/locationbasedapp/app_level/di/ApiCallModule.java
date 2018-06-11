package singh.pk.locationbasedapp.app_level.di;

import com.fatboyindustrial.gsonjodatime.DateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import singh.pk.locationbasedapp.BuildConfig;
import singh.pk.locationbasedapp.app_level.LocationBasedAppScope;
import singh.pk.locationbasedapp.network_call.ApiCall;
import singh.pk.locationbasedapp.network_call.di.NetworkModule;

@Module(includes = NetworkModule.class)
public class ApiCallModule {

    @Provides
    @LocationBasedAppScope
    public ApiCall apiCall (Retrofit apiCallRetrofit) {
        return apiCallRetrofit.create(ApiCall.class);
    }

    @Provides
    @LocationBasedAppScope
    public Gson gson () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        return gsonBuilder.create();
    }

    @Provides
    @LocationBasedAppScope
    public Retrofit retrofit (OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BuildConfig.LOCATION_BASED_APP_BASE_URL)
                .build();
    }
}
