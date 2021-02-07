package ventures.g45.kopisehati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Promo extends AppCompatActivity {

    TextView txtTitle, txtJudul, txtIsi, namaProduk, detailProduk, txtTotall, txtSize, totall, increment, decrement, display;
    ImageView icBack, btnOrder, thumbnail_detail;
    Button btnPesan;
    Typeface MontserratRegular;
    String sJudul, sIsi, noHp, cek, defaultUrl, urlGetDetailMenu, urlPesan, urlCekPesanan, urlCekCart, urlDeleteTempCart, dataUrl, urlGetNotif;
    Integer id, id_outlet, click;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    JSONParser jsonParser = new JSONParser();
    AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout blockShoot, blockSize, blockDetail, blockQty;
    RelativeLayout blockatas;
    ProgressBar pBar;
    private int uprange = 10;
    private int downrange = 1;
    private int values = 1;
    private int harga_addons = 0;
    private int subtotal = 0;
    private int sHarga= 0;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    float factor;
    Integer id_size=0;
    Integer id_addons=0;
    ProgressDialog pDialog;
    Integer hasil = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        id_outlet = sharedPreferences.getInt("id_outlet", 0);
        noHp = sharedPreferences.getString("noHp", "");
        hasil = sharedPreferences.getInt("hasil", 0);

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlGetDetailMenu = defaultUrl + "getmenudetail.html";
        urlPesan = defaultUrl + "savetempcart.html";
        urlCekPesanan = defaultUrl + "cekpesanan.html";
        urlCekCart = defaultUrl + "cektempcart.html";
        urlDeleteTempCart = defaultUrl + "deletetemp.html";
        urlGetNotif = defaultUrl + "getnotif.html";

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        factor = getResources().getDisplayMetrics().density;

        txtTitle = findViewById(R.id.txtTitle);
        txtJudul = findViewById(R.id.judul);
        txtIsi = findViewById(R.id.isi);
        icBack = findViewById(R.id.icBack);
        btnPesan = findViewById(R.id.btnPesan);
        txtTitle.setTypeface(MontserratRegular);
        txtJudul.setTypeface(MontserratRegular);
        txtIsi.setTypeface(MontserratRegular);
        btnPesan.setTypeface(MontserratRegular);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        if (id != 0) {
            sJudul = intent.getStringExtra("judul");
            sIsi = intent.getStringExtra("isi");
            txtJudul.setText(sJudul);
            txtIsi.setText(Html.fromHtml(sIsi));
        }else{
            new GetNotif().execute();
        }

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailMenu();
            }
        });

    }

    public void showDetailMenu(){

        dialog = new AlertDialog.Builder(Promo.this, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.detail_menu, null);
        dialog.setView(dialogView);

        alertDialog = dialog.create();
        namaProduk = dialogView.findViewById(R.id.namaProduk);
        namaProduk.setTypeface(MontserratRegular);
        detailProduk = dialogView.findViewById(R.id.detailProduk);
        detailProduk.setTypeface(MontserratRegular);
        txtTotall = dialogView.findViewById(R.id.txtTotal);
        txtTotall.setTypeface(MontserratRegular);
        btnOrder = dialogView.findViewById(R.id.btnOrder);
        //btnOrder.setTypeface(MontserratRegular);
        txtSize = dialogView.findViewById(R.id.txtSize);
        txtSize.setTypeface(MontserratRegular);
        thumbnail_detail = dialogView.findViewById(R.id.thumbnail);
        totall = dialogView.findViewById(R.id.total);
        ImageView icBack = dialogView.findViewById(R.id.icBack);
        blockShoot = dialogView.findViewById(R.id.blockShoot);
        blockSize = dialogView.findViewById(R.id.blockSize);
        increment = dialogView.findViewById(R.id.increment);
        decrement = dialogView.findViewById(R.id.decrement);
        display = dialogView.findViewById(R.id.display);
        final ImageView btnOrder = dialogView.findViewById(R.id.btnOrder);
        pBar = dialogView.findViewById(R.id.pBar);
        blockatas = dialogView.findViewById(R.id.blockAtas);
        blockDetail = dialogView.findViewById(R.id.blockDetail);
        blockQty = dialogView.findViewById(R.id.blockQty);
        final  ImageView btnHapus = dialogView.findViewById(R.id.btnHapus);

        display.setText(String.valueOf(values));

        increment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (values >= downrange && values <= uprange)
                    values += 1;
                if (values > uprange)
                    values = downrange;
                display.setText(String.valueOf(values));

                subtotal = (sHarga + harga_addons) * values;
                totall.setText("Rp " + decimalFormat.format(Integer.valueOf(subtotal)));

            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (values >= downrange && values <= uprange)
                    values -= 1;

                if (values < downrange)
                    values = uprange;

                display.setText(String.valueOf(values));
                subtotal = (sHarga + harga_addons) * values;
                totall.setText("Rp " + decimalFormat.format(Integer.valueOf(subtotal)));
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = 0;
                new CekCart().execute();
            }
        });

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }

        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CekCart().execute();
            }
        });



        cek = "baru";

        new GetDetailMenu().execute(id.toString());

        alertDialog.show();

    }

    private class GetDetailMenu extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("id_menu", args[0]));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
            params.add(new BasicNameValuePair("cek", cek));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetDetailMenu, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            pBar.setVisibility(View.GONE);
            blockatas.setVisibility(View.VISIBLE);
            blockDetail.setVisibility(View.VISIBLE);
            blockQty.setVisibility(View.VISIBLE);

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    } else {

                        namaProduk.setText(result.getString("nama"));
                        detailProduk.setText(result.getString("keterangan"));
                        Picasso.get().load(dataUrl + "menu/thumbnail/" + result.getString("thumbnail")).into(thumbnail_detail);
                        sHarga = result.getInt("harga_jual");
                        subtotal = (sHarga + harga_addons) * values;
                        totall.setText("Rp " + decimalFormat.format(Integer.valueOf(subtotal)));

                        JSONArray size = new JSONArray(result.getString("size"));
                        if (size.length() > 0) {
                            for (int i = 0; i < size.length(); i++) {

                                final JSONObject menu = size.getJSONObject(i);

                                final Integer idsize = menu.getInt("id_size");

                                final LinearLayout daftar = new LinearLayout(Promo.this);
                                daftar.setWeightSum(size.length());
                                daftar.setBackground(getResources().getDrawable(R.drawable.border));
                                daftar.setPadding((int) (5 * factor), (int) (2 * factor), (int) (8* factor), 0);
                                daftar.setOrientation(LinearLayout.VERTICAL);

                                ViewGroup.LayoutParams layoutNama;

                                if (size.length() == 1){
                                    layoutNama = new ViewGroup.LayoutParams((int) (100 * factor), ViewGroup.LayoutParams.WRAP_CONTENT);}
                                else {
                                    layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                }

                                final TextView nama = new TextView(Promo.this);
                                nama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                nama.setLayoutParams(layoutNama);
                                nama.setTypeface(MontserratRegular);
                                nama.setPadding((int)(0*factor), (int) (5*factor), (int)(0*factor), (int)(8*factor));
                                nama.setTextSize(18);
                                nama.setText(menu.getString("nama"));
                                nama.setTextColor(getResources().getColor(android.R.color.white));
                                nama.setEllipsize(TextUtils.TruncateAt.END);
                                nama.setTypeface(null, Typeface.BOLD);
                                nama.setMaxLines(2);
                                nama.setEllipsize(TextUtils.TruncateAt.END);
                                daftar.addView(nama);

                                //harga_size = menu.getInt("harga");

//                                if (id_add_ons.equals(null)) {
//                                    if (id_addons.equals(id_add_ons)) {
//                                        daftar.setBackground(getResources().getDrawable(R.drawable.border_white));
//                                        nama.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                                        produk.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                                    }
//                                }
                                if (size.length() == 1){
                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams1.setMargins(0, 0, (int) (15 * factor), (int) (10 * factor));
                                    daftar.setLayoutParams(layoutParams1);
                                } else {
                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                                    layoutParams1.setMargins(0, 0, (int) (15 * factor), (int) (10 * factor));
                                    daftar.setLayoutParams(layoutParams1);
                                }

                                if (id_size == idsize){
                                    daftar.setBackground(getResources().getDrawable(R.drawable.border_white));
                                    nama.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                                }

                                daftar.setOnClickListener(new DetailMenu(menu.getString("id_size"), "size", menu.getInt("harga")));

                                blockSize.addView(daftar);
                            }
                        }

                        JSONArray addOns = new JSONArray(result.getString("addons"));
                        if (!addOns.equals(null)) {
                            blockShoot.setVisibility(View.VISIBLE);
                            if (addOns.length() > 0) {
                                for (int i = 0; i < addOns.length(); i++) {

                                    final JSONObject menu = addOns.getJSONObject(i);

                                    final Integer id_add_ons = menu.getInt("id_addons");

                                    final LinearLayout daftar = new LinearLayout(Promo.this);
                                    daftar.setWeightSum(addOns.length());
                                    daftar.setBackground(getResources().getDrawable(R.drawable.border));
                                    daftar.setPadding((int) (1 * factor), (int) (2 * factor), (int) (5 * factor), (int) (5 * factor));
                                    daftar.setOrientation(LinearLayout.VERTICAL);

                                    ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    final TextView nama = new TextView(Promo.this);
                                    nama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    nama.setLayoutParams(layoutNama);
                                    nama.setTypeface(MontserratRegular);
                                    nama.setPadding((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (0 * factor));
                                    nama.setTextSize(18);
                                    nama.setText(menu.getString("nama"));
                                    nama.setTextColor(getResources().getColor(android.R.color.white));
                                    nama.setEllipsize(TextUtils.TruncateAt.END);
                                    nama.setTypeface(null, Typeface.BOLD);
                                    nama.setMaxLines(2);
                                    nama.setEllipsize(TextUtils.TruncateAt.END);
                                    daftar.addView(nama);

                                    //harga_addons = Integer.valueOf(menu.getString("harga_jual"));

                                    if (id_addons == id_add_ons) {
                                        if (click == 1) {
                                            daftar.setBackground(getResources().getDrawable(R.drawable.border_white));
                                            nama.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                                        }
                                    }

                                    Log.d("banyak", Integer.valueOf(menu.length()).toString());

                                    if (addOns.length() == 1){
                                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams1.setMargins(0, 0, (int) (15 * factor), (int) (10 * factor));
                                        daftar.setLayoutParams(layoutParams1);
                                    } else {
                                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                                        layoutParams1.setMargins(0, 0, (int) (15 * factor), (int) (10 * factor));
                                        daftar.setLayoutParams(layoutParams1);
                                    }

                                    daftar.setOnClickListener(new DetailMenu(menu.getString("id_addons"), "addons", menu.getInt("harga_jual")));

                                    blockShoot.addView(daftar);
                                }
                            }
                        }
                    }
                }else{

                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class DetailMenu implements View.OnClickListener {

        String id, kat;
        Integer harga;
        public DetailMenu(String id, String kat, Integer harga) {
            this.id = id;
            this.kat = kat;
            this.harga = harga;
        }

        @Override
        public void onClick(View v) {
            if (kat.equals("size")) {
                sHarga = harga;
                id_size = Integer.valueOf(id);
                showDetailMenu();
                Log.d("id_size", id);
            } else{
                if (id.equals(String.valueOf(id_addons))) {
                    if (click == 0) {
                        harga_addons = harga;
                        click = 1;
                    }else {
                        id_addons = Integer.valueOf(0);
                        harga_addons = 0;
                        click = 0;
                    }
                }
                else {
                    harga_addons = harga;
                    click = 1;
                    id_addons = Integer.valueOf(id);
                }

                showDetailMenu();
            }
        }
    }

    private class CekCart extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("jenis", "menu"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlCekCart, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null){
                    if (jsonObject.getInt("error") == 1){

                    }else {
                        Log.d("adagak", jsonObject.getString("cek"));
                        if (jsonObject.has(jsonObject.getString("cek"))) {
                            if (jsonObject.getInt("cek") == 2) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Promo.this);
                                builder1.setMessage("Apakah Anda ingin mengubah pesanan?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Iya",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                new DeleteTempCart().execute();
                                                dialog.cancel();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Tidak",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                new Pesan().execute();
                            }
                        }else {
                            new Pesan().execute();
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    private class Pesan extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
            params.add(new BasicNameValuePair("id_menu", id.toString()));
            params.add(new BasicNameValuePair("id_size", id_size.toString()));
            params.add(new BasicNameValuePair("id_addons", id_addons.toString()));
            params.add(new BasicNameValuePair("qty", String.valueOf(values)));
            params.add(new BasicNameValuePair("session", ""));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlPesan, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Promo.this);
            pDialog.setMessage("Mengirim data...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null){
                    if (result.getInt("error")==1){

                    }else {
                        hasil = hasil + subtotal;

                        editor.putInt("hasil", hasil);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Checkout.class);
                        editor.putInt("selected", 0);
                        editor.apply();
                        intent.putExtra("from", "0");
                        startActivity(intent);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class DeleteTempCart extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Promo.this);
            pDialog.setMessage("Mengirim data...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlDeleteTempCart, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (jsonObject != null){
                    if (jsonObject.getInt("error") == 1){

                    }else{
                        sharedPreferences.edit().remove("hasil").apply();
                        sharedPreferences.edit().remove("id_pembayaran").apply();
                        sharedPreferences.edit().remove("id_pengiriman").apply();
                        sharedPreferences.edit().remove("total").apply();
                        sharedPreferences.edit().remove("ongkir").apply();
                        sharedPreferences.edit().remove("harga_per_km").apply();
                        sharedPreferences.edit().remove("jarak").apply();
                        sharedPreferences.edit().remove("id_voucher").apply();
                        sharedPreferences.edit().remove("kode").apply();
                        new Pesan().execute();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class GetNotif extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Promo.this);
            pDialog.setMessage("Mengambil data...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetNotif, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (jsonObject != null){
                    if (jsonObject.getInt("error") == 1){

                    }else{
                        if (jsonObject.getString("jenis").equals("promo produk")){
                            Intent resultIntent = new Intent(getApplicationContext(), Promo.class);
                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            resultIntent.putExtra("title", jsonObject.getString("judul"));
                            resultIntent.putExtra("isi", jsonObject.getString("isi"));
                            resultIntent.putExtra("id", jsonObject.getInt("id"));
                            startActivity(resultIntent);
                        }else if (jsonObject.getString("jenis").equals("voucher")){
                            Intent resultIntent = new Intent(getApplicationContext(), Detail_inbox.class);
                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            editor.putString("isi", jsonObject.getString("isi"));
                            editor.putInt("id", jsonObject.getInt("id"));
                            editor.apply();
                            startActivity(resultIntent);
                        }else {
                            Intent resultIntent = new Intent(getApplicationContext(), Inbox.class);
                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(resultIntent);
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
