package ventures.g45.kopisehati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.List;

import javax.xml.validation.Validator;

import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.adapter.adapter_menu;
import ventures.g45.kopisehati.adapter.adapter_orders;
import ventures.g45.kopisehati.modal.modal_menu;
import ventures.g45.kopisehati.modal.modal_orders;

public class Orders extends AppCompatActivity {

    private List<modal_orders> list;
    adapter_orders adapter;
    ListView listView;
    SharedPreferences sharedPreferences;
    String noHp, defaultUrl, urlGetOrder, dataUrl;
    Integer id_outlet;
    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;
    LinearLayout blockInfo, blockProduk;
    ImageView icoBack, history;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    AlertDialog alertDialog;
    float factor;
    Typeface MontserratRegular;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    TextView txtSeeAll;
    TextView namaProduk, detailProduk, txtTotall, txtSize, totall, increment, decrement, display;
    ImageView btnOrder, thumbnail_detail;
    String cek, urlGetDetailMenu, urlPesan, urlCekCart, urlDeleteTempCart;
    Integer id, click;
    LinearLayout blockShoot, blockSize, blockDetail, blockQty, blockProdukKami;
    RelativeLayout blockatas;
    ProgressBar pBar;
    private int uprange = 10;
    private int downrange = 1;
    private int values = 1;
    private int harga_addons = 0;
    private int subtotal = 0;
    private int sHarga= 0;
    Integer id_size=0;
    Integer id_addons=0;
    String id_menu;
    Integer hasil = 0;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        noHp = sharedPreferences.getString("noHp", "");
        id_outlet = sharedPreferences.getInt("id_outlet", 0);
        hasil = sharedPreferences.getInt("hasil", 0);

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        urlGetOrder = defaultUrl + "getorders.html";
        urlGetDetailMenu = defaultUrl + "getmenudetail.html";
        urlPesan = defaultUrl + "savetempcart.html";
        urlCekCart = defaultUrl + "cektempcart.html";
        urlDeleteTempCart = defaultUrl + "deletetemp.html";

        factor = getResources().getDisplayMetrics().density;
        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();

        list = new ArrayList<>();
        listView = findViewById(R.id.list_item);
        adapter = new adapter_orders(Orders.this, R.layout.cv_orders, list);
        listView.setAdapter(adapter);

        blockProduk = findViewById(R.id.blockPromo);
        blockProdukKami = findViewById(R.id.blockProduk);
        blockInfo = findViewById(R.id.blockInfo);
        icoBack = findViewById(R.id.icBack);
        txtSeeAll = findViewById(R.id.txtSeeAll);
        icoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(Orders.this, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.riwayat_order, null);
                dialog.setView(dialogView);
                alertDialog = dialog.create();
                listView = dialogView.findViewById(R.id.list_item);
                blockInfo = dialogView.findViewById(R.id.blockInfo);
                icoBack = dialogView.findViewById(R.id.icBack);



                list = new ArrayList<>();
                adapter = new adapter_orders(Orders.this, R.layout.cv_orders, list);
                listView.setAdapter(adapter);

                String status = "history";

                new GetDaftarOrder().execute(status);

                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                icoBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modal_orders modalOrders = (modal_orders) parent.getItemAtPosition(position);
                if (!modalOrders.getStatus_pembayaran().equals("null")){
                    if (modalOrders.getStatus_pembayaran().equals("lunas")){
                        Intent intent = new Intent(getApplicationContext(), DetailBerlangganan.class);
                        editor.putInt("id_paket", modalOrders.getId_paket());
                        editor.apply();
                        intent.putExtra("id_orderan", modalOrders.getId_orderan());
                        intent.putExtra("nama_paket", "nama");
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), DetailOrders.class);
                        intent.putExtra("id_orderan", modalOrders.getId_orderan());
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), DetailOrders.class);
                    intent.putExtra("id_orderan", modalOrders.getId_orderan());
                    startActivity(intent);
                }
            }
        });

        txtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DaftarMenu.class);
                intent.putExtra("id_kategori", "1");
                intent.putExtra("from", "2");
                startActivity(intent);
            }
        });


        String status = "progress";
        new GetDaftarOrder().execute(status);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private class GetDaftarOrder extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("status", args[0]));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetOrder, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Orders.this);
            pDialog.setMessage("Mengambil data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null){
                    if (result.getInt("error") == 1){

                    }else{
                        if (result.has("data")) {
                            JSONArray daftarOrder = new JSONArray(result.getString("data"));
                            if (daftarOrder.length() > 0) {
                                blockProdukKami.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                blockInfo.setVisibility(View.GONE);
                                for (int j = 0; j < daftarOrder.length(); j++) {

                                    final JSONObject daftar = daftarOrder.getJSONObject(j);

                                    modal_orders modalOrders = new modal_orders();
                                    modalOrders.setId_orderan(daftar.getString("id_orderan"));
                                    modalOrders.setWaktu(daftar.getString("waktu"));
                                    modalOrders.setStatus_orderan(daftar.getString("status_orderan"));
                                    modalOrders.setThumbnail(daftar.getString("qrcode"));
                                    modalOrders.setStatus_pembayaran(daftar.getString("status_pembayaran"));
                                    modalOrders.setId_paket(daftar.getInt("id_paket"));
                                    list.add(modalOrders);
                                    //Helper.getListViewSize(listView);
                                }
                            }
                        }else {
                            JSONArray kategoriMenu = new JSONArray(result.getString("menu"));
                            if (kategoriMenu.length() > 0) {
                                for (int i = 0; i < kategoriMenu.length(); i++) {

                                    final JSONObject menu = kategoriMenu.getJSONObject(i);

                                    //String id_kat = menu.getString("id_kategori");

                                    LinearLayout kategori = new LinearLayout(Orders.this);
                                    kategori.setPadding((int) (0 * factor), (int) (0 * factor), (int) (0 * factor), (int) (10 * factor));
                                    kategori.setBackground(getDrawable(R.drawable.radius_corners));
                                    kategori.setOrientation(LinearLayout.VERTICAL);
                                    ImageView thumbnail = new ImageView(Orders.this);
                                    Picasso.get().load(dataUrl + "menu/thumbnail/" + menu.getString("thumbnail")).into(thumbnail);

                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (130 * factor), (int) (130 * factor));
                                    thumbnail.setLayoutParams(layoutParams);

                                    kategori.addView(thumbnail);

                                    ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    TextView nama = new TextView(Orders.this);
                                    nama.setTextSize(16);
                                    nama.setLayoutParams(layoutNama);
                                    nama.setTypeface(MontserratRegular);
                                    nama.setPadding((int) (5 * factor), (int) (5 * factor), (int) (0 * factor), (int) (0 * factor));
                                    nama.setText(menu.getString("nama"));
                                    nama.setMaxLines(2);
                                    nama.setEllipsize(TextUtils.TruncateAt.END);
                                    //nama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    nama.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    kategori.addView(nama);

                                    ViewGroup.LayoutParams layoutHarga = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    TextView harga = new TextView(Orders.this);
                                    harga.setTextSize(16);
                                    harga.setLayoutParams(layoutHarga);
                                    harga.setTypeface(MontserratRegular);
                                    harga.setPadding((int) (5 * factor), (int) (5 * factor), (int) (0 * factor), (int) (0 * factor));
                                    harga.setText("Rp " + decimalFormat.format(menu.getInt("harga")));
                                    harga.setMaxLines(2);
                                    harga.setEllipsize(TextUtils.TruncateAt.END);
                                    // harga.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    harga.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                   // harga.setTypeface(Typeface.DEFAULT_BOLD);
                                    kategori.addView(harga);

                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (i == 0) {
                                        layoutParams1.setMargins((int) (0 * factor), 0, (int) (0 * factor), (int) (10 * factor));
                                    } else {
                                        layoutParams1.setMargins((int) (10 * factor), 0, (int) (0 * factor), (int) (10 * factor));
                                    }

                                    kategori.setLayoutParams(layoutParams1);


                                    kategori.setOnClickListener(new GetMenu(menu.getString("id_menu"), menu.getInt("size"), menu.getInt("harga")));

                                    blockProduk.addView(kategori);
                                }
                            }
                        }




                    }
                }else{

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    }

    public class GetMenu implements View.OnClickListener {

        String id;
        Integer size, harga;
        ImageView thumbnail;
        public GetMenu(String id, Integer size, Integer harga) {
            this.id = id;
            this.size = size;
            this.harga = harga;
        }

        @Override
        public void onClick(View v) {
            id_menu = id;
            id_size = size;
            sHarga = harga;
            showDetailMenu();
        }
    }

    public void showDetailMenu(){

        dialog = new AlertDialog.Builder(Orders.this, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
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
                Intent intent = new Intent(getApplicationContext(), Orders.class);
                startActivity(intent);
            }

        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CekCart().execute();
            }
        });



        cek = "baru";

        new GetDetailMenu().execute(id_menu);



    }

    private class GetDetailMenu extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Orders.this);
            pDialog.setMessage("Mengambil data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
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
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    } else {
                        alertDialog.show();
                        namaProduk.setText(result.getString("nama"));
                        detailProduk.setText(result.getString("keterangan"));
                        Picasso.get().load(dataUrl + "menu/thumbnail/" + result.getString("thumbnail")).into(thumbnail_detail);
                        subtotal = (sHarga + harga_addons) * values;
                        totall.setText("Rp " + decimalFormat.format(Integer.valueOf(subtotal)));

                        JSONArray size = new JSONArray(result.getString("size"));
                        if (size.length() > 0) {
                            for (int i = 0; i < size.length(); i++) {

                                final JSONObject menu = size.getJSONObject(i);

                                final Integer idsize = menu.getInt("id_size");

                                final LinearLayout daftar = new LinearLayout(Orders.this);
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

                                final TextView nama = new TextView(Orders.this);
                                nama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                nama.setLayoutParams(layoutNama);
                                nama.setTypeface(MontserratRegular);
                                nama.setPadding((int)(0*factor), (int) (5*factor), (int)(0*factor), (int)(8*factor));
                                nama.setTextSize(14);
                                nama.setText(menu.getString("nama"));
                                nama.setTextColor(getResources().getColor(android.R.color.white));
                                nama.setEllipsize(TextUtils.TruncateAt.END);
                                nama.setTypeface(null, Typeface.BOLD);
                                nama.setMaxLines(2);
                                nama.setEllipsize(TextUtils.TruncateAt.END);
                                daftar.addView(nama);

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

                                    final LinearLayout daftar = new LinearLayout(Orders.this);
                                    daftar.setWeightSum(addOns.length());
                                    daftar.setBackground(getResources().getDrawable(R.drawable.border));
                                    daftar.setPadding((int) (1 * factor), (int) (2 * factor), (int) (5 * factor), (int) (5 * factor));
                                    daftar.setOrientation(LinearLayout.VERTICAL);

                                    ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    final TextView nama = new TextView(Orders.this);
                                    nama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    nama.setLayoutParams(layoutNama);
                                    nama.setTypeface(MontserratRegular);
                                    nama.setPadding((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));
                                    nama.setTextSize(14);
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
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Orders.this);
            pDialog.setMessage("Mengirim data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

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
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (jsonObject != null){
                    if (jsonObject.getInt("error") == 1){

                    }else {
                        Log.d("adagak", jsonObject.getString("cek"));
                        if (jsonObject.has(jsonObject.getString("cek"))) {
                            if (jsonObject.getInt("cek") == 2) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Orders.this);
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
            params.add(new BasicNameValuePair("id_menu", id_menu.toString()));
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

            pDialog = new ProgressDialog(Orders.this);
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
                        Intent intent = new Intent(getApplicationContext(), DaftarMenu.class);
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
            pDialog = new ProgressDialog(Orders.this);
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
}
