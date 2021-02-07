package ventures.g45.kopisehati;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SimpanAlamat extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Typeface MontserratRegular;
    TextView txtTitle, txtDetailInformasi, txtAlamat,  txtNama, txtNomor;
    EditText txtTambahCatatan, txtSimpanAlamat;
    Button btnSimpan;
    LinearLayout blockAlamat;
    String detailAlamat, nama, noHp, defaultUrl, dataUrl, urlSimpanAlamat, alamat, urlHitungJarak, sCatatan, sKategori, sHapus;
    LatLng titik;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    double titikLatitude, titikLongtitude, lat_outlet, long_outlet;
    ImageView icBack;
    String dari;
    Integer id_alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_alamat);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        noHp = sharedPreferences.getString("noHp", "");
        nama = sharedPreferences.getString("nama", "");
        dari = sharedPreferences.getString("dari", "");
        lat_outlet = Double.parseDouble(sharedPreferences.getString("lat_outlet", "0"));
        long_outlet = Double.parseDouble(sharedPreferences.getString("long_outlet", "0"));


        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlSimpanAlamat = defaultUrl + "getsimpanalamat.html";
        urlHitungJarak = defaultUrl + "getjarak.html";

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

        Intent intent = getIntent();
        detailAlamat = intent.getStringExtra("detailAlamat");
        titik = intent.getExtras().getParcelable("titik");
        // Log.d("titik", titik.toString());
        sCatatan = intent.getStringExtra("catatan");
        sKategori = intent.getStringExtra("kategori");
        //alamat = intent.getStringExtra("alamat");
        id_alamat = intent.getIntExtra("id_alamat", 0);

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        txtDetailInformasi = findViewById(R.id.txtDetailInformasi);
        txtDetailInformasi.setTypeface(MontserratRegular);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtAlamat.setTypeface(MontserratRegular);
        txtTambahCatatan = findViewById(R.id.txtTambahCatatan);
        txtTambahCatatan.setTypeface(MontserratRegular);
        txtSimpanAlamat = findViewById(R.id.txtSimpanAlamat);
        txtSimpanAlamat.setTypeface(MontserratRegular);
        txtNama = findViewById(R.id.txtNama);
        txtNama.setTypeface(MontserratRegular);
        txtNomor = findViewById(R.id.txtNomor);
        txtNomor.setTypeface(MontserratRegular);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setTypeface(MontserratRegular);
        blockAlamat = findViewById(R.id.blockAlamat);
        icBack = findViewById(R.id.icBack);

        txtAlamat.setText(detailAlamat);
        txtNomor.setText(noHp);
        txtNama.setText(nama);

        if (sCatatan != null){
            txtTambahCatatan.setText(sCatatan);
        }if (sKategori != null){
            txtSimpanAlamat.setText(sKategori);
        }

        blockAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenentukanAlamat.class);
                startActivity(intent);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveAlamat().execute();
            }
        });

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        MarkerOptions marker = new MarkerOptions().position(titik).title("Alamat Kamu");

// adding marker
        mMap.addMarker(marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(titik, 18.0f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 200, null);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(getApplicationContext(), MenentukanAlamat.class);
                intent.putExtra("latitude", titik.latitude);
                intent.putExtra("longtitude", titik.longitude);

                if (id_alamat != 0) {
                    intent.putExtra("id_alamat", id_alamat);
                }
                if (txtTambahCatatan.getText().toString() != null){
                    intent.putExtra("catatan",txtTambahCatatan.getText().toString());
                }if (txtSimpanAlamat.getText().toString() != null){
                    intent.putExtra("kategori",txtSimpanAlamat.getText().toString());
                }
                startActivity(intent);
            }
        });

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
////        mLastLocation = location;
////        if (mCurrLocationMarker != null) {
////            mCurrLocationMarker.remove();
////        }
////        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(posisiKurir).title("Posisi Kurir"));
//
//        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
//        latLongBuilder.include(posisiKurir);
//
//        LatLngBounds bounds = latLongBuilder.build();
//
//        int width = getResources().getDisplayMetrics().widthPixels;
//        int height = getResources().getDisplayMetrics().heightPixels;
//        int paddingMap = (int) (width * 0.2);
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
//        mMap.animateCamera(cu);
//
//            }
//        });

        //drawPolylines();

//        CameraPosition cameraPosition = new CameraPosition.Builder().target(posisiKurir).zoom(16).build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

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

    private class SaveAlamat extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SimpanAlamat.this);
            pDialog.setMessage("Mengirim data...");
            pDialog.setCancelable(true);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();

            titikLatitude = titik.latitude;
            titikLongtitude = titik.longitude;


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
                        Intent intent = new Intent(getApplicationContext(), DaftarAlamat.class);
                        startActivity(intent);
                    }else {
                        id_alamat = result.getInt("id");
                        new hitungjarak().execute();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class hitungjarak extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("lat_alamat", String.valueOf(titikLatitude)));
            params.add(new BasicNameValuePair("long_alamat", String.valueOf(titikLongtitude)));
            params.add(new BasicNameValuePair("lat_outlet", String.valueOf(lat_outlet)));
            params.add(new BasicNameValuePair("long_outlet", String.valueOf(long_outlet)));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlHitungJarak, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent;
                        if (dari.equals("menu")) {
                            intent = new Intent(getApplicationContext(), Checkout.class);
                        }else if (dari.equals("alamat")){
                            intent = new Intent(getApplicationContext(), DaftarAlamat.class);
                        }
                        else {
                            intent = new Intent(getApplicationContext(), Berlangganan.class);
                        }
                        intent.putExtra("pesanan", 1);
                        editor.putString("nama_alamat", txtSimpanAlamat.getText().toString());
                        editor.putInt("id_alamat", id_alamat);
                        editor.putString("alamat", detailAlamat);
                        editor.putString("lat_alamat", String.valueOf(titikLatitude));
                        double jarak = Math.ceil(Float.parseFloat(result.getString("jarak")));
                        Log.d("simpan", String.valueOf(jarak));
                        editor.putInt("jarak", (int) jarak);
                        editor.putString("long_alamat", String.valueOf(titikLongtitude));
                        editor.apply();
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
