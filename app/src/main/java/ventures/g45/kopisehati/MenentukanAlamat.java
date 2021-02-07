package ventures.g45.kopisehati;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenentukanAlamat extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveCanceledListener {


    Typeface MontserratRegular;
    TextView txtCari, txtAlamat, txtDetailAlamat, txtTambahCatatan, txtSimpanAlamat, txtNama, txtNomor;
    Button btnKirim, btnSimpan;
    LatLng placeLatLng;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Marker myMarker;
    MarkerOptions marker;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    final String[] sAlamat = new String[1];
    Geocoder geocoder;
    Double originLat, originLng;
    String curLocation, koordinat, sCatatan, sKategori, dari, detailAlamat, noHp, nama, defaultUrl, dataUrl, urlSimpanAlamat;
    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    double latitude, longtitude, titikLatitude, titikLongtitude;
    ImageView icBack;
    Integer id_alamat;
    private BottomSheetBehavior bottomSheetBehavior;
    ImageView scroll;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    LinearLayout blockDetailInformasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menentukan_alamat);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longtitude = intent.getDoubleExtra("longtitude", 0);
        sCatatan = intent.getStringExtra("catatan");
        sKategori = intent.getStringExtra("kategori");
        id_alamat = intent.getIntExtra("id_alamat", 0);
        detailAlamat = intent.getStringExtra("detailAlamat");

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlSimpanAlamat = defaultUrl + "getsimpanalamat.html";

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        dari = sharedPreferences.getString("dari", "");
        noHp = sharedPreferences.getString("noHp", "");
        nama = sharedPreferences.getString("nama", "");
        if (latitude == 0) {
            latitude = Double.parseDouble(sharedPreferences.getString("latitude", ""));
            longtitude = Double.parseDouble(sharedPreferences.getString("longtitude", ""));
        }
        placeLatLng = new LatLng(latitude, longtitude);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

        txtCari = findViewById(R.id.txtCari);
        txtCari.setTypeface(MontserratRegular);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtAlamat.setTypeface(MontserratRegular);
        txtDetailAlamat = findViewById(R.id.txtDetailAlamat);
        txtDetailAlamat.setTypeface(MontserratRegular);
        btnKirim = findViewById(R.id.btnKirim);
        btnKirim.setTypeface(MontserratRegular);
        icBack = findViewById(R.id.icBack);
        scroll = findViewById(R.id.scroll);
        bottomSheetBehavior = BottomSheetBehavior.from((FrameLayout)findViewById(R.id.blockAlamat));
        bottomSheetBehavior.setPeekHeight(400);

        txtTambahCatatan = findViewById(R.id.txtTambahCatatan);
        txtTambahCatatan.setTypeface(MontserratRegular);
        txtSimpanAlamat = findViewById(R.id.txtSimpanAlamat);
        txtSimpanAlamat.setTypeface(MontserratRegular);
        txtNama = findViewById(R.id.txtNama);
        txtNama.setTypeface(MontserratRegular);
        txtNomor = findViewById(R.id.txtNomor);
        txtNomor.setTypeface(MontserratRegular);
        blockDetailInformasi = findViewById(R.id.blockDetailInformasi);

        txtAlamat.setText(detailAlamat);
        txtNomor.setText(noHp);
        txtNama.setText(nama);

        if (sCatatan != null){
            txtTambahCatatan.setText(sCatatan);
        }if (sKategori != null){
            txtSimpanAlamat.setText(sKategori);
        }

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        scroll.setImageDrawable(getDrawable(R.drawable.down));
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        scroll.setImageDrawable(getDrawable(R.drawable.up));
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        if (dari.equals("alamat")){
            btnKirim.setText("Simpan Alamat");
            btnKirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SaveAlamat().execute();
                }
            });
        }else{
            blockDetailInformasi.setVisibility(View.GONE);
            btnKirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kirimKe();
                }
            });
        }

        txtCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPencarian();
            }
        });


        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        openMap();
    }

    public void showPencarian(){
        dialog = new AlertDialog.Builder(MenentukanAlamat.this, R.style.AddToCart);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_search_address, null);
        dialog.setView(dialogView);
        final AlertDialog alertDialog = dialog.create();
        TextView txtTitleInputAlamat = dialogView.findViewById(R.id.txtTitleInputAlamat);
        txtTitleInputAlamat.setTypeface(MontserratRegular);
        final EditText inpAlamat = dialogView.findViewById(R.id.inpAlamat);
        inpAlamat.setTypeface(MontserratRegular);
        inpAlamat.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        inpAlamat.setSingleLine(true);
        Button btnSearchAddress = dialogView.findViewById(R.id.btnSearchAddress);
        btnSearchAddress.setTypeface(MontserratRegular);
        ImageView icoClose = dialogView.findViewById(R.id.icoClose);
        icoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sAlamat[0] = inpAlamat.getText().toString();
                if (sAlamat[0].isEmpty()) {
                    inpAlamat.setError("Silahkan isi alamat lengkap anda!");
                } else {
                    alertDialog.dismiss();
                    AddAlamat();
                }
            }
        });

        inpAlamat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    sAlamat[0] = inpAlamat.getText().toString();
                    if (sAlamat[0].isEmpty()) {
                        inpAlamat.setError("Silahkan isi alamat lengkap anda!");
                    } else {
                        alertDialog.dismiss();
                        AddAlamat();
                    }
                }
                return false;
            }
        });

        alertDialog.show();
    }

    public void AddAlamat() {
        if (!sAlamat[0].isEmpty()) {
            geocoder = new Geocoder(MenentukanAlamat.this);
            try {
                List<Address> addressList = geocoder.getFromLocationName(sAlamat[0] + ", Yogyakarta", 1);
                if (addressList.size() > 0) {
                    originLat = addressList.get(0).getLatitude();
                    originLng = addressList.get(0).getLongitude();
                    curLocation = addressList.get(0).getAddressLine(0);
                    LatLng lokasi = new LatLng(originLat, originLng);

                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 18));

                    txtAlamat.setText(curLocation);
                } else {
                    AlertDialog.Builder alertDialogBuilder;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        alertDialogBuilder = new AlertDialog.Builder(MenentukanAlamat.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        alertDialogBuilder = new AlertDialog.Builder(MenentukanAlamat.this);
                        alertDialogBuilder
                                .setMessage("Alamat anda tidak dapat ditampilkan di peta. Silahkan cek kembali atau pilih melalui peta.")
                                .setCancelable(true)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//       AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("ID").build();
//
//        try {
//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                    .setFilter(typeFilter)
//                    .build(this);
//            startActivityForResult(intent, 0);
//        } catch (GooglePlayServicesRepairableException e){
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//
//            Place placeData = PlaceAutocomplete.getPlace(this, data);
//
//            if (placeData.isDataValid()) {
//                // Show in Log Cat
//                Log.d("autoCompletePlace Data", placeData.toString());
//
//                // Dapatkan Detail
//                String placeAddress = placeData.getAddress().toString();
//                placeLatLng = placeData.getLatLng();
//                String placeName = placeData.getName().toString();
//
//                txtCari.setText(placeName);
//                txtAlamat.setText(placeName);
//                txtDetailAlamat.setText(placeAddress);
//
//                openMap();
//
//            } else {
//                // Data tempat tidak valid
//                Toast.makeText(this, "Invalid Place !", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public void openMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                placeLatLng = latLng;
                if (myMarker == null){
                    marker = new MarkerOptions().position(placeLatLng).title("Alamat kamu");
                    myMarker = mMap.addMarker(marker);
                }
                else{
                    myMarker.setPosition(placeLatLng);
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 18.0f));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 200, null);
            }
        });

        //Memulai Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        marker = new MarkerOptions().position(placeLatLng).title("Alamat Kamu");
        myMarker = mMap.addMarker(marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 18.0f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 200, null);

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onCameraMoveStarted(int reason) {

//        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
//            Toast.makeText(this, "The user gestured on the map.",
//                    Toast.LENGTH_SHORT).show();
//        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
//            Toast.makeText(this, "The user tapped something on the map.",
//                    Toast.LENGTH_SHORT).show();
//        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
//            Toast.makeText(this, "The app moved the camera.",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onCameraMove() {
//        Toast.makeText(this, "The camera is moving.",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMoveCanceled() {
//        Toast.makeText(this, "Camera movement canceled.",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mMap.getCameraPosition().target;
        Geocoder geocoder = new Geocoder(MenentukanAlamat.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                placeLatLng = new LatLng(latLng.latitude, latLng.longitude);
                myMarker.setPosition(placeLatLng);
                String alamat = addresses.get(0).getAddressLine(0);
                String kota = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String negara = addresses.get(0).getCountryName();
                String kodepos = addresses.get(0).getPostalCode();
                String namaarea = addresses.get(0).getThoroughfare();
                String nomor = addresses.get(0).getFeatureName();
                txtAlamat.setText(nomor + ", " + namaarea);
                txtDetailAlamat.setText(alamat);
//                curLocation = addressList.get(0).getAddressLine(0);
//                mCurrentLocation.setLatitude(latLng.latitude);
//                mCurrentLocation.setLongitude(latLng.longitude);
//                addresses = addressList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(this, "The camera has stopped moving.",
//                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Izin ditolak.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void kirimKe(){
        Intent intent = new Intent(getApplicationContext(), SimpanAlamat.class);
        String detailAlamat = txtDetailAlamat.getText().toString();
        intent.putExtra("detailAlamat", detailAlamat);
        intent.putExtra("titik", placeLatLng);
        intent.putExtra("alamat", txtAlamat.getText().toString());
        if (sCatatan != null){
            intent.putExtra("catatan",sCatatan);
        }if (sKategori != null){
            intent.putExtra("kategori",sKategori);
        }if (id_alamat != 0){
            intent.putExtra("id_alamat", id_alamat);
        }
        startActivity(intent);
    }

    private class SaveAlamat extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MenentukanAlamat.this);
            pDialog.setMessage("Mengirim data...");
            pDialog.setCancelable(true);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();

            titikLatitude = placeLatLng.latitude;
            titikLongtitude = placeLatLng.longitude;

            String detailAlamat = txtDetailAlamat.getText().toString();

            params.add(new BasicNameValuePair("id_alamat", id_alamat.toString()));
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("latitude", String.valueOf(titikLatitude)));
            params.add(new BasicNameValuePair("longtitude", String.valueOf(titikLongtitude)));
            params.add(new BasicNameValuePair("alamat", detailAlamat));
            params.add(new BasicNameValuePair("catatan", txtTambahCatatan.getText().toString()));
            params.add(new BasicNameValuePair("kategori", txtSimpanAlamat.getText().toString()));
            params.add(new BasicNameValuePair("hapus", "no"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlSimpanAlamat, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null){
                    if (result.getInt("error") == 1){

                    }else {
                        Intent intent = new Intent(getApplicationContext(), DaftarAlamat.class);
                        startActivity(intent);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
