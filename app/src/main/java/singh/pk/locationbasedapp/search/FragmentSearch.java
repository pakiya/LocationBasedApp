package singh.pk.locationbasedapp.search;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;;
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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import singh.pk.locationbasedapp.R;
import singh.pk.locationbasedapp.pojo.Item_;
import singh.pk.locationbasedapp.pojo_image.ItemImage;
import singh.pk.locationbasedapp.search.di.DaggerSearchComponet;
import singh.pk.locationbasedapp.search.di.SearchComponet;
import singh.pk.locationbasedapp.search.di.SearchModule;
import singh.pk.locationbasedapp.search.pojo_search_place.Venue;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class FragmentSearch extends Fragment implements SearchContrect.Views, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, SearchView.OnQueryTextListener {

    // Views
    @BindView(R.id.current_location_img_search_fragment) ImageView currentLocationImg;
    @BindView(R.id.search_location_img_search_fragment) ImageView searchLocationImg;
    @BindView(R.id.detail_recycler_view_search_fragment) RecyclerView detailsListRecycler;
    @BindView(R.id.fragment_search_progress_bar) ProgressBar mPBLoading;
    @BindView(R.id.search_place) SearchView searchPlaceFromList;
    @BindView(R.id.location_name) TextView locationNameTxt;

    @Inject
    SearchPresenter searchPresenter;

    @Inject
    ItemListAdapterSearch itemListAdapterSearch;

    // get Current location
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LocationManager locationManager;
    private Location mLocation;
    private LocationRequest mLocationRequest;

    double LONGITUDE;
    double LATITUDE ;
    String LOCATION_NAME = null;

    // Search location
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    List<ItemImage> imageList = new ArrayList<>(0);
    List<Venue> userInfo = new ArrayList<>();

    private static final String TAG = "FragmentSearch";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // DI inject
        ButterKnife.bind(this, view);

        // DI inject
        SearchComponet searchComponet = DaggerSearchComponet.builder().searchModule(new SearchModule(this)).build();
        searchComponet.injectSearchFragment(this);


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

    private void init() {

        detailsListRecycler.setHasFixedSize(true);
        detailsListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsListRecycler.setAdapter(itemListAdapterSearch);
        searchPlaceFromList.setOnQueryTextListener(this);

        currentLocationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    searchPresenter.getPlaceInfoApiCall(LATITUDE+","+LONGITUDE);
                    showProgress();
                } catch (NullPointerException e){
                    Toast.makeText(getContext(), "Parameter Error!", Toast.LENGTH_SHORT).show();
                }

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE,1);
                        Log.i(TAG, "Location L & L :::: " +LATITUDE +" , "+LONGITUDE );

                        if (addresses.size() > 0){
                            LOCATION_NAME = addresses.get(0).getLocality();
                            if (LOCATION_NAME != null){
                                locationNameTxt.setText(LOCATION_NAME);
                            }
                            Toast.makeText(getContext(), "Location Name :: "+ LOCATION_NAME, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });

        searchLocationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });



    }

    // get location after search.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "Place: " + place.getLatLng());
                String locationLatLong = String.valueOf(place.getLatLng().latitude+","+place.getLatLng().longitude);
                LOCATION_NAME = String.valueOf(place.getName());
                if (LOCATION_NAME != null){
                    locationNameTxt.setText(LOCATION_NAME);
                }
                if (locationLatLong != null){
                    Toast.makeText(getContext(), locationLatLong, Toast.LENGTH_SHORT).show();
                    searchPresenter.getPlaceInfoApiCall(locationLatLong);
                    showProgress();
                } else {
                    Toast.makeText(getContext(), "Parameter Error!", Toast.LENGTH_SHORT).show();
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
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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

    private void startLocationUpdates() {
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
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
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


    @Override
    public void showPlaceInfoResponse(List<Venue> placeInfo) {
        userInfo =  placeInfo;
        removeProgress();
        itemListAdapterSearch.updatePlaceInfo(userInfo);
    }

    @Override
    public void getItemClickValue(Venue placeDetails) {

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
        name.setText(placeDetails.getName());
        cityName.setText(placeDetails.getLocation().getCity());
        stateName.setText(placeDetails.getLocation().getState());
        countryName.setText(placeDetails.getLocation().getCountry());
        contactNumber.setText(placeDetails.getLocation().getPostalCode());

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
    public void getImageUser(Venue placeDetails) {
        searchPresenter.getImageApiCAll(placeDetails);
        showProgress();
    }

    @Override
    public void showImagesResponse(List<ItemImage> images, Venue userInfo) {
        imageList.clear();
        for (int i = 0; i<images.size(); i++){
            imageList.add(images.get(i));
        }
        getItemClickValue(userInfo);
    }

    @Override
    public void showErrorResponse(String error) {
        removeProgress();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        List<Venue> newList = new ArrayList<>();
        for (Venue item : userInfo){
            String name = item.getName().toLowerCase();
            if (name.contains(newText))
                newList.add(item);
        }
        itemListAdapterSearch.searchFromName(newList);
        detailsListRecycler.setAdapter(itemListAdapterSearch);

        return true;
    }
}
