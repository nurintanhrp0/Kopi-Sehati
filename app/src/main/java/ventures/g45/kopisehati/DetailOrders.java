package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ventures.g45.kopisehati.adapter.adapter_berlangganan;
import ventures.g45.kopisehati.adapter.adapter_pesanan;
import ventures.g45.kopisehati.modal.modal_berlangganan;
import ventures.g45.kopisehati.modal.modal_pesanan;

public class DetailOrders extends AppCompatActivity {

    Integer from, totall;
    String id_orderan, urlGetDetail, defaultUrl, dataUrl, ubah, urlUbahStatus, keterangan, no_tujuan, atas_nama;
    TextView txtTitle, jenis, txtInfo, txtAlamat, txtDaftarPesanan, txtPromo, promo, txtPembayaran, txtPengiriman, txtDetailPembayaran, txtHarga, harga, txtOngkir, ongkir, txtTotal, total, kosong, txtOutlet, outlet, txtStatus, tvStatus, txtStatusPembayaran, statusPembayaran, kosong4, txtQrcode, txtDiskon, diskon;
    ImageView pembayaran, pengiriman, icBack, qrcode;
    Typeface MontserratRegular;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    LinearLayout blockAlamat, blockPengiriman, blockOngkir, blockPromo, blockQrcode, blockDiskon;
    DecimalFormat format;
    ListView blockPesanan;
    List list;
    adapter_pesanan adapterPesanan;
    adapter_berlangganan adapterBerlangganan;
    Button btnBatalkan, btnKonfirmasi, help;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RelativeLayout blockStatussPembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_orders);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        format = new DecimalFormat("#,###.##");
        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();

        Intent intent = getIntent();
        from = intent.getIntExtra("from", 0);
        id_orderan = intent.getStringExtra("id_orderan");

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlGetDetail = defaultUrl + "getdetailorder.html";
        urlUbahStatus = defaultUrl + "getubahstatus.html";

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        jenis = findViewById(R.id.jenis);
        jenis.setTypeface(MontserratRegular);
        txtInfo = findViewById(R.id.txtInfo);
        txtInfo.setTypeface(MontserratRegular);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtAlamat.setTypeface(MontserratRegular);
        txtDaftarPesanan = findViewById(R.id.txtDaftarPesanan);
        txtDaftarPesanan.setTypeface(MontserratRegular);
        txtPromo = findViewById(R.id.txtPromo);
        txtPromo.setTypeface(MontserratRegular);
        txtPembayaran = findViewById(R.id.txtPembayaran);
        txtPembayaran.setTypeface(MontserratRegular);
        txtPengiriman = findViewById(R.id.txtPengiriman);
        txtPengiriman.setTypeface(MontserratRegular);
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
        blockPengiriman = findViewById(R.id.blockPengiriman);
        blockOngkir = findViewById(R.id.blockOngkir);
        list = new ArrayList<>();
        blockPesanan= findViewById(R.id.blockPesanan);
        pembayaran = findViewById(R.id.pembayaran);
        pengiriman = findViewById(R.id.pengriman);
        promo = findViewById(R.id.promo);
        promo.setTypeface(MontserratRegular);
        kosong = findViewById(R.id.kosong3);
        icBack = findViewById(R.id.icBack);
        txtOutlet = findViewById(R.id.txtOutlet);
        txtOutlet.setTypeface(MontserratRegular);
        outlet = findViewById(R.id.outlet);
        outlet.setTypeface(MontserratRegular);
        txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setTypeface(MontserratRegular);
        tvStatus = findViewById(R.id.tvStatus);
        tvStatus.setTypeface(MontserratRegular);
        txtStatusPembayaran = findViewById(R.id.txtStatusPembayaran);
        txtStatusPembayaran.setTypeface(MontserratRegular);
        statusPembayaran = findViewById(R.id.statusPembayaran);
        statusPembayaran.setTypeface(MontserratRegular);
        btnBatalkan = findViewById(R.id.btnBatalkan);
        btnBatalkan.setTypeface(MontserratRegular);
        btnKonfirmasi = findViewById(R.id.btnBKonfirmasi);
        btnKonfirmasi.setTypeface(MontserratRegular);
        blockPromo = findViewById(R.id.blockPromo);
        kosong4 = findViewById(R.id.kosong4);
        blockQrcode = findViewById(R.id.blockQrcode);
        txtQrcode = findViewById(R.id.txtQrcode);
        txtQrcode.setTypeface(MontserratRegular);
        qrcode = findViewById(R.id.qrcode);
        blockDiskon = findViewById(R.id.blockDiskon);
        txtDiskon = findViewById(R.id.txtDiskon);
        diskon = findViewById(R.id.diskon);
        blockStatussPembayaran = findViewById(R.id.blockStatusPembayaran);
        help = findViewById(R.id.help);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubah = "konfirmasi";
                new ubahStatus().execute();
            }
        });

        btnBatalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubah = "batal";
                new ubahStatus().execute();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), AfterOrder.class);
                        intent.putExtra("id_orderan", id_orderan);
                        intent.putExtra("keterangan", keterangan);
                        intent.putExtra("total", totall);
                        intent.putExtra("no_tujuan", no_tujuan);
                        intent.putExtra("atas_nama", atas_nama);
                        intent.putExtra("from", 1);
                        startActivity(intent);
            }
        });

        new GetDetailOrder().execute();
    }

    @Override
    public void onBackPressed() {
        if (from == 1){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else {
            super.onBackPressed();
        }
    }

    private class GetDetailOrder extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id_orderan", id_orderan));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetDetail, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DetailOrders.this);
            pDialog.setMessage("Mengambil data...");
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
                if (result != null) {

                    if (result.getInt("error") == 1) {

                    } else {
                        Integer ongkirr;
                        jenis.setText(result.getString("jenis"));
                        tvStatus.setText(result.getString("status_orderan"));
                        statusPembayaran.setText(result.getString("status_pembayaran"));
                        outlet.setText(result.getString("outlet") + ", " + result.getString("lokasi"));
                        Picasso.get().load(dataUrl + "metode/" + result.getString("pembayaran")).into(pembayaran);

                        keterangan = result.getString("keterangan_pembayaran");
                        no_tujuan = result.getString("no_tujuan");
                        atas_nama = result.getString("atas_nama");

                        if (result.getString("status_pembayaran").equals("lunas") || result.getString("status_orderan").equals("cancel")) {
                            help.setVisibility(View.GONE);
                        }

                        Integer hasil = 0, sub = 0;
                        if (result.getString("berlangganan").equals("null")) {
                            JSONArray daftarPesanan = new JSONArray(result.getString("pesanan"));
                            if (daftarPesanan.length() > 0) {
                                for (int k = 0; k < daftarPesanan.length(); k++) {
                                    final JSONObject pesanan = daftarPesanan.getJSONObject(k);

                                    modal_pesanan modalPesanan = new modal_pesanan();
                                    modalPesanan.setNama(pesanan.getString("nama"));
                                    modalPesanan.setSize(pesanan.getString("size"));
                                    modalPesanan.setAddons(pesanan.getString("addons"));
                                    modalPesanan.setQty(pesanan.getString("qty"));
                                    modalPesanan.setThumbnail(pesanan.getString("thumbnail"));
                                    modalPesanan.setHarga(pesanan.getString("subtotal"));
                                    sub = pesanan.getInt("subtotal");
                                    hasil = hasil + sub;
                                    list.add(modalPesanan);
                                    adapterPesanan = new adapter_pesanan(DetailOrders.this, R.layout.cv_daftar_pesanan, list);
                                    blockPesanan.setAdapter(adapterPesanan);
                                    Helper.getListViewSize(blockPesanan);
                                }
                            }
                        } else {
                            String status = "daftar";
                            editor.putString("status", status);
                            editor.apply();
                            modal_berlangganan modalBerlangganan = new modal_berlangganan();
                            modalBerlangganan.setThumbnail(result.getString("thumbnail"));
                            modalBerlangganan.setNama(result.getString("nama"));
                            modalBerlangganan.setIsi(result.getString("keterangan"));
                            modalBerlangganan.setPeriode(result.getInt("periode"));
                            modalBerlangganan.setNormal_price(result.getInt("normal_price"));
                            modalBerlangganan.setSubscribe_price(result.getInt("subscribe_price"));
                            hasil = result.getInt("subscribe_price");
                            list.add(modalBerlangganan);
                            adapterBerlangganan = new adapter_berlangganan(DetailOrders.this, R.layout.cv_paket, list);
                            blockPesanan.setAdapter(adapterBerlangganan);
                            Helper.getListViewSize(blockPesanan);

                            if (result.getString("status_pembayaran").equals("lunas")) {
                                Intent intent = new Intent(getApplicationContext(), DetailBerlangganan.class);
                                editor.putInt("id_paket", result.getInt("id_paket"));
                                editor.apply();
                                intent.putExtra("id_orderan", id_orderan);
                                intent.putExtra("nama_paket", result.getString("nama"));
                                startActivity(intent);
                            }
                        }


                        if (result.getString("jenis").equals("Dikirim")) {
                            blockAlamat.setVisibility(View.VISIBLE);
                            blockPengiriman.setVisibility(View.VISIBLE);
                            blockOngkir.setVisibility(View.VISIBLE);
                            kosong.setVisibility(View.VISIBLE);

                            //String[] area = result.getString("alamat").split(",", 2);
                            txtAlamat.setText(result.getString("alamat"));
                            Picasso.get().load(dataUrl + "metode/" + result.getString("pengiriman")).into(pengiriman);
                            ongkir.setText("Rp " + format.format(result.getInt("ongkos_kirim")));
                            ongkirr = result.getInt("ongkos_kirim");

                            totall = hasil + ongkirr;
                        } else {
                            blockQrcode.setVisibility(View.VISIBLE);
                            Picasso.get().load(dataUrl + "qr_code/" + result.getString("qrcode")).into(qrcode);
                            totall = hasil;
                        }

                        if (result.getString("voucher").equals("null")) {
                            blockPromo.setVisibility(View.GONE);
                            kosong4.setVisibility(View.VISIBLE);
                        } else {
                            blockDiskon.setVisibility(View.VISIBLE);
                            promo.setText(result.getString("voucher"));
                            Integer nilai = result.getInt("nilai_voucher");
                            if (result.getString("jenis_voucher").equals("nominal")) {
                                totall = totall - nilai;
                            } else {
                                nilai = (totall * nilai / 1000);
                                totall = totall - nilai;
                            }
                            diskon.setText("Rp " + format.format(nilai));
                        }

                        harga.setText("Rp " + format.format(hasil));
                        total.setText("Rp " + format.format(totall));

                        if (!result.getString("status_pembayaran").equals("belum dibayar") || result.getString("status_orderan").equals("cancel")) {
                            btnBatalkan.setVisibility(View.GONE);
                            btnKonfirmasi.setVisibility(View.GONE);
                        }
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

        }
    }

    private class ubahStatus extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("ubah", ubah));
            params.add(new BasicNameValuePair("id_orderan", id_orderan));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlUbahStatus, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DetailOrders.this);
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
                if (result !=  null){
                    if (result.getInt("error") == 1){

                    }else {
                        Intent intent = new Intent(getApplicationContext(), DetailOrders.class);
                        intent.putExtra("id_orderan", id_orderan);
                        intent.putExtra("from", 1);
                        startActivity(intent);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
