package ventures.g45.kopisehati;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.database.core.Context;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonIOException;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Typeface MontserratRegular, MontserratLight, MontserratThin;
    TextView txt_greetings, txtTMotiv, txtEspresso, txtSingleOrigin, txtJuiceSmoothies, txtNonCoffee, txtSnack, txtOthers, txtHome, txtOrders, txtInbox, txtAccount, txtAlamat, txtCart, txtInfo;
    LinearLayout home, orders, inbox, account, espresso, singleOrigin, juiceSmoothies, nonCoffee, snacks, others, blockKategoriMenu, blockAlamat, blockOutlet;
    Intent a, b, c, d, e, f, g, h, i, j;
    ImageView ic_greetings, blockBanner, icCart, icFav;
    SimpleDateFormat timeFormat;
    String waktu, defaultUrl, urlGetDataHome, token, dataUrl, detailAlamat, urlKategoriMenu, noHp, urlGetOutlet, pesanan="2", nama_user, checkout, urlCekVersi, urlDeleteTempCart, urlHitungJarak, lokasi_outlet;
    Integer id, id_outlet, qty_cart =0, versi;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private LocationTrack locationTrack;
    Double latitude, longtitude;
    double[] lat_outlet, long_outlet;
    LatLng lokasiku;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    List<Address> addresses;
    float factor;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    AlertDialog alertDialog;
    ScrollView scrollView;
    ProgressBar pBar;
    private static final String TAG = "MainActivity";
    private ViewFlipper mViewFliper;
    ViewFlipper slider;
    private GestureDetectorCompat mDetector;
    private MainActivity ctx;
    AppUpdateManager mAppUpdateManager;
    private InstallStateUpdatedListener installStateupdateListener;
    private int kirim = 0;
    Float distance= Float.valueOf(0);
    Integer hasil = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        id_outlet = sharedPreferences.getInt("id_outlet", 0);
        token = sharedPreferences.getString("token", "");
        noHp = sharedPreferences.getString("noHp", "");
        nama_user = sharedPreferences.getString("nama", "");
        Log.d("nama", nama_user);
        versi = sharedPreferences.getInt("versi", 1);
        distance = sharedPreferences.getFloat("jarak2", 0);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        MontserratLight = (((KopiSehati) getApplication()).getMontserratRegular());
        MontserratThin = ((KopiSehati) getApplication()).getMontserratThin();

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlGetDataHome = defaultUrl + "gethomedata.html";
        urlKategoriMenu = defaultUrl + "getkategorimenu.html";
        urlGetOutlet = defaultUrl + "getdataoutlet.html";
        urlCekVersi = defaultUrl + "versioncheck.html";
        urlDeleteTempCart = defaultUrl + "deletetemp.html";
        urlHitungJarak = defaultUrl + "getjarak.html";

        factor = getResources().getDisplayMetrics().density;

        timeFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar now = Calendar.getInstance();
        now.get(Calendar.DATE + Calendar.MONTH + Calendar.YEAR);
        waktu = timeFormat.format(now.getTime());

        checkFirstRun();

        txt_greetings = (TextView) findViewById(R.id.txt_greetings);
        txt_greetings.setTypeface(MontserratRegular);
        txtEspresso = findViewById(R.id.txtEspresso);
        txtEspresso.setTypeface(MontserratRegular);
        txtSingleOrigin = findViewById(R.id.txtSingleOrigin);
        txtSingleOrigin.setTypeface(MontserratRegular);
        txtJuiceSmoothies = findViewById(R.id.txtJuiceSmoothies);
        txtJuiceSmoothies.setTypeface(MontserratRegular);
        txtNonCoffee = findViewById(R.id.txtNonCoffee);
        txtNonCoffee.setTypeface(MontserratRegular);
        txtSnack = findViewById(R.id.txtSnacks);
        txtSnack.setTypeface(MontserratRegular);
        txtOthers = findViewById(R.id.txtOthers);
        txtOthers.setTypeface(MontserratRegular);
        txtHome = findViewById(R.id.txtHome);
        txtHome.setTypeface(MontserratRegular);
        txtOrders = findViewById(R.id.txtOrders);
        txtOrders.setTypeface(MontserratRegular);
        txtInbox = findViewById(R.id.txtInbox);
        txtInbox.setTypeface(MontserratRegular);
        txtAccount = findViewById(R.id.txtAccount);
        txtAccount.setTypeface(MontserratRegular);
        home = findViewById(R.id.home);
        home.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);
        inbox = findViewById(R.id.inbox);
        inbox.setOnClickListener(this);
        account = findViewById(R.id.account);
        account.setOnClickListener(this);
        ic_greetings = findViewById(R.id.ic_greetings);
        espresso = findViewById(R.id.espresso);
        espresso.setOnClickListener(this);
        singleOrigin = findViewById(R.id.singleOrigin);
        singleOrigin.setOnClickListener(this);
        juiceSmoothies = findViewById(R.id.juiceSmoothies);
        juiceSmoothies.setOnClickListener(this);
        nonCoffee = findViewById(R.id.nonCoffee);
        nonCoffee.setOnClickListener(this);
        snacks = findViewById(R.id.snacks);
        snacks.setOnClickListener(this);
        others = findViewById(R.id.others);
        others.setOnClickListener(this);
        blockKategoriMenu = findViewById(R.id.blockKategoriMenu);
        blockAlamat = findViewById(R.id.blockAlamat);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtAlamat.setTypeface(MontserratRegular);
        blockBanner = findViewById(R.id.blockBanner);
        scrollView = findViewById(R.id.scrollView);
        icCart = findViewById(R.id.ic_cart);
        icFav = findViewById(R.id.ic_fav);
        txtAccount = findViewById(R.id.txtAccount);
        txtCart = findViewById(R.id.txtCart);
        txtInfo = findViewById(R.id.txtInfo);
        txtInfo.setTypeface(MontserratRegular);
        slider = (ViewFlipper) findViewById(R.id.slider);

        blockBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noHp.isEmpty()) {
                    Intent a = new Intent(MainActivity.this, SignIn.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a);
                }else {
                    Intent intent = new Intent(getApplicationContext(), DaftarPaket.class);
                    startActivity(intent);
                }
            }
        });

        blockAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogPutih);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.popup_pilih_outlet, null);
                dialog.setView(dialogView);
                blockOutlet = dialogView.findViewById(R.id.blockOutlet);
                pBar = dialogView.findViewById(R.id.pBar);
                alertDialog = dialog.create();

                new GetOutlet().execute();

                alertDialog.show();
            }
        });

        icCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noHp.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else {
                    if (qty_cart == 0){
                        Intent intent = new Intent(getApplicationContext(), KeranjangBelanja.class);
                        startActivity(intent);
                    }else {
                        if (checkout.equals("1")){
                            Intent intent = new Intent(getApplicationContext(), Checkout.class);
                            intent.putExtra("asal", 1);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(),Berlangganan.class);
                            intent.putExtra("asal", 1);
                            startActivity(intent);
                        }

                    }

                }
            }
        });

        icFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noHp.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), Favorite.class);
                    startActivity(intent);
                }
            }
        });

//        mAppUpdateManager = AppUpdateManagerFactory.create(this);
//        mAppUpdateManager.registerListener(installStateupdateListener);
//        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(AppUpdateInfo -> {
//            if (AppUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && AppUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
//
//                try {
//                    mAppUpdateManager.startUpdateFlowForResult(
//                            AppUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, RC_APP_UPDATE);
//                } catch (IntentSender.SendIntentException e) {
//                e.printStackTrace();
//            }
//
//        } else if (AppUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
//            popupSnackbarForCompleteUpdate();
//        } else {
//            Log.e(TAG, "checkForAppUpdateAvailability: something else");
//        }
//
//        });
//
//        installStateupdateListener = state -> {
//            if (state.installStatus() == InstallStatus.DOWNLOADED){
//                popupSnackbarForCompleteUpdate();
//            }else if (state.installStatus() == InstallStatus.INSTALLED) {
//                if (mAppUpdateManager != null){
//                    mAppUpdateManager.unregisterListener(installStateupdateListener);
//                }
//            }else {
//                Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
//            }
//        };

    }

//    private void popupSnackbarForCompleteUpdate() {
//       Toast.makeText(getApplicationContext(), "Update Success!", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_APP_UPDATE) {
//            if (resultCode != RESULT_OK) {
//                Log.e(TAG, "onActivityResult: app download failed");
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
        System.exit(0);
    }

    public void checkFirstRun(){
        boolean isFirstRun = getSharedPreferences("kopisehati", 0).getBoolean("isFirstRun", true);
        if (isFirstRun){
            // Place your dialog code here to display the dialog
            //Toast.makeText(getApplicationContext(), "Firs Run", Toast.LENGTH_LONG).show();
            try {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            getSharedPreferences("kopisehati", 0)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
        else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(getApplicationContext(), "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void getLocation(){
        locationTrack = new LocationTrack(MainActivity.this);
        if(locationTrack.canGetLocation()) {
            latitude = locationTrack.getLatitude();
            longtitude = locationTrack.getLongitude();
            lokasiku = new LatLng(latitude, longtitude);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                String alamat = addresses.get(0).getAddressLine(0);
                String kota = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String negara = addresses.get(0).getCountryName();
                String kodepos = addresses.get(0).getPostalCode();
                String namaarea = addresses.get(0).getFeatureName();

                detailAlamat = namaarea + ", " + alamat + " " + kota + " " + state + " " + negara + " " + kodepos;
            } catch (Exception e){
                e.printStackTrace();
            }
            editor.putString("latitude", latitude.toString());
            editor.putString("longtitude", longtitude.toString());
            editor.putString("lokasiku", lokasiku.toString());
            editor.putString("detailAlamat", detailAlamat);
            editor.apply();

            new GetDataHome().execute();
            //Toast.makeText(getApplicationContext(), "My Current location: " + locationTrack.getLatitude() + " Long : " + locationTrack.getLongitude(), Toast.LENGTH_LONG).show();
        }
        else{
            locationTrack.showSettingsAlert();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                if (noHp.isEmpty()) {
                    a = new Intent(this, SignIn.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a);
                } else {
                    scrollView.smoothScrollTo(0, 0); }
                break;
            case R.id.orders:
                if (noHp.isEmpty()) {
                    b = new Intent(this, SignIn.class);;
                } else {
                    b = new Intent(this, Orders.class);
                }
                b.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(b);
                break;
            case  R.id.inbox:
                if (noHp.isEmpty()){
                    c = new Intent(this, SignIn.class);
                } else {
                    c = new Intent(this, Inbox.class); }
                c.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(c);
                break;
            case R.id.account:
                if (noHp.isEmpty()) {
                    d = new Intent(this, SignIn.class);
                    startActivity(d);
                    d.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }else { d = new Intent(this,Account.class);
                    d.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(d);}

                break;
            case R.id.espresso :
                if (noHp.isEmpty()) {
                    e = new Intent(this, SignIn.class);
                    startActivity(e);
                    e.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }else {
                    id = 1;
                    e = new Intent(getApplicationContext(), DaftarMenu.class);
                    editor.putInt("id_kategori", Integer.valueOf(id));
                    editor.putInt("click", Integer.valueOf(id) - 1);
                    editor.apply();
                    e.putExtra("from", "0");
                    startActivity(e);
                }
                break;
            case R.id.singleOrigin :
                if (noHp.isEmpty()) {
                    f = new Intent(this, SignIn.class);
                    startActivity(f);
                    f.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }else {
                    id = 2;
                    f = new Intent(getApplicationContext(), DaftarMenu.class);
                    editor.putInt("id_kategori", Integer.valueOf(id));
                    editor.putInt("click", Integer.valueOf(id) - 1);
                    editor.apply();
                    f.putExtra("from", "0");
                    startActivity(f);
                }
                break;
            case R.id.juiceSmoothies :
                if (noHp.isEmpty()) {
                    g = new Intent(this, SignIn.class);
                    startActivity(g);
                    g.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }else {
                    id = 3;
                    g = new Intent(getApplicationContext(), DaftarMenu.class);
                    editor.putInt("id_kategori", Integer.valueOf(id));
                    editor.putInt("click", Integer.valueOf(id) - 1);
                    editor.apply();
                    g.putExtra("from", "0");
                    startActivity(g);
                }
                break;
            case R.id.nonCoffee :
                if (noHp.isEmpty()) {
                    h = new Intent(this, SignIn.class);
                    startActivity(h);
                    h.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }else {
                    id = 4;
                    h = new Intent(getApplicationContext(), DaftarMenu.class);
                    h.putExtra("id_outlet", id_outlet);
                    editor.putInt("id_kategori", Integer.valueOf(id));
                    editor.putInt("click", Integer.valueOf(id) - 1);
                    editor.apply();
                    h.putExtra("from", "0");
                    startActivity(h);
                }
                break;
            case R.id.snacks :
                if (noHp.isEmpty()) {
                    i = new Intent(this, SignIn.class);
                    startActivity(i);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }else {
                    id = 5;
                    i = new Intent(getApplicationContext(), DaftarMenu.class);
                    editor.putInt("id_kategori", Integer.valueOf(id));
                    editor.putInt("click", Integer.valueOf(id) - 1);
                    editor.apply();
                    i.putExtra("from", "0");
                    startActivity(i);
                }
                break;
            case R.id.others:
                if (noHp.isEmpty()) {
                    j = new Intent(this, SignIn.class);
                    startActivity(j);
                    j.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }else {
                    id = 6;
                    j = new Intent(getApplicationContext(), DaftarMenu.class);
                    editor.putInt("id_kategori", Integer.valueOf(id));
                    editor.putInt("click", Integer.valueOf(id) - 1);
                    editor.apply();
                    j.putExtra("from", "0");
                    startActivity(j);
                }
                break;
        }
    }

    private class GetDataHome extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("waktu", waktu));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject json = jsonParser.makeHttpRequest(urlGetDataHome, "POST", params);

            return json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Mengambil data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            try {
                if (result != null) {
                    if (result.getInt("error") == 1) {
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    } else {
                        if (noHp.isEmpty()){
                            txtAccount.setText("Login");
                            txt_greetings.setText(result.getString("salam"));
                        }else {
                            String[] nama_depan = nama_user.split(" ");
                            txtAccount.setText("Account");
                            txt_greetings.setText("Hi, " + nama_depan[0]);
                        }
                        Picasso.get().load(dataUrl + "greetings/" + result.getString("icon_greeting")).into(ic_greetings);

                        JSONArray daftar_slider = new JSONArray(result.getString("slider"));
                        for (int l = 0; l < daftar_slider.length(); l++) {

                            final JSONObject sliderr = daftar_slider.getJSONObject(l);

                            ImageView thumbnail = new ImageView(MainActivity.this);
                            Picasso.get().load(dataUrl + "slide/" + sliderr.getString("image")).into(thumbnail);
                            //thumbnail.setPadding((int)(1*factor), (int)(15*factor), (int)(1*factor), (int)(1*factor));
                            //thumbnail.setAdjustViewBounds(true);

                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (380 * factor));
                            //layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                            thumbnail.setLayoutParams(layoutParams);

                            slider.addView(thumbnail);
                        }

                        if (!noHp.isEmpty()) {
                            qty_cart = result.getInt("qty_cart");
                            if (qty_cart > 0) {
                                editor.putInt("qty_cart", qty_cart).apply();
                                txtCart.setVisibility(View.VISIBLE);
                                txtCart.setText(result.getString("qty_cart"));

                            }
                        }

                        checkout = result.getString("cek");

                        if (id_outlet.equals(0)) {
                            JSONArray daftarOutlet = new JSONArray(result.getString("outlet"));
                            if (daftarOutlet.length() > 0) {

                                Float outlet[] = new Float[daftarOutlet.length()];
                                long_outlet = new double[daftarOutlet.length()];
                                lat_outlet = new double[daftarOutlet.length()];

                                for (int i = 0; i < daftarOutlet.length(); i++) {

                                    final JSONObject menu = daftarOutlet.getJSONObject(i);
                                    String titik = menu.getString("koordinat");
                                    String[] koordinat = titik.split(",");
                                    double titikLatitude = Double.parseDouble(koordinat[0]);
                                    double titikLongtitude = Double.parseDouble(koordinat[1]);
                                    float[] hasil = new float[1];
                                    Location.distanceBetween(titikLatitude, titikLongtitude, latitude, longtitude, hasil);
                                    Log.d("Result", Float.valueOf(hasil[0]).toString());
                                    outlet[i] = hasil[0];

                                    new hitungjarak().execute(String.valueOf(titikLatitude), String.valueOf(titikLongtitude));
                                }

//
                                Float terdekat = outlet[0];

                                for (int j = 0; j < outlet.length; j++) {

                                    if (outlet[j] <= terdekat) {
                                        final JSONObject dekat = daftarOutlet.getJSONObject(j);
                                        terdekat = outlet[j];
                                        Float pembulatan = outlet[j] / 1000;
                                        //txtAlamat.setText(dekat.getString("lokasi") + " " + decimalFormat.format(pembulatan) + " KM");
                                        //txtTMotiv.setText("Outlet terdekat dengan kamu " + dekat.getString("nama") + " di " + dekat.getString("lokasi") + ", " + dekat.getString("alamat"));
                                        lokasi_outlet = dekat.getString("lokasi");
                                        id_outlet = dekat.getInt("id");
                                        editor.putInt("id_outlet", id_outlet);
                                        editor.apply();

                                    }
//                                    Log.d("Terdekat", Float.valueOf(terdekat).toString());
                                }
                            }
                        }else {
                            txtAlamat.setText(result.getString("lokasi") + " " + decimalFormat.format(distance) + " KM");

                            new cekversi().execute();
                        }



                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class hitungjarak extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("lat_alamat", String.valueOf(latitude)));
            params.add(new BasicNameValuePair("long_alamat", String.valueOf(longtitude)));
            params.add(new BasicNameValuePair("lat_outlet", args[0]));
            params.add(new BasicNameValuePair("long_outlet", args[1]));

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
                        if (distance < Float.parseFloat(result.getString("jarak"))){
                            Log.d("distance", distance.toString());
                            txtAlamat.setText(lokasi_outlet + " " + decimalFormat.format(distance) + " KM");
                            editor.putFloat("jarak2", distance);
                            editor.apply();
                        }distance = Float.parseFloat(result.getString("jarak"));

                        if ((pDialog != null) && (pDialog.isShowing()))
                            pDialog.dismiss();
                        pDialog = null;

                        new cekversi().execute();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class GetKategoriMenu extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlKategoriMenu, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    } else {
                        JSONArray kategoriMenu = new JSONArray(result.getString("menu"));
                        int baris = (int) Math.ceil(kategoriMenu.length()/3);
                        Log.d("baris", Integer.toString(baris));
                        int k = 0;
                        int i = 0;
                        if (kategoriMenu.length() > 0) {
                            while (i < baris){
                                LinearLayout linearLayoutProduk = new LinearLayout(MainActivity.this);
                                linearLayoutProduk.setWeightSum(3);

                                for (int j = 0; j < 3; j++) {

                                    final JSONObject menu = kategoriMenu.getJSONObject(k);

                                    LinearLayout linearLayoutKiri = new LinearLayout(MainActivity.this);
                                    linearLayoutKiri.setOrientation(LinearLayout.VERTICAL);
                                    linearLayoutKiri.setId(View.generateViewId());
                                    linearLayoutKiri.setBackground(getResources().getDrawable(R.drawable.background_border));

                                    LinearLayout.LayoutParams layoutParamsKiri = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                                    if (j == 0) {
                                        layoutParamsKiri.setMargins((int) (0 * factor), 0, (int) (0 * factor), (int) (10 * factor));
                                    } else {
                                        layoutParamsKiri.setMargins((int) (10 * factor), 0, (int) (0 * factor), (int) (10 * factor));
                                    }
                                    linearLayoutKiri.setLayoutParams(layoutParamsKiri);

                                    ImageView thumbnail = new ImageView(MainActivity.this);
                                    Picasso.get().load(dataUrl + "kategori/" + menu.getString("thumbnail")).into(thumbnail);
                                    thumbnail.setPadding((int)(1*factor), (int)(15*factor), (int)(1*factor), (int)(1*factor));
                                    thumbnail.setAdjustViewBounds(true);

                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (60 * factor));
                                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                                    thumbnail.setLayoutParams(layoutParams);

                                    linearLayoutKiri.addView(thumbnail);

                                    ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (60 * factor));

                                    TextView nama = new TextView(MainActivity.this);
                                    nama.setLayoutParams(layoutNama);
                                    nama.setPadding((int)(10*factor), (int)(15*factor), (int)(10*factor), (int)(10*factor));
                                    nama.setTypeface(MontserratRegular);
                                    nama.setTextSize(14);
                                    nama.setText(menu.getString("nama"));
                                    nama.setMaxLines(2);
                                    nama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    nama.setEllipsize(TextUtils.TruncateAt.END);
                                    nama.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    linearLayoutKiri.addView(nama);

                                    linearLayoutKiri.setOnClickListener(new KategoriMenu(menu.getString("id"), "dipilih"));

                                    linearLayoutProduk.addView(linearLayoutKiri);

                                    k++;
                                }

                                i++;

                                // blockKategoriMenu.addView(linearLayoutProduk);
                            }
                        }
                    }
                }
                else {
                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class KategoriMenu implements View.OnClickListener {

        String id, kat;
        public KategoriMenu(String id, String kat) {
            this.id = id;
            this.kat = kat;
        }

        @Override
        public void onClick(View v) {
            if (noHp.isEmpty()){
                Intent intent =new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(MainActivity.this, DaftarMenu.class);
                editor.putInt("id_kategori", Integer.valueOf(id));
                editor.putInt("click", Integer.valueOf(id) - 1);
                editor.apply();
                intent.putExtra("from", "0");
                Log.d("id", id);
                startActivity(intent);}
        }
    }

    private class GetOutlet extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            String lokasi = latitude.toString();
            Log.d("lokasi", lokasi);

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("lat", latitude.toString()));
            params.add(new BasicNameValuePair("long", longtitude.toString()));
            params.add(new BasicNameValuePair("pesanan", pesanan));


            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetOutlet, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            pBar.setVisibility(View.GONE);

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    }
                    else {
                        JSONArray daftar = new JSONArray(result.getString("list"));
                        if (daftar.length() > 0){

                            LinearLayout linearLayout= new LinearLayout(MainActivity.this);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setId(View.generateViewId());

                            for (int i=0; i < daftar.length(); i++){

                                final JSONObject outlet = daftar.getJSONObject(i);

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                                layoutParams.setMargins((int) (20 * factor), 0, (int) (15 * factor), (int) (15 * factor));
                                linearLayout.setLayoutParams(layoutParams);

                                ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView nama= new TextView(MainActivity.this);
                                nama.setLayoutParams(layoutNama);
                                nama.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), 0);
                                nama.setTextSize(16);
                                nama.setAllCaps(true);
                                nama.setText(outlet.getString("nama") );
                                nama.setTextColor(getResources().getColor(android.R.color.black));
                                nama.setMaxLines(1);
                                nama.setEllipsize(TextUtils.TruncateAt.END);
                                linearLayout.addView(nama);

                                ViewGroup.LayoutParams layoutJarak = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView jarak= new TextView(MainActivity.this);
                                jarak.setLayoutParams(layoutJarak);
                                jarak.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), 0);
                                jarak.setTextSize(14);
                                jarak.setAllCaps(true);
                                jarak.setText(outlet.getString("jarak")+ " KM");
                                jarak.setTextColor(getResources().getColor(android.R.color.black));
                                jarak.setMaxLines(1);
                                jarak.setEllipsize(TextUtils.TruncateAt.END);
                                linearLayout.addView(jarak);

                                ViewGroup.LayoutParams layoutAlamat= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView alamat = new TextView(MainActivity.this);
                                alamat.setLayoutParams(layoutAlamat);
                                alamat.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), (int) (8 * factor));
                                alamat.setTextSize(14);
                                alamat.setText(outlet.getString("lokasi") + ", " + outlet.getString("alamat"));
                                alamat.setMaxLines(3);
                                alamat.setEllipsize(TextUtils.TruncateAt.END);
                                linearLayout.addView(alamat);

                                nama.setOnClickListener(new selesai(outlet.getInt("id"), "outlet", outlet.getString("jarak")));
                                alamat.setOnClickListener(new selesai(outlet.getInt("id"), "outlet",  outlet.getString("jarak")));
                            }


                            blockOutlet.addView(linearLayout);
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class selesai implements View.OnClickListener{
        Integer id;
        String kat, jarak;
        public selesai(Integer id, String kat, String jarak) {
            this.id = id;
            this.kat = kat;
            this.jarak = jarak;
        }

        @Override
        public void onClick(View v) {
            final Integer id_click = id;
            if (qty_cart > 0) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Keranjang Belanja Tidak Kosong");
                builder1.setMessage("Jika anda mengganti outlet, maka keranjang belanja anda akan dikosongkan. Apakah anda akan berganti outlet?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Iya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.putInt("id_outlet", id_click);
                                editor.putFloat("jarak2", Float.parseFloat(jarak));
                                Log.d("id", id_click.toString());
                                editor.apply();
                                new DeleteTempCart().execute();

                            }
                        });

                builder1.setNegativeButton(
                        "Tidak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                alertDialog.dismiss();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }else {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                editor.putInt("id_outlet", id);
                editor.putFloat("jarak2", Float.parseFloat(jarak));
                Log.d("id", id.toString());
                editor.apply();
                finish();
                startActivity(intent);
            }
        }

    }

    public class cekversi extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlCekVersi, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (versi < jsonObject.getInt("versi")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Versi Terbaru Kopi Sehati telah rilis. Apakah Anda ingin update aplikasi?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "UPDATE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/"));
                                    startActivity(intent);

                                }
                            });

                    builder1.setNegativeButton(
                            "NANTI",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class DeleteTempCart extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlDeleteTempCart, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject != null){
                    if (jsonObject.getInt("error") == 1){

                    }else{
                        Intent intent = new Intent(getApplicationContext(), DaftarMenu.class);
                        sharedPreferences.edit().remove("hasil").apply();
                        sharedPreferences.edit().remove("id_pembayaran").apply();
                        sharedPreferences.edit().remove("id_pengiriman").apply();
                        sharedPreferences.edit().remove("total").apply();
                        sharedPreferences.edit().remove("ongkir").apply();
                        sharedPreferences.edit().remove("harga_per_km").apply();
                        sharedPreferences.edit().remove("jarak").apply();
                        sharedPreferences.edit().remove("id_voucher").apply();
                        sharedPreferences.edit().remove("kode").apply();
                        intent.putExtra("from", "0");
                        startActivity(intent);
                        finish();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
