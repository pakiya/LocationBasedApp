package singh.pk.locationbasedapp.explore;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import singh.pk.locationbasedapp.R;
import singh.pk.locationbasedapp.explore.di.DaggerExploreComponet;
import singh.pk.locationbasedapp.explore.di.ExploreComponet;
import singh.pk.locationbasedapp.explore.di.ExploreModule;
import singh.pk.locationbasedapp.pojo.Item_;
import singh.pk.locationbasedapp.pojo_image.ItemImage;

import static android.app.Activity.RESULT_OK;

public class FragmentExplore extends Fragment implements ExploreContrect.Views, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    // Views
    @BindView(R.id.food_btn) Button foodBtn;
    @BindView(R.id.drink_btn) Button drinkBtn;
    @BindView(R.id.coffee_btn) Button coffeeBtn;
    @BindView(R.id.shops_btn) Button shopsBtn;
    @BindView(R.id.arts_btn) Button artsBtn;
    @BindView(R.id.outdoors_btn) Button outdoorsBtn;
    @BindView(R.id.sights_btn) Button sightsBtn;
    @BindView(R.id.trending_btn) Button trendingBtn;

    @BindView(R.id.current_location_img) ImageView currentLocationImg;
    @BindView(R.id.location_name_txt) TextView locationNameTxt;
    @BindView(R.id.search_location_img) ImageView searchLocationImg;
    @BindView(R.id.detail_recycler_view_explore_fragment) RecyclerView detailsListRecycler;
    @BindView(R.id.fragment_explor_progress_bar) ProgressBar mPBLoading;

    // Items
    private static final String FOOD = "food";
    private static final String DRINKS = "drinks";
    private static final String COFFEE = "coffee";
    private static final String SHOPS = "shops";
    private static final String ARTS = "arts";
    private static final String OUTDOORS = "outdoors";
    private static final String SIGHTS = "sights";
    private static final String TRENDING = "trending";

    @Inject
    ExplorePresenter explorePresenter;
    @Inject
    ItemListAdapterExplore itemListAdapterExplore;

    // Location
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;

    private com.google.android.gms.location.LocationListener listener;

    String LOCATION_NAME = null;
    double LONGITUDE ;
    double LATITUDE ;
    private static final int RESULT_CANCELED = 323;


    private static final String TAG = "FragmentExplore";

    List<ItemImage> imageList = new ArrayList<>(0);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // DI inject
        ButterKnife.bind(this, view);

        // DI inject
        ExploreComponet exploreComponet = DaggerExploreComponet.builder().exploreModule(new ExploreModule(this)).build();
        exploreComponet.injectExploreFragment(this);


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        checkLocation();
        init();
        removeProgress();
    }

    private void init(){

        detailsListRecycler.setHasFixedSize(true);
        detailsListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsListRecycler.setAdapter(itemListAdapterExplore);

        // Get Current location code.
        currentLocationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE,1);
                    if (addresses.size() > 0){
                        LOCATION_NAME = addresses.get(0).getLocality();
                        locationNameTxt.setText(LOCATION_NAME);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // Location search code.
        searchLocationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        // Food Api call
        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, FOOD);
            }
        });

        // Drink Api call
        drinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, DRINKS);
            }
        });

        // Coffee Api call
        coffeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, COFFEE);
            }
        });

        // Shops Api call
        shopsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, SHOPS);
            }
        });

        // Arts Api call
        artsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, ARTS);
            }
        });

        // Outdoors Api call
        outdoorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, OUTDOORS);
            }
        });

        // Sights Api call
        sightsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, SIGHTS);
            }
        });

        // Trending Api call
        trendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeApiCall(LOCATION_NAME, TRENDING);
            }
        });

    }

    public void placeApiCall(String locationName, String placeType){
        if (locationName != null && placeType != null){
            explorePresenter.getPlaceInfoApiCall(locationName, placeType);
            showProgress();
        } else {
            Toast.makeText(getContext(), "Parameter not pass", Toast.LENGTH_SHORT).show();
        }
    }

    // get location after search.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "Place: " + place.getName());
                LOCATION_NAME = String.valueOf(place.getName());
                if (LOCATION_NAME != null){
                    locationNameTxt.setText(LOCATION_NAME);
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void showPlaceInfo(List<Item_> placeInfo) {
        removeProgress();
        itemListAdapterExplore.updatePlaceInfo(placeInfo);
    }

    @Override
    public void getItemClickValue(Item_ placeDetails) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_info, null);

        ImageView imageView = view.findViewById(R.id.user_images);
        TextView name = view.findViewById(R.id.name);
        TextView cityName = view.findViewById(R.id.city_name);
        TextView stateName = view.findViewById(R.id.state_name);
        TextView countryName = view.findViewById(R.id.country_name);
        TextView contactNumber = view.findViewById(R.id.contact_number);

        try {
            String url_1 = imageList.get(0).getPrefix();
            String url_2 = imageList.get(0).getSuffix();
            Picasso.get().load(url_1+"300x200"+url_2).placeholder(R.drawable.default_icon).into(imageView);
        } catch (IndexOutOfBoundsException t){
            t.getMessage();
        }
        name.setText(placeDetails.getVenue().getName());
        cityName.setText(placeDetails.getVenue().getLocation().getCity());
        stateName.setText(placeDetails.getVenue().getLocation().getState());
        countryName.setText(placeDetails.getVenue().getLocation().getCountry());
        contactNumber.setText(placeDetails.getVenue().getContact().getPhone());

        mBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        mBuilder.setView(view);
        AlertDialog dialog = mBuilder.create();
        removeProgress();
        dialog.show();

    }

    @Override
    public void getImageUser(Item_ placeDetails) {
        explorePresenter.getImageApiCAll(placeDetails);
        showProgress();
    }

    @Override
    public void showImages(List<ItemImage> images, Item_ userInfo) {
        imageList.clear();
        for (int i = 0; i<images.size(); i++){
            imageList.add(images.get(i));
        }
        getItemClickValue(userInfo);
    }

    @Override
    public void showErrorMessage(String error) {
        removeProgress();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            LATITUDE = mLocation.getLatitude();
            LONGITUDE = mLocation.getLongitude();


        } else {
            Toast.makeText(getContext(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        if (LOCATION_NAME != null){
            locationNameTxt.setText(LOCATION_NAME);
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }


    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void showProgress() { mPBLoading.setVisibility(View.VISIBLE); }
    public void removeProgress() { mPBLoading.setVisibility(View.GONE); }

}
