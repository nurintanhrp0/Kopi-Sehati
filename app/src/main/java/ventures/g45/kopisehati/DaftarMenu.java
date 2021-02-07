package ventures.g45.kopisehati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.adapter.adapter_kategori;
import ventures.g45.kopisehati.adapter.adapter_menu;
import ventures.g45.kopisehati.adapter.adapter_pesanan;
import ventures.g45.kopisehati.modal.modal_kaegori;
import ventures.g45.kopisehati.modal.modal_menu;
import ventures.g45.kopisehati.modal.modal_pesanan;

public class DaftarMenu extends AppCompatActivity {

    TextView nama, txtEspresso, txtSingleOrigin, txtJuiceSmoothies, txtNonCoffee, txtSnacks, txtOthers, txtTotal, total, namaProduk, detailProduk, txtSize, txtTotall, totall, increment, decrement, display;
    ImageView ic_espresso, ic_singleOrigin, ic_juiceSmoothies, ic_nonCoffee, ic_snacks, ic_others, thumbnail_detail, thumbnail;
    Button btnPesan;
    ImageView btnOrder;
    Typeface MontserratRegular;
    static ListView list_espresso;
    ListView list_singleOrigin;
    ListView list_juice;
    ListView blockPesanan;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    NumberPicker numberPicker;
    Intent intent, e, f, g, h, i, j;
    Integer id, id_posisi, qty_cart;
    Integer id_outlet;
    Integer id_menu;
    Integer click = 0;
    Integer addons=0;
    Integer pilih =0;
    Integer id_size=0;
    Integer id_addons=0;
    Integer position=0;
    Integer nilai;
    Integer hasil;
    Integer id_size_ubah;
    Integer id_addons_ubah;
    Integer values_ubah;
    Integer subtotal_ubah;
    Integer ubah_size;
    Integer ubah_addons;
    static Integer selected;
    LinearLayout daftar, espresso, singleOrigin, juiceSmoothies, nonCoffee, snacks, others, blockShoot, blockSize, blockDetail, blockQty;
    private List<modal_menu> list;
    private List<modal_pesanan> list_pesanan;
    private ArrayList<modal_kaegori> list_kategori;
    adapter_menu adapter_espresso, adapter_singleOrigin;
    adapter_pesanan adapterPesanan;
    adapter_kategori adapterKategori;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String defaultUrl, urlGetDaftarMenu, dataUrl, id_kategori, urlGetDetailMenu, sNama, sThumbnail, sKeterangan, from, noHp, urlPesan, urlCekPesanan, cek, session="0", urlCekCart, urlDeleteTempCart;
    SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    float factor;
    HorizontalScrollView blockKategori;
    AlertDialog alertDialog;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    ScrollView blockMenu;
    JSONArray daftarMenu=null;
    private int uprange = 10;
    private int downrange = 1;
    private int values = 1;
    private int harga_addons = 0;
    private int subtotal = 0;
    private int sHarga= 0;
    RecyclerView  blockKategoriMenu;
    static Integer[] posisi;
    ProgressBar pBar;
    RelativeLayout blockatas;
    Integer menu = 0, choose = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_menu);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        id_outlet = sharedPreferences.getInt("id_outlet", 0);
        noHp = sharedPreferences.getString("noHp", "");
        hasil = sharedPreferences.getInt("hasil", 0);
        selected = sharedPreferences.getInt("selected", 0);
        qty_cart = sharedPreferences.getInt("qty_cart", 0);

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlGetDaftarMenu = defaultUrl + "getdatakategori.html";
        urlGetDetailMenu = defaultUrl + "getmenudetail.html";
        urlPesan = defaultUrl + "savetempcart.html";
        urlCekPesanan = defaultUrl + "cekpesanan.html";
        urlCekCart = defaultUrl + "cektempcart.html";
        urlDeleteTempCart = defaultUrl + "deletetemp.html";


        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        id_kategori = intent.getStringExtra("id_kategori");
        from = intent.getStringExtra("from");

        factor = getResources().getDisplayMetrics().density;
        menu = 0;

//        txtEspresso = findViewById(R.id.txtEspresso);
//        txtEspresso.setTypeface(MontserratRegular);
//        txtSingleOrigin = findViewById(R.id.txtSingleOrigin);
//        txtSingleOrigin.setTypeface(MontserratRegular);
//        txtJuiceSmoothies = findViewById(R.id.txtJuiceSmoothies);
//        txtJuiceSmoothies.setTypeface(MontserratRegular);
//        txtNonCoffee = findViewById(R.id.txtNonCoffee);
//        txtNonCoffee.setTypeface(MontserratRegular);
//        txtSnacks = findViewById(R.id.txtSnacks);
//        txtSnacks.setTypeface(MontserratRegular);
//        txtOthers = findViewById(R.id.txtOthers);
//        txtOthers.setTypeface(MontserratRegular);
        txtTotal = findViewById(R.id.txtTotal);
        txtTotal.setTypeface(MontserratRegular);
        total = findViewById(R.id.total);
        total.setTypeface(MontserratRegular);
        btnPesan = findViewById(R.id.btnPesan);
        btnPesan.setTypeface(MontserratRegular);
//        ic_espresso = findViewById(R.id.ic_espresso);
//        ic_singleOrigin = findViewById(R.id.ic_singleOrigin);
//        ic_juiceSmoothies = findViewById(R.id.ic_juiceSmoothies);
//        ic_nonCoffee = findViewById(R.id.ic_nonCoffee);
//        ic_snacks = findViewById(R.id.ic_snacks);
//        ic_others = findViewById(R.id.ic_others);
//        espresso = findViewById(R.id.espresso);
//        espresso.setOnClickListener(this);
//        singleOrigin = findViewById(R.id.singleOrigin);
//        singleOrigin.setOnClickListener(this);
//        juiceSmoothies = findViewById(R.id.juiceSmoothies);
//        juiceSmoothies.setOnClickListener(this);
//        nonCoffee = findViewById(R.id.nonCoffee);
//        nonCoffee.setOnClickListener(this);
//        snacks = findViewById(R.id.snacks);
//        snacks.setOnClickListener(this);
//        others = findViewById(R.id.others);
//        others.setOnClickListener(this);
        //blockKategoriMenu = findViewById(R.id.blockKategoriMenu);
        blockMenu = findViewById(R.id.blockMenu);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        blockKategoriMenu = (RecyclerView) findViewById(R.id.blockKategoriMenu);
        blockKategoriMenu.setLayoutManager(layoutManager);
        list_kategori = new ArrayList<>();

        list = new ArrayList<>();
        list_pesanan = new ArrayList<>();
        list_espresso = findViewById(R.id.list_espresso);
        list_singleOrigin = findViewById(R.id.list_singleOrigin);
        //list_juice = findViewById(R.id.list_juice);

        //list_singleOrigin.setAdapter(adapter);

//        if (id == 2){
//            ic_espresso.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//            txtEspresso.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));
//        } else if (id == 6){
//            ic_singleOrigin.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//            txtSingleOrigin.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));
//        } else if (id == 3){
//            ic_juiceSmoothies.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//            txtJuiceSmoothies.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));
//        } else if (id == 4){
//            ic_nonCoffee.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//            txtNonCoffee.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));
//        } else if (id == 7) {
//            ic_snacks.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//            txtSnacks.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));
//        } else {
//            ic_others.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//            txtOthers.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));
//        }

        list_espresso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modal_menu modalMenu = (modal_menu) parent.getItemAtPosition(position);
                id_menu = Integer.valueOf(modalMenu.getId_menu());
                sNama = modalMenu.getNama();
                sThumbnail = modalMenu.getThumbnail();
                sHarga = Integer.valueOf(modalMenu.getHarga());
                sKeterangan = modalMenu.getKeterangan();
                id_size = Integer.valueOf(modalMenu.getSize());
                id_kategori = modalMenu.getId_kategori();
                selected = position;
                editor.putInt("id_kategori", Integer.parseInt(id_kategori));
                editor.putInt("click", Integer.valueOf(id_kategori) - 1);
                editor.putInt("selected", selected);
                editor.apply();

                if (choose == 0) {
                    new CekPesanan().execute();
                    choose = 1;
                }
            }
        });

        list_espresso.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                list_espresso.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        modal_menu modalMenu = (modal_menu) view.getItemAtPosition(id_posisi);
                        Integer id = Integer.valueOf(modalMenu.getId_kategori());
                        id = id -1;
                        //editor.putInt("click", Integer.valueOf(id) - 1).apply();
                        Log.d("scroll", String.valueOf(id_kategori));
                        for (int i = 0; i < list_kategori.size(); i++){
                            if (i == id){
                                adapterKategori.notifyItemChanged(id, "ganti");
                            }else{
                                adapterKategori.notifyItemChanged(i, "tidak");
                            }
                        }

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        id_posisi = Integer.valueOf(firstVisibleItem);


                    }
                });
                return false;
            }
        });

        new GetDaftarMenu().execute();
    }


    public static void focusOnView(final Integer id, final Integer dari){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dari == 0){
                        list_espresso.smoothScrollToPositionFromTop(posisi[id], 0);
                }else if (dari == 1){
                     Log.d("ke", id.toString());
                     list_espresso.smoothScrollToPositionFromTop(id, 0);
                }
                }catch (Exception e){}

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (menu == 0) {
            if (from.equals("0")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else if (from.equals("2")) {
                super.onBackPressed();
            } else {
                Intent intent = new Intent(getApplicationContext(), Checkout.class);
                startActivity(intent);
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), DaftarMenu.class);
            intent.putExtra("id_kategori", id_kategori);
            intent.putExtra("from", "0");
            startActivity(intent);
        }

    }

    private class GetDaftarMenu extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetDaftarMenu, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(DaftarMenu.this);
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
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    }else {
                        Integer id = 0, baris =0;
                        posisi = new Integer[100];
                        JSONArray kategoriMenu = new JSONArray(result.getString("menu"));
                        if (kategoriMenu.length() > 0) {
                            for (int i = 0; i < kategoriMenu.length(); i++) {

                                final JSONObject menu = kategoriMenu.getJSONObject(i);

                                final Integer id_kat = menu.getInt("id_kategori");

                                modal_kaegori modalKaegori = new modal_kaegori();
                                modalKaegori.setNama(menu.getString("nama"));
                                modalKaegori.setThumbnail(menu.getString("thumbnail"));
                                modalKaegori.setId_kategori(menu.getInt("id_kategori"));

                                list_kategori.add(modalKaegori);
                                adapterKategori = new adapter_kategori(list_kategori, DaftarMenu.this);
                                blockKategoriMenu.setAdapter(adapterKategori);


                            }
                        }
                        JSONObject menu, menu2, menu3;

                        daftarMenu = new JSONArray(result.getString("kategori"));
                        if (daftarMenu.length() > 0){
                            for (int i=0; i < daftarMenu.length(); i++) {

                                menu2 = daftarMenu.getJSONObject(i);

                                modal_menu modalMenu1 = new modal_menu();
                                modalMenu1.setId(menu2.getString("id"));


                                if (!menu2.getString("id").equals("null")) {
                                    modalMenu1.setId_kategori(menu2.getString("id_kategori"));
                                    JSONArray daftar = new JSONArray(menu2.getString("espresso"));

                                    for (int j = 0; j < daftar.length(); j++) {

                                        menu3 = daftar.getJSONObject(j);
                                        modal_menu modalMenu = new modal_menu();
                                        modalMenu.setNama(menu3.getString("nama"));
                                        modalMenu.setThumbnail(menu3.getString("thumbnail"));
                                        modalMenu.setHarga(menu3.getString("harga_jual"));
                                        Log.d("harga", menu3.getString("harga_jual"));
                                        modalMenu.setId_menu(menu3.getString("id_menu"));
                                        modalMenu.setKeterangan(menu3.getString("keterangan"));
                                        modalMenu.setSize(menu3.getString("size"));
                                        modalMenu.setId_kategori(menu2.getString("id_kategori"));
                                        modalMenu.setNama_kategori(menu3.getString("nama_kategori"));


                                            if (id != menu2.getInt("id_kategori")) {
                                                posisi[menu2.getInt("id_kategori")] = baris;
                                                id = menu2.getInt("id_kategori");
                                                Log.d("id", id.toString());
                                                modalMenu.setBaris(baris);
                                            }else {
                                                modalMenu.setBaris(-1);
                                            }

                                        baris = baris + 1;

                                        list.add(modalMenu);
                                        adapter_espresso = new adapter_menu(DaftarMenu.this, R.layout.cv_daftar_menu, list);
                                        list_espresso.setAdapter(adapter_espresso);

                                    }
                                }else {
                                    baris = baris + 1;
                                    posisi[menu2.getInt("id_kosong")] = baris;

                                    Log.d("id_kat",menu2.getString("id_kosong"));

                                   // ArrayList<String> items = new ArrayList<>();
                                    //String item = "Pig";
                                    //int insertIndex = baris;
                                    //items.add(insertIndex, item);
                                    modalMenu1.setNama("null");
                                    modalMenu1.setBaris(baris);
                                    modalMenu1.setNama_kategori(menu2.getString("nama_kategori"));
                                    adapter_espresso.add(modalMenu1);
                                    adapter_espresso.notifyDataSetChanged();
                                    list_espresso.setAdapter(adapter_espresso);

                                }



                            }
                        }

                            hasil = 0;
                            JSONArray daftarPesanan = new JSONArray(result.getString("pesanan"));
                            if (daftarPesanan.length() > 0) {
                                for (int k = 0; k < daftarPesanan.length(); k++) {
                                    final JSONObject pesanan = daftarPesanan.getJSONObject(k);
                                    Integer sub = pesanan.getInt("harga");
                                    hasil = hasil + sub;
                                }
                            }

                        total.setText("Rp " + decimalFormat.format(hasil));
                        if (hasil != 0) {
                            btnPesan.setClickable(true);
                            btnPesan.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
                            btnPesan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent z = new Intent(getApplicationContext(), Checkout.class);
                                    startActivity(z);
                                }
                            });
                        }

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Ups! Menu yang kamu pilih belum tersedia di outlet ini.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class KategoriMenu implements View.OnClickListener {

        String id, kat;
        Integer pos;
        ImageView thumbnail;
        public KategoriMenu(String id, ImageView thumbnail) {
            this.id = id;
            this.thumbnail = thumbnail;
        }

        @Override
        public void onClick(View v) {
            thumbnail.setImageTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
//            Intent intent = new Intent(DaftarMenu. this, DaftarMenu.class);
////            intent.putExtra("id_kategori", id);
////            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            id_kategori = id;
////
        Log.d("onclick", id);
////           startActivity(intent);
            //focusOnView();
        }
    }

    private class CekPesanan extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DaftarMenu.this);
            pDialog.setMessage("Mengambil data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("id_menu", id_menu.toString()));
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlCekPesanan, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(final JSONObject result) {

            try {
                if (result != null) {

                    if (result.has("posisi")) {
                        if ((pDialog != null) && (pDialog.isShowing()))
                            pDialog.dismiss();
                        pDialog = null;

                        JSONArray daftar = new JSONArray(result.getString("posisi"));
                        for (int i = 0; i < daftar.length(); i++) {
                            final JSONObject pesanan = daftar.getJSONObject(i);
                            if (daftar.length() > 1) {
                                dialog = new AlertDialog.Builder(DaftarMenu.this, R.style.DialogPutih);
                                inflater = getLayoutInflater();
                                dialogView = inflater.inflate(R.layout.popup_daftar_pesanan, null);
                                dialog.setView(dialogView);
                                alertDialog = dialog.create();
                                alertDialog.setCancelable(false);

                                blockPesanan = dialogView.findViewById(R.id.blockDaftar);
                                TextView tambahPesanan = dialogView.findViewById(R.id.tambahPesanan);
                                ImageView icClose = dialogView.findViewById(R.id.icClose);

                                        modal_pesanan modalPesanan = new modal_pesanan();
                                        modalPesanan.setNama(pesanan.getString("nama"));
                                        modalPesanan.setHarga(pesanan.getString("harga"));
                                        modalPesanan.setHarga_size(pesanan.getInt("harga_size"));
                                        modalPesanan.setSize(pesanan.getString("size"));
                                        modalPesanan.setAddons(pesanan.getString("addons"));
                                        modalPesanan.setQty(pesanan.getString("qty"));
                                        modalPesanan.setThumbnail(pesanan.getString("thumbnail"));
                                        modalPesanan.setHarga_addons(pesanan.getInt("harga_addons"));
                                        modalPesanan.setId_size(pesanan.getInt("id_size"));
                                        modalPesanan.setId_addons(pesanan.getInt("id_addons"));
                                        modalPesanan.setSession(pesanan.getString("session"));

                                        list_pesanan.add(modalPesanan);
                                        adapterPesanan = new adapter_pesanan(DaftarMenu.this, R.layout.cv_daftar_pesanan, list_pesanan);
                                        blockPesanan.setAdapter(adapterPesanan);
                                        Helper.getListViewSize(blockPesanan);

                                blockPesanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        cek = "ubah";
                                        modal_pesanan modalPesanan = (modal_pesanan) parent.getItemAtPosition(position);
                                        id_size_ubah = modalPesanan.getId_size();
                                        id_addons_ubah = modalPesanan.getId_addons();
                                        values_ubah = Integer.valueOf(modalPesanan.getQty());
                                        ubah_addons = modalPesanan.getHarga_addons();
                                        ubah_size = Integer.valueOf(modalPesanan.getHarga_size());
                                        session = modalPesanan.getSession();

                                        showDetailMenu();
                                    }
                                });

                                tambahPesanan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        session = "";
                                        cek = "baru";
                                        showDetailMenu();
                                    }
                                });

                                icClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       // alertDialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), DaftarMenu.class);
                                        intent.putExtra("from", "0");
                                        startActivity(intent);
                                    }
                                });

                                alertDialog.show();
                            } else {
                                id_size_ubah = pesanan.getInt("id_size");
                                id_addons_ubah = pesanan.getInt("id_addons");
                                values_ubah = pesanan.getInt("qty");
                                ubah_addons = pesanan.getInt("harga_addons");
                                ubah_size = pesanan.getInt("harga_size");
                                session = pesanan.getString("session");

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(DaftarMenu.this);
                                builder1.setMessage("Apakah Anda ingin mengubah pesanan?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ubah",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                cek = "ubah";
                                                showDetailMenu();
                                                dialog.cancel();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Pesanan Baru",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                session = "";
                                                cek = "baru";
                                                showDetailMenu();
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        }

                    }
                    else {
                        cek = "baru";
                        showDetailMenu();
                    }
                }
            }
            catch(JSONException e){
                    e.printStackTrace();
                }
            }

    }

    public void showDetailMenu(){
        pDialog = new ProgressDialog(DaftarMenu.this);
        pDialog.setMessage("Mengirim data...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);
        pDialog.show();

        if (cek.equals("ubah")){
            id_size = id_size_ubah;
            id_addons = id_addons_ubah;
            values = values_ubah;
            harga_addons = ubah_addons;
            sHarga = ubah_size;
            click = 1;
            subtotal = (sHarga + harga_addons) * values;
            hasil = hasil - subtotal;
            downrange = 0;
        }
        dialog = new AlertDialog.Builder(DaftarMenu.this, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.detail_menu, null);
        dialog.setView(dialogView);

        alertDialog = dialog.create();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                menu = 1;
                onBackPressed();
            }
        });
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
        //final ImageView btnOrder = dialogView.findViewById(R.id.btnOrder);
        blockatas = dialogView.findViewById(R.id.blockAtas);
        blockDetail = dialogView.findViewById(R.id.blockDetail);
        blockQty = dialogView.findViewById(R.id.blockQty);
        final  ImageView btnHapus = dialogView.findViewById(R.id.btnHapus);

        namaProduk.setText(sNama);
        detailProduk.setText(sKeterangan);
        Picasso.get().load(dataUrl + "menu/thumbnail/" + sThumbnail).into(thumbnail_detail);
        subtotal = (sHarga + harga_addons) * values;
        totall.setText("Rp " + decimalFormat.format(Integer.valueOf(subtotal)));

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

                if (values == 0){
                    btnHapus.setVisibility(View.VISIBLE);
                }
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
                if (values == 0){
                    btnHapus.setVisibility(View.VISIBLE);
                }
            }
        });

        if (cek.equals("ubah")){
            btnHapus.setVisibility(View.VISIBLE);
        }

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
                menu = 1;
                onBackPressed();
            }

        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CekCart().execute();
            }
        });



        cek = "baru";

        new GetDetailMenu().execute(id_menu.toString());
    }

    private class GetDetailMenu extends AsyncTask<String, Void, JSONObject>{

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
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    } else {
                        alertDialog.show();
                        JSONArray size = new JSONArray(result.getString("size"));
                        if (size.length() > 0) {
                            for (int i = 0; i < size.length(); i++) {

                                final JSONObject menu = size.getJSONObject(i);

                               final Integer idsize = menu.getInt("id_size");

                                final LinearLayout daftar = new LinearLayout(DaftarMenu.this);
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

                                final TextView nama = new TextView(DaftarMenu.this);
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

                                    final LinearLayout daftar = new LinearLayout(DaftarMenu.this);
                                    daftar.setWeightSum(addOns.length());
                                    daftar.setBackground(getResources().getDrawable(R.drawable.border));
                                    daftar.setPadding((int) (1 * factor), (int) (2 * factor), (int) (5 * factor), (int) (5 * factor));
                                    daftar.setOrientation(LinearLayout.VERTICAL);

                                    ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    final TextView nama = new TextView(DaftarMenu.this);
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

                        choose = 0;
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
            params.add(new BasicNameValuePair("session", session));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlPesan, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DaftarMenu.this);
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

    private class CekCart extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DaftarMenu.this);
            pDialog.setMessage("Mengirim data...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("jenis", "menu"));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlCekCart, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null){
                    if (jsonObject.getInt("error") == 1){

                    }else {
                        if ((pDialog != null) && (pDialog.isShowing()))
                            pDialog.dismiss();
                        pDialog = null;
                        Log.d("adagak", jsonObject.getString("cek"));
                        if (jsonObject.has("cek")) {
                            if (jsonObject.getString("cek").equals("2")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(DaftarMenu.this);
                                builder1.setMessage("Keranjang belanja Anda tidak kosong, apakah Anda ingin mengganti orderan?");
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

    private class DeleteTempCart extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DaftarMenu.this);
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
                        intent.putExtra("from", "0");
                        new Pesan().execute();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
