package ventures.g45.kopisehati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ventures.g45.kopisehati.adapter.adapter_menu;
import ventures.g45.kopisehati.adapter.adapter_pesanan;
import ventures.g45.kopisehati.modal.modal_menu;
import ventures.g45.kopisehati.modal.modal_pesanan;

public class Checkout extends AppCompatActivity {

    Typeface MontserratRegular;
    TextView txtTitle, txtDikirim, txtAmbilSendrii, txtInfo, txtAlamat, txtWaktuPesanan, txtWaktu, txtDibawaPulang, txtDaftarPesanan, txtTambahPesanan, txtPromo, txtTambahPromo, txtPembayaran, txtDetailPembayaran, txtHarga, harga, txtOngkir, ongkir, txtTotal, total, totall, increment, decrement, display, kosong2, txtPengiriman, txtDiskon, diskon, txtNoPesanan, txtDetailAlamat, txtLokasi;
    LinearLayout blockAlamat, blockWaktuPesanan, blockDibawaPulang, blockOngkir,blockMetode, blockOutlet, blockShoot, blockSize, blockPengiriman, blockDiskon, blockImagePembayaran, blockImagePengiriman, blockDetail, blockQty;
    RelativeLayout promo;
    Integer pesanan, id_outlet, id_pembayaran=0, click=0, hasil, id_menu, id_size,id_addons, id_pengiriman=0, hargaDiskon, asal, min, nilai, maks;
    Intent intent, a, b;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    JSONParser jsonParser = new JSONParser();
    String defaultUrl, urlGetOutlet, urlGetCheckout, detailAlamat, dataUrl, noHp, sAlamat, koordinat_member, alamat, urlGantiPesanan, session, urlGetDetailMenu, urlPesan,  sNama, sKeterangan, sThumbnail, jenis, urlsimpanorder, kode, keterangan, urlDeleteTempCart, nama_pembayaran, jenis_promo, nama_alamat, urlHitungJarak, no_tujuan, nama_outlet, lokasi, detail_outlet, atas_nama;
    Float factor, distance, jarak_alamat;
    AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;
    double latitude, longtitude, lat_alamat, long_alamat, lat_outlet, long_outlet;
    LatLng lokasiku;
    DecimalFormat decimalFormat, format;
    Button btnPesan;
    ImageView icBack, add, img;
    ListView blockPesanan;
    private List<modal_pesanan> list;
    adapter_pesanan adapterPesanan;
    ProgressBar pBar;
    private int uprange = 10;
    private int downrange = 0;
    private int values = 1;
    private int harga_addons = 0;
    private int subtotal = 0;
    private int sHarga= 0;
    private  int hargaTotal = 0;
    private  int hargaOngkir = 0;
    private  int harga_per_km =0;
    private  int jarak =0 ;
    private int id_voucher = 0;
    private int id_alamat = 0;
    RadioGroup blockMetodePembayaran, blockMetodePengiriman;
    Drawable innerBitmap;
    RelativeLayout blockatas;
    Integer menu = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        id_outlet = sharedPreferences.getInt("id_outlet", 0);
        latitude = Double.parseDouble(sharedPreferences.getString("latitude", ""));
        longtitude = Double.parseDouble(sharedPreferences.getString("longtitude", ""));
        lat_alamat = Double.parseDouble(sharedPreferences.getString("lat_alamat", "0"));
        Log.d("lat_alamat", String.valueOf(lat_alamat));
        long_alamat = Double.parseDouble(sharedPreferences.getString("long_alamat", "0"));
        detailAlamat = sharedPreferences.getString("detailAlamat","");
        alamat = sharedPreferences.getString("alamat", "");
        noHp = sharedPreferences.getString("noHp", "");
        lokasiku = new LatLng(latitude, longtitude);
        Log.d("latitude", Double.valueOf(latitude).toString());
        Log.d("longtitude", Double.valueOf(longtitude).toString());
        id_pembayaran = sharedPreferences.getInt("id_pembayaran", 0);
        id_pengiriman = sharedPreferences.getInt("id_pengiriman", 1);
        hargaTotal = sharedPreferences.getInt("total", 0);
        hargaOngkir = sharedPreferences.getInt("ongkir", 0);
        harga_per_km = sharedPreferences.getInt("harga_per_km", 0);
        jarak = sharedPreferences.getInt("jarak", 0);
        id_alamat = sharedPreferences.getInt("id_alamat", 0);
        Log.d("id_alamat", String.valueOf(id_alamat));
        id_voucher = sharedPreferences.getInt("id_voucher", 0);
        hargaDiskon = sharedPreferences.getInt("diskon", 0);
        kode = sharedPreferences.getString("kode", "");
        nilai = sharedPreferences.getInt("nilai", 0);
        Log.d("nilai", nilai.toString());
        min = sharedPreferences.getInt("min", 0);
        maks = sharedPreferences.getInt("maks", 0);
        jenis_promo = sharedPreferences.getString("jenis_promo", "");
        nama_pembayaran = sharedPreferences.getString("nama_pembayaran", "");
        keterangan = sharedPreferences.getString("keterangan", "");
        nama_alamat = sharedPreferences.getString("nama_alamat", " ");
        distance = sharedPreferences.getFloat("jarak2", 0);
        jarak_alamat = sharedPreferences.getFloat("jarak_alamat", 0);
        lat_outlet = Double.parseDouble(sharedPreferences.getString("lat_outlet", "0"));
        long_outlet = Double.parseDouble(sharedPreferences.getString("long_outlet", "0"));

        intent = getIntent();
        pesanan = intent.getIntExtra("pesanan", 2);
        asal = intent.getIntExtra("asal", 0);

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlGetOutlet = defaultUrl + "getdataoutlet.html";
        urlGetCheckout = defaultUrl + "getdatacheckout.html";
        urlGantiPesanan = defaultUrl + "gantipesanan.html";
        urlGetDetailMenu = defaultUrl + "getmenudetail.html";
        urlPesan = defaultUrl + "savetempcart.html";
        urlsimpanorder = defaultUrl + "getsimpanorder.html";
        urlDeleteTempCart = defaultUrl + "deletetemp.html";
        urlHitungJarak = defaultUrl + "getjarak.html";

        factor = getResources().getDisplayMetrics().density;
        decimalFormat = new DecimalFormat("#.##");
        format = new DecimalFormat("#,###.##");
        menu = 0;

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        txtAmbilSendrii = findViewById(R.id.ambilSendiri);
        txtAmbilSendrii.setTypeface(MontserratRegular);
        txtDikirim = findViewById(R.id.dikirim);
        txtDikirim.setTypeface(MontserratRegular);
        txtInfo = findViewById(R.id.txtInfo);
        txtInfo.setTypeface(MontserratRegular);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtAlamat.setTypeface(MontserratRegular);
        txtWaktuPesanan = findViewById(R.id.txtWaktuPesanan);
        txtWaktuPesanan.setTypeface(MontserratRegular);
        txtWaktu = findViewById(R.id.txtWaktu);
        txtWaktu.setTypeface(MontserratRegular);
        txtDaftarPesanan = findViewById(R.id.txtDaftarPesanan);
        txtDaftarPesanan.setTypeface(MontserratRegular);
        txtTambahPesanan = findViewById(R.id.txtTambahPesanan);
        txtTambahPesanan.setTypeface(MontserratRegular);
        txtPromo = findViewById(R.id.txtPromo);
        txtPromo.setTypeface(MontserratRegular);
        txtTambahPromo = findViewById(R.id.txtTambahPromo);
        txtTambahPromo.setTypeface(MontserratRegular);
        txtPembayaran = findViewById(R.id.txtPembayaran);
        txtPembayaran.setTypeface(MontserratRegular);
        txtDetailPembayaran = findViewById(R.id.txtDetailPembayaran);
        txtDetailPembayaran.setTypeface(MontserratRegular);
        txtHarga = findViewById(R.id.txtHarga);
        txtHarga.setTypeface(MontserratRegular);
        harga = findViewById(R.id.harga);
        harga.setTypeface(MontserratRegular);
        txtOngkir = findViewById(R.id.txtOngkir);
        txtOngkir.setTypeface(MontserratRegular);
        ongkir = findViewById(R.id.ongkir);
        ongkir.setTypeface(MontserratRegular);
        txtTotal = findViewById(R.id.txtTotal);
        txtTotal.setTypeface(MontserratRegular);
        total = findViewById(R.id.total);
        total.setTypeface(MontserratRegular);
        blockAlamat = findViewById(R.id.blockAlamat);
        blockWaktuPesanan = findViewById(R.id.blockWaktuPesanan);
        blockOngkir = findViewById(R.id.blockOngkir);
        blockMetode = findViewById(R.id.blockMetode);
        blockMetodePembayaran = findViewById(R.id.blockMetodePembayaran);
        btnPesan = findViewById(R.id.btnPesan);
        icBack = findViewById(R.id.icBack);
        kosong2 = findViewById(R.id.kosong2);
        blockPengiriman = findViewById(R.id.blockPengiriman);
        txtPengiriman = findViewById(R.id.txtPengiriman);
        txtPengiriman.setTypeface(MontserratRegular);
        blockMetodePengiriman = findViewById(R.id.blockMetodePengiriman);
        promo = findViewById(R.id.promo);
        blockDiskon = findViewById(R.id.blockDiskon);
        txtDiskon = findViewById(R.id.txtDiskon);
        txtDiskon.setTypeface(MontserratRegular);
        diskon = findViewById(R.id.diskon);
        diskon.setTypeface(MontserratRegular);
        add = findViewById(R.id.add);
        blockImagePembayaran = findViewById(R.id.blockImagePembayaran);
        blockImagePengiriman = findViewById(R.id.blockImagePengiriman);
        txtNoPesanan = findViewById(R.id.txtNoPesanan);
        txtNoPesanan.setTypeface(MontserratRegular);
        txtDetailAlamat = findViewById(R.id.txtDetailAlamat);
        txtDetailAlamat.setTypeface(MontserratRegular);
        txtLokasi = findViewById(R.id.txtLokasi);
        txtLokasi.setTypeface(MontserratRegular);


        list = new ArrayList<>();
        blockPesanan= findViewById(R.id.blockPesanan);

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_pembayaran == 0) {
                    Toast.makeText(getApplicationContext(), "Silihkan pilih salah satu metode pembayaran", Toast.LENGTH_LONG).show();
                } else {
                    if (pesanan == 1){
                        if (nama_pembayaran.equals("Cash")){
                            Toast.makeText(getApplicationContext(), "Tidak dapat memilih metode pembayaran tunai jika orderan dikirimkan", Toast.LENGTH_LONG).show();
                        }
                        else {
                            new simpanorder().execute();
                        }
                    }else {
                        new simpanorder().execute();
                    }

                }
            }
        });

        if (pesanan == 1){
            jenis = "antar";
            txtLokasi.setVisibility(View.GONE);
            blockMetode.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
            txtDikirim.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.white));
            blockAlamat.setVisibility(View.VISIBLE);
            txtInfo.setText("Pesanan akan dikirim ke");
            blockOngkir.setVisibility(View.VISIBLE);
            blockPengiriman.setVisibility(View.VISIBLE);
            kosong2.setVisibility(View.VISIBLE);


            blockAlamat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new AlertDialog.Builder(Checkout.this, R.style.DialogPutih);
                    inflater = getLayoutInflater();
                    dialogView = inflater.inflate(R.layout.popup_pilih_outlet, null);
                    dialog.setView(dialogView);
                    blockOutlet = dialogView.findViewById(R.id.blockOutlet);
                    TextView txtTitle = dialogView.findViewById(R.id.pilihan_title);
                    txtTitle.setText("Pilih Alamat");
                    TextView txtTambahAlamat = dialogView.findViewById(R.id.tambahAlamat);
                    txtTambahAlamat.setVisibility(View.VISIBLE);
                    pBar = dialogView.findViewById(R.id.pBar);
                    alertDialog = dialog.create();

                    txtTambahAlamat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MenentukanAlamat.class);
                            String dari = "menu";
                            editor.putString("dari", dari);
                            editor.apply();
                            startActivity(intent);
                        }
                    });

                    new GetOutlet().execute();

                    alertDialog.show();
                }
            });
        }

        if (pesanan == 2) {
            jenis = "ambil";
            txtAmbilSendrii.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
            txtAmbilSendrii.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.white));
            blockAlamat.setVisibility(View.VISIBLE);

            blockAlamat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new AlertDialog.Builder(Checkout.this, R.style.DialogPutih);
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
        }

        txtDikirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesanan = 1;
                a = new Intent(getApplicationContext(), Checkout.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                a.putExtra("pesanan", pesanan);
                startActivity(a);
            }
        });

        txtAmbilSendrii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesanan = 2;
                b = new Intent(getApplicationContext(), Checkout.class);
                b.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                b.putExtra("pesanan", pesanan);
                startActivity(b);
            }
        });

        txtTambahPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DaftarMenu.class);
                intent.putExtra("id_kategori", "1");
                intent.putExtra("from", "1");
                startActivity(intent);
            }
        });

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        blockPesanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modal_pesanan modalPesanan = (modal_pesanan) parent.getItemAtPosition(position);
                click =1;
                id_size = modalPesanan.getId_size();
                id_addons = modalPesanan.getId_addons();
                id_menu = modalPesanan.getId_menu();
                sNama = modalPesanan.getNama();
                sThumbnail = modalPesanan.getThumbnail();
                sKeterangan = modalPesanan.getKeterangan();
                sHarga = modalPesanan.getHarga_size();
                harga_addons = modalPesanan.getHarga_addons();
                values = Integer.valueOf(modalPesanan.getQty());
                session = modalPesanan.getSession();
                showDetailMenu();
                alertDialog.show();

            }
        });

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (kode != ""){
                    intent = new Intent(getApplicationContext(), Detail_inbox.class);
                    intent.putExtra("status", "batalkan");
                }else {
                    intent = new Intent(getApplicationContext(), Voucher.class);
                    intent.putExtra("status", "tambah");
                }
                intent.putExtra("pesanan", pesanan);
                startActivity(intent);
            }
        });

        new GetCheckout().execute();

    }

    @Override
    public void onBackPressed() {
        if (menu == 0) {
            if (asal == 0) {
                if (hasil == 0) {
                    new DeleteTempCart().execute();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DaftarMenu.class);
                    intent.putExtra("id_kategori", "1");
                    intent.putExtra("from", "0");
                    editor.putString("kode", "");
                    editor.apply();
                    startActivity(intent);
                }

            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), Checkout.class);
            startActivity(intent);
        }

    }

    private class GetOutlet extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("pesanan", pesanan.toString()));
            if (pesanan == 2) {
                    params.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
                    params.add(new BasicNameValuePair("long", String.valueOf(longtitude)));

            }else {
                params.add(new BasicNameValuePair("lat", String.valueOf(lat_outlet)));
                params.add(new BasicNameValuePair("long", String.valueOf(long_outlet)));
            }
            params.add(new BasicNameValuePair("noHp", noHp));

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

                            LinearLayout linearLayout= new LinearLayout(Checkout.this);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setId(View.generateViewId());

                            for (int i=0; i < daftar.length(); i++){

                                final JSONObject outlet = daftar.getJSONObject(i);

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                                layoutParams.setMargins((int) (20 * factor), 0, (int) (15 * factor), (int) (15 * factor));
                                linearLayout.setLayoutParams(layoutParams);

                                ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView nama= new TextView(Checkout.this);
                                nama.setLayoutParams(layoutNama);
                                nama.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), 0);
                                nama.setTextSize(16);
                                nama.setAllCaps(true);
                                nama.setText(outlet.getString("nama"));
                                nama.setTextColor(getResources().getColor(android.R.color.black));
                                nama.setMaxLines(1);
                                nama.setEllipsize(TextUtils.TruncateAt.END);
                                linearLayout.addView(nama);

                                ViewGroup.LayoutParams layoutJarak = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView jarak= new TextView(Checkout.this);
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

                                TextView alamat = new TextView(Checkout.this);
                                alamat.setLayoutParams(layoutAlamat);
                                alamat.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), (int) (8 * factor));
                                alamat.setTextSize(14);
                                if (pesanan == 2){
                                    alamat.setText(outlet.getString("lokasi") + ", " + outlet.getString("alamat"));}
                                else {
                                    alamat.setText( outlet.getString("alamat"));
                                }
                                alamat.setMaxLines(3);
                                alamat.setEllipsize(TextUtils.TruncateAt.END);
                                linearLayout.addView(alamat);

                                if (pesanan == 2){
                                    nama.setOnClickListener(new selesai(outlet.getInt("id"), "outlet", "", "", outlet.getString("nama"), outlet.getString("jarak")));
                                    alamat.setOnClickListener(new selesai(outlet.getInt("id"), "outlet","","", outlet.getString("nama"), outlet.getString("jarak"))); }
                                else {
                                    nama.setOnClickListener(new selesai(outlet.getInt("id"), "alamat", outlet.getString("alamat"), outlet.getString("koordinat"), outlet.getString("nama"), outlet.getString("jarak")));
                                    alamat.setOnClickListener(new selesai(outlet.getInt("id"), "alamat", outlet.getString("alamat"), outlet.getString("koordinat"), outlet.getString("nama"), outlet.getString("jarak")));
                                }
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
        Integer id_pilih;
        String kat, alamat, koordinat, nama, jarak;
        public selesai(Integer id, String kat, String alamat, String koordinat, String nama, String jarak) {
            this.id_pilih = id;
            this.kat = kat;
            this.alamat = alamat;
            this.koordinat = koordinat;
            this.nama = nama;
            this.jarak = jarak;
        }

        @Override
        public void onClick(View v) {
            if (kat == "outlet"){

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Checkout.this);
                builder1.setTitle("Keranjang Belanja Tidak Kosong");
                builder1.setMessage("Jika anda mengganti outlet, maka keranjang belanja anda akan dikosongkan. Apakah anda akan berganti outlet?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Iya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editor.putInt("id_outlet", id_pilih);
                                editor.putFloat("jarak2", Float.parseFloat(jarak));
                                pesanan = 2;
                                id_outlet = id;
                                editor.apply();
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

            }
            else {
                //String[] area = alamat.split(",", 2);
                String[] titik = koordinat.split(",");
                lat_alamat = Double.parseDouble(titik[0]);
                long_alamat = Double.parseDouble(titik[1]);
                pesanan = 1;
                editor.putString("nama_alamat", nama);
                editor.putInt("id_alamat", id_pilih);
                editor.putString("alamat", alamat);
                editor.putString("lat_alamat", String.valueOf(lat_alamat));
                editor.putString("long_alamat", String.valueOf(long_alamat));
                editor.putInt("jarak", (int) Math.ceil(Float.parseFloat(jarak)));
                editor.apply();
                Intent intent = new Intent(Checkout.this, Checkout.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("pesanan", pesanan);
                startActivity(intent);
            }
        }

    }

    private class GetCheckout extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("jenis", "menu"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetCheckout, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Checkout.this);
            pDialog.setMessage("Mengambil data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    }else {
                        hasil = 0;
                        if (result.getString("koordinat").equals("null")) {
                            cekdiskon();

                        } else {

                            Integer banyak = result.getInt("banyak");
                            if (banyak > 0) {
                                JSONArray daftarPesanan = new JSONArray(result.getString("pesanan"));
                                if (daftarPesanan.length() > 0) {
                                    for (int k = 0; k < daftarPesanan.length(); k++) {
                                        final JSONObject pesanan = daftarPesanan.getJSONObject(k);

                                        modal_pesanan modalPesanan = new modal_pesanan();
                                        modalPesanan.setNama(pesanan.getString("nama"));
                                        modalPesanan.setHarga(pesanan.getString("harga"));
                                        modalPesanan.setSize(pesanan.getString("size"));
                                        modalPesanan.setAddons(pesanan.getString("addons"));
                                        modalPesanan.setQty(pesanan.getString("qty"));
                                        modalPesanan.setThumbnail(pesanan.getString("thumbnail"));
                                        modalPesanan.setId_menu(pesanan.getInt("id_menu"));
                                        modalPesanan.setId_size(pesanan.getInt("id_size"));
                                        modalPesanan.setId_addons(pesanan.getInt("id_addons"));
                                        modalPesanan.setSession(pesanan.getString("session"));
                                        modalPesanan.setKeterangan(pesanan.getString("keterangan"));
                                        modalPesanan.setHarga_size(pesanan.getInt("harga_size"));
                                        modalPesanan.setHarga_addons(pesanan.getInt("harga_addons"));
                                        Integer sub = pesanan.getInt("harga");
                                        hasil = hasil + sub;
                                        editor.putInt("hasil", hasil);
                                        editor.apply();
                                        list.add(modalPesanan);
                                        adapterPesanan = new adapter_pesanan(Checkout.this, R.layout.cv_daftar_pesanan, list);
                                        blockPesanan.setAdapter(adapterPesanan);
                                        Helper.getListViewSize(blockPesanan);
                                    }
                                }
                            } else {
                                txtNoPesanan.setVisibility(View.VISIBLE);
                            }

                            JSONArray metodePengiriman = new JSONArray(result.getString("pengiriman"));
                            if (metodePengiriman.length() > 0) {
                                for (int i = 0; i < metodePengiriman.length(); i++) {

                                    final JSONObject pengiriman = metodePengiriman.getJSONObject(i);

                                    final Integer id = pengiriman.getInt("id_pengiriman");
                                    final Integer harga = pengiriman.getInt("harga_per_km");

                                    ImageView thumbnail = new ImageView(Checkout.this);
                                    thumbnail.setPadding((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));
                                    Picasso.get().load(dataUrl + "metode/" + pengiriman.getString("thumbnail")).into(thumbnail);

                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (100 * factor), (int) (50 * factor));
                                    layoutParams.setMargins((int) (5 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));
                                    thumbnail.setLayoutParams(layoutParams);

                                    blockImagePengiriman.addView(thumbnail);

                                    LinearLayout.LayoutParams layoutButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * factor));
                                    layoutButton.setMargins((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));


                                    final RadioButton rdBtn = new RadioButton(Checkout.this);
                                    rdBtn.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
                                    rdBtn.setLayoutParams(layoutButton);
                                    rdBtn.setPadding((int) (0 * factor), (int) (0 * factor), (int) (0 * factor), (int) (0 * factor));
                                    rdBtn.setId(id);

//                                rdBtn.setBackgroundResource(getResources().getColor(R.color.colorPrimaryDark));
                                    blockMetodePengiriman.addView(rdBtn);

                                    if (id_pengiriman == rdBtn.getId()) {
                                        Log.d("jarak", String.valueOf(jarak));
                                        rdBtn.setChecked(true);
                                        harga_per_km = harga;
                                        editor.putInt("harga_per_km", harga_per_km);
                                        editor.apply();
                                    }

                                    rdBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            harga_per_km = harga;
                                            id_pengiriman = rdBtn.getId();
                                            editor.putInt("id_pengiriman", id_pengiriman);
                                            editor.apply();

                                            cekdiskon();
                                            }

                                    });

                                }
                            }

                            JSONArray metodePembayaran = new JSONArray(result.getString("pembayaran"));
                            if (metodePembayaran.length() > 0) {
                                for (int i = 0; i < metodePembayaran.length(); i++) {

                                    final JSONObject pembayaran = metodePembayaran.getJSONObject(i);

                                    final Integer id = pembayaran.getInt("id_pembayaran");
                                    final String ket = pembayaran.getString("keterangan_pembayaran");
                                    final String nama = pembayaran.getString("nama");
                                    final String no = pembayaran.getString("no_tujuan");
                                    final String a_n = pembayaran.getString("atas_nama");

                                    ImageView thumbnail = new ImageView(Checkout.this);
                                    thumbnail.setPadding((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));
                                    Picasso.get().load(dataUrl + "metode/" + pembayaran.getString("thumbnail")).into(thumbnail);

                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (100 * factor), (int) (50 * factor));
                                    layoutParams.setMargins((int) (5 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));
                                    thumbnail.setLayoutParams(layoutParams);

                                    blockImagePembayaran.addView(thumbnail);

                                    LinearLayout.LayoutParams layoutButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * factor));
                                    layoutButton.setMargins((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));

                                    final RadioButton rdBtn = new RadioButton(Checkout.this);
                                    rdBtn.setLayoutParams(layoutButton);
                                    rdBtn.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
                                    rdBtn.setPadding((int) (0 * factor), (int) (0 * factor), (int) (0 * factor), (int) (0 * factor));
                                    rdBtn.setId(pembayaran.getInt("id_pembayaran"));


                                    if (id_pembayaran == rdBtn.getId()) {
                                        rdBtn.setChecked(true);
                                    }

                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams1.setMargins((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));

                                    rdBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            id_pembayaran = rdBtn.getId();
                                            keterangan = ket;
                                            nama_pembayaran = nama;
                                            no_tujuan = no;
                                            atas_nama = a_n;
                                            Log.d("pembayaran", id_pembayaran.toString());
                                            editor.putString("keterangan", keterangan);
                                            editor.putString("nama_pembayaran", nama_pembayaran);
                                            editor.putInt("id_pembayaran", id_pembayaran);
                                            editor.putString("no_tujuan", no_tujuan);
                                            editor.apply();
                                        }
                                    });

                                    blockMetodePembayaran.addView(rdBtn);
                                }
                            }

                            sAlamat = result.getString("alamat");
                            Log.d("alamat", sAlamat);

                            id_outlet = result.getInt("id_outlet");
                            editor.putInt("id_outlet", id_outlet).apply();

                            String titik = result.getString("koordinat");
                            String[] koordinat = titik.split(",");
                            double titikLatitude = Double.parseDouble(koordinat[0]);
                            lat_outlet = titikLatitude;
                            double titikLongtitude = Double.parseDouble(koordinat[1]);
                            long_outlet = titikLongtitude;
                            editor.putString("lat_outlet", String.valueOf(lat_outlet));
                            editor.putString("long_outlet", String.valueOf(long_outlet));
                            editor.apply();
                            final float[] hasill = new float[1];
                            if (pesanan == 2) {
                                nama_outlet = result.getString("nama_outlet");
                                lokasi = result.getString("lokasi");
                                detail_outlet = result.getString("alamat_outlet");
                                new hitungjarak().execute(String.valueOf(latitude), String.valueOf(longtitude));
                            } else {
                                if (id_alamat != 0) {
                                    new hitungjarak().execute(String.valueOf(lat_alamat), String.valueOf(long_alamat));
                                } else {
                                    String namaarea[] = detailAlamat.split(",");
                                    Location.distanceBetween(titikLatitude, titikLongtitude, latitude, longtitude, hasill);
                                    txtAlamat.setText(nama_alamat + " ( " + decimalFormat.format(hasill[0] / 1000) + " KM )");
                                    jarak = (int) Math.ceil(hasill[0] / 1000);
                                    txtDetailAlamat.setText(detailAlamat);

                                    cekdiskon();
                                }
                            }



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

    private class hitungjarak extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("lat_alamat", args[0]));
            params.add(new BasicNameValuePair("long_alamat", args[1]));
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
                        if (pesanan == 1){
                            jarak = (int) Math.ceil(Float.parseFloat(result.getString("jarak")));
                            editor.putInt("jarak", jarak);
                            editor.apply();
                            txtAlamat.setText(nama_alamat + " ( " + decimalFormat.format(Float.parseFloat(result.getString("jarak"))) + " KM )");
                             txtDetailAlamat.setText(alamat);
                        }else{
                            distance = Float.parseFloat(result.getString("jarak"));
                            editor.putFloat("jarak2", distance);
                            editor.apply();
                            txtAlamat.setText(nama_outlet + " (" + decimalFormat.format(distance) + " KM )");
                            txtLokasi.setText(lokasi);
                            txtDetailAlamat.setText(detail_outlet);
                        }

                        cekdiskon();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class GantiPesanan extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGantiPesanan, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null){
                    if (jsonObject.getInt("error") == 1){

                    }else {
                       // Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Checkout.this, Checkout.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("pesanan", pesanan);
                        startActivity(intent);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    public void showDetailMenu(){

        Log.d("size", id_size.toString());

        dialog = new AlertDialog.Builder(Checkout.this, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
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
        TextView namaProduk = dialogView.findViewById(R.id.namaProduk);
        namaProduk.setTypeface(MontserratRegular);
        TextView detailProduk = dialogView.findViewById(R.id.detailProduk);
        detailProduk.setTypeface(MontserratRegular);
        TextView txtTotall = dialogView.findViewById(R.id.txtTotal);
        txtTotall.setTypeface(MontserratRegular);
        final ImageView btnOrder = dialogView.findViewById(R.id.btnOrder);
       // btnOrder.setTypeface(MontserratRegular);
        TextView txtSize = dialogView.findViewById(R.id.txtSize);
        txtSize.setTypeface(MontserratRegular);
        ImageView thumbnail_detail = dialogView.findViewById(R.id.thumbnail);
        totall = dialogView.findViewById(R.id.total);
        ImageView icBack = dialogView.findViewById(R.id.icBack);
        blockShoot = dialogView.findViewById(R.id.blockShoot);
        blockSize = dialogView.findViewById(R.id.blockSize);
        increment = dialogView.findViewById(R.id.increment);
        decrement = dialogView.findViewById(R.id.decrement);
        display = dialogView.findViewById(R.id.display);
        blockatas = dialogView.findViewById(R.id.blockAtas);
        blockDetail = dialogView.findViewById(R.id.blockDetail);
        blockQty = dialogView.findViewById(R.id.blockQty);
        ImageView btnHapus = dialogView.findViewById(R.id.btnHapus);
        btnHapus.setVisibility(View.VISIBLE);

        namaProduk.setText(sNama);
        detailProduk.setText(sKeterangan);
        Picasso.get().load(dataUrl + "menu/thumbnail/" + sThumbnail).into(thumbnail_detail);
        subtotal = (sHarga + harga_addons) * values;
        totall.setText("Rp " + format.format(subtotal));

        display.setText(String.valueOf(values));

        increment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (values >= downrange && values <= uprange)
                    values += 1;
                if (values > uprange)
                    values = downrange;
                display.setText(String.valueOf(values));

                subtotal = (sHarga + harga_addons) * values;
                totall.setText("Rp " + format.format(subtotal));


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
                totall.setText("Rp " + format.format(subtotal));
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
                new Pesan().execute();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = 0;
                new Pesan().execute();
            }
        });

        new GetDetailMenu().execute(id_menu.toString());

    }

    private class GetDetailMenu extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Checkout.this);
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
//                                               namaProduk.setText(result.getString("nama"));
//                        detailProduk.setText(result.getString("keterangan"));
//                        totall.setText("Rp " + decimalFormat.format(Integer.valueOf(result.getString("harga_jual"))));
//                        String total = totall.getText().toString();
//                        String total1 = total.replace("Rp ", "");
//                        String total2 = total1.replace(",","");
//                        Picasso.get().load(dataUrl + "menu/thumbnail/" + result.getString("thumbnail")).into(thumbnail_detail);

                        JSONArray size = new JSONArray(result.getString("size"));
                        if (size.length() > 0) {
                            for (int i = 0; i < size.length(); i++) {

                                final JSONObject menu = size.getJSONObject(i);

                                final Integer idsize = menu.getInt("id_size");

                                final LinearLayout daftar = new LinearLayout(Checkout.this);
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

                                final TextView nama = new TextView(Checkout.this);
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

//                                daftar.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (pilih == 0){
//                                            daftar.setBackground(getResources().getDrawable(R.drawable.border_white));
//                                            nama.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                                            pilih = 1;
//                                            if (Integer.parseInt(totall.getText().toString()) == subtotal){
//                                                totall.setText(harga_size.toString());
//                                            }
//                                            else {
//                                                if (addons == 1){
//                                                    Integer jumlah = harga_size + harga_addons;
//                                                    totall.setText(jumlah.toString());
//                                                } else {
//                                                    Integer jumlah = Integer.parseInt(totall.getText().toString()) + harga_addons;
//                                                    totall.setText(jumlah.toString());
//                                                }
//                                            }
//                                            }
//                                        else{
//                                            daftar.setBackground(getResources().getDrawable(R.drawable.border));
//                                            nama.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
//                                            pilih = 0;
//                                                totall.setText(totall.getText().toString());
//                                        }
//                                    }
//                                });

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

                                    final LinearLayout daftar = new LinearLayout(Checkout.this);
                                    daftar.setWeightSum(addOns.length());
                                    daftar.setBackground(getResources().getDrawable(R.drawable.border));
                                    daftar.setPadding((int) (1 * factor), (int) (2 * factor), (int) (5 * factor), (int) (5 * factor));
                                    daftar.setOrientation(LinearLayout.VERTICAL);

                                    ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    final TextView nama = new TextView(Checkout.this);
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


//                                if (id_add_ons.equals(null)) {
//                                    if (id_addons.equals(id_add_ons)) {
//                                        daftar.setBackground(getResources().getDrawable(R.drawable.border_white));
//                                        nama.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                                        produk.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                                    }
//                                }

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


//                                    daftar.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if (click == 0) {
//                                                daftar.setBackground(getResources().getDrawable(R.drawable.border_white));
//                                                nama.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                                                produk.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
//                                                click = 1;
//                                               Integer jumlah = Integer.parseInt(totall.getText().toString()) + harga_addons;
//                                                totall.setText(jumlah.toString());
//                                                addons = 1;
//                                                id_addons = id_add_ons;
//                                            } else {
//                                                daftar.setBackground(getResources().getDrawable(R.drawable.border));
//                                                nama.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
//                                                produk.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
//                                                click = 0;
//                                                Integer jumlah = Integer.parseInt(totall.getText().toString()) - harga_addons;
//                                                totall.setText(jumlah.toString());
//                                            }
//                                        }
//                                    });

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

            pDialog = new ProgressDialog(Checkout.this);
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
//                        hasil = hasil + subtotal;
//                        Log.d("hasil", hasil.toString());
//                        editor.putInt("hasil", hasil);
//                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), Checkout.class);
                        intent.putExtra("pesanan", pesanan);
                        startActivity(intent);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class simpanorder extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
            params.add(new BasicNameValuePair("id_alamat", String.valueOf(id_alamat)));
            params.add(new BasicNameValuePair("jenis", jenis));
            params.add(new BasicNameValuePair("id_pembayaran", id_pembayaran.toString()));
            params.add(new BasicNameValuePair("hargaOngkir", String.valueOf(hargaOngkir)));
            params.add(new BasicNameValuePair("id_voucher", String.valueOf(id_voucher)));
            params.add(new BasicNameValuePair("id_pengiriman", String.valueOf(id_pengiriman)));
            params.add(new BasicNameValuePair("alamat", detailAlamat));
            params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
            params.add(new BasicNameValuePair("longtitude", String.valueOf(longtitude)));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlsimpanorder, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Checkout.this);
            pDialog.setMessage("Mengirim data...");
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

                    }else {
                        Intent intent = new Intent(getApplicationContext(), AfterOrder.class);
                        intent.putExtra("from", 1);
                        intent.putExtra("id_orderan", result.getString("id_orderan"));
                        intent.putExtra("keterangan", keterangan);
                        intent.putExtra("total", hargaTotal);
                        intent.putExtra("no_tujuan", no_tujuan);
                        intent.putExtra("atas_nama", atas_nama);
                        intent.putExtra("from", 0);
                        sharedPreferences.edit().remove("hasil").apply();
                        sharedPreferences.edit().remove("id_pembayaran").apply();
                        sharedPreferences.edit().remove("id_pengiriman").apply();
                        sharedPreferences.edit().remove("total").apply();
                        sharedPreferences.edit().remove("ongkir").apply();
                        sharedPreferences.edit().remove("harga_per_km").apply();
                        sharedPreferences.edit().remove("jarak").apply();
                        sharedPreferences.edit().remove("id_voucher").apply();
                        sharedPreferences.edit().remove("kode").apply();
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
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void cekdiskon(){
            pDialog.dismiss();
            pDialog = null;

        if (hasil == 0) {
            Intent intent = new Intent(getApplicationContext(), KeranjangBelanja.class);
            sharedPreferences.edit().remove("hasil").apply();
            sharedPreferences.edit().remove("id_pembayaran").apply();
            sharedPreferences.edit().remove("id_pengiriman").apply();
            sharedPreferences.edit().remove("total").apply();
            sharedPreferences.edit().remove("ongkir").apply();
            sharedPreferences.edit().remove("harga_per_km").apply();
            sharedPreferences.edit().remove("jarak").apply();
            sharedPreferences.edit().remove("id_voucher").apply();
            sharedPreferences.edit().remove("kode").apply();
            startActivity(intent);
        }
        else {
            //ongkir
            if (pesanan == 1) {
                if (jarak <= 4) {
                    hargaOngkir = 8000;
                } else {
                    hargaOngkir = harga_per_km * jarak;
                }
            } else {
                hargaOngkir = 0;
            }

            //promo
            if (id_voucher == 0) {
                add.setVisibility(View.VISIBLE);
                txtTambahPromo.setText("Promo");
                blockDiskon.setVisibility(View.GONE);
                hargaTotal = hasil + hargaOngkir;
            } else {
                add.setVisibility(View.GONE);
                blockDiskon.setVisibility(View.VISIBLE);
                txtTambahPromo.setText(kode);

                if (hasil >= min) {
                    Log.d("min", min.toString());

                    if (jenis_promo.equals("nominal")) {
                        hargaDiskon = nilai;
                        Log.d("jenis", jenis_promo);

                    } else {
                        hargaDiskon = (hasil * nilai / 1000);
                        Log.d("diskon", hargaDiskon.toString());
                        if (hargaDiskon > maks) {
                            hargaDiskon = maks;
                        }
                    }
                    diskon.setText("- Rp " + format.format(hargaDiskon));
                    editor.putInt("diskon", hargaDiskon);
                    editor.apply();

                } else {
                    txtTambahPromo.setText("Promo");
                    hargaDiskon = 0;
                    blockDiskon.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Maaf, pesanan Anda belum mencapai jumlah minimum pembelian", Toast.LENGTH_LONG).show();
                    editor.putString("kode", "");
                    editor.putInt("id_voucher", 0);
                    editor.apply();
                }
                //total
                hargaTotal = (hasil + hargaOngkir) - hargaDiskon;
            }

            ongkir.setText("Rp " + format.format(hargaOngkir));
            harga.setText("Rp " + format.format(hasil));
            total.setText("Rp " + format.format(hargaTotal));
        }

        editor.putInt("total", hargaTotal);
        editor.putInt("hasil", hasil);
        editor.apply();

    }


}
