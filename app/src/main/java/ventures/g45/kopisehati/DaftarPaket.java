package ventures.g45.kopisehati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ventures.g45.kopisehati.adapter.adapter_berlangganan;
import ventures.g45.kopisehati.adapter.adapter_pesanan;
import ventures.g45.kopisehati.modal.modal_berlangganan;
import ventures.g45.kopisehati.modal.modal_menu;

public class DaftarPaket extends AppCompatActivity {

    TextView txtTitle, nama_paket, hargaawal, total;
    ImageView icBack;
    ListView list_paket, list_menu;
    Typeface MontserratRegular;
    private List<modal_berlangganan> list;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Integer id_outlet, id_paket, harga_awal, harga_akhir;
    JSONParser jsonParser = new JSONParser();
    String urlGetDaftarPaket, defaultUrl, dataUrl, status, urlDetailPaket, urlPesan, noHp, urlCekCart, urlDeleteTempCart, sNamaPaket;
    adapter_berlangganan adapterBerlangganan;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    AlertDialog alertDialog;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    float factor;
    LinearLayout isi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_paket);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        id_outlet = sharedPreferences.getInt("id_outlet", 0);
        noHp = sharedPreferences.getString("noHp", "");

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlGetDaftarPaket = defaultUrl + "getdaftarpaket.html";
        urlDetailPaket = defaultUrl + "getdetailpaket.html";
        urlPesan = defaultUrl + "simpantempberlangganan.html";
        urlCekCart = defaultUrl + "cektempcart.html";
        urlDeleteTempCart = defaultUrl + "deletetemp.html";

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        factor = getResources().getDisplayMetrics().density;
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        icBack = findViewById(R.id.icBack);
        list_paket = findViewById(R.id.list_item);


        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        list_paket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modal_berlangganan modalBerlangganan= (modal_berlangganan) parent.getItemAtPosition(position);
                id_paket = modalBerlangganan.getId_paket();

                dialog = new AlertDialog.Builder(DaftarPaket.this, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.detail_paket, null);
                dialog.setView(dialogView);
                ImageView icBack = dialogView.findViewById(R.id.icBack);
                nama_paket = dialogView.findViewById(R.id.txtNama);
                nama_paket.setTypeface(MontserratRegular);
                list_menu = dialogView.findViewById(R.id.list_item);
                TextView txtTotal = dialogView.findViewById(R.id.txtTotal);
                txtTotal.setTypeface(MontserratRegular);
                hargaawal = dialogView.findViewById(R.id.hargaAwal);
                hargaawal.setTypeface(MontserratRegular);
                hargaawal.setTypeface(null, Typeface.ITALIC);
                total = dialogView.findViewById(R.id.total);
                total.setTypeface(MontserratRegular);
                TextView txtInfo = dialogView.findViewById(R.id.txtInfo);
                txtInfo.setTypeface(MontserratRegular);
                Button btnBerlangganan = dialogView.findViewById(R.id.btnBerlangganan);
                btnBerlangganan.setTypeface(MontserratRegular);
                ImageView thumbnail = dialogView.findViewById(R.id.thumbnail);

                sNamaPaket = modalBerlangganan.getNama();
                harga_awal = modalBerlangganan.getNormal_price();
                harga_akhir = modalBerlangganan.getSubscribe_price();

                Picasso.get().load(dataUrl + "paket/list/" + modalBerlangganan.getThumbnail()).into(thumbnail);

                if (modalBerlangganan.getOngkos_kirim() == 0){
                    txtInfo.setText("exclude ongkos kirim");
                }else {
                    txtInfo.setText("include ongkos kirim");
                }

                alertDialog = dialog.create();
                status = "detail";
                editor.putString("status", status);
                editor.apply();

                btnBerlangganan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CekCart().execute();
                    }
                });

                new GetDetailPaket().execute();


                icBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        status = "daftar";
        editor.putString("status", status);
        editor.apply();
        new GetPaket().execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private class GetPaket extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetDaftarPaket, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DaftarPaket.this);
            pDialog.setMessage("Mengambil data...");
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
                    if (result.getInt("error") == 1){

                    }else {
                        list = new ArrayList<>();

                        JSONArray daftar1 = new JSONArray(result.getString("paket"));
                        for (int i = 0; i < daftar1.length(); i++) {
                            final JSONObject paket1 = daftar1.getJSONObject(i);

                            modal_berlangganan modalBerlangganan = new modal_berlangganan();
                            modalBerlangganan.setNama(paket1.getString("nama_paket"));
                            modalBerlangganan.setThumbnail(paket1.getString("thumbnail"));
                            modalBerlangganan.setId_paket(paket1.getInt("id_paket"));
                            modalBerlangganan.setNormal_price(paket1.getInt("normal_price"));
                            modalBerlangganan.setSubscribe_price(paket1.getInt("subscribe_price"));
                            modalBerlangganan.setOngkos_kirim(paket1.getInt("ongkos_kirim"));
                            modalBerlangganan.setPeriode(paket1.getInt("periode"));
                            modalBerlangganan.setIsi(paket1.getString("keterangan"));

                            list.add(modalBerlangganan);
                            adapterBerlangganan = new adapter_berlangganan(DaftarPaket.this, R.layout.cv_paket, list);
                            list_paket.setAdapter(adapterBerlangganan);
                        }

                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class GetDetailPaket extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
            params.add(new BasicNameValuePair("id_paket", id_paket.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlDetailPaket, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DaftarPaket.this);
            pDialog.setMessage("Mengambil data...");
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
                    if (result.getInt("error") == 1){

                    }else {
                        alertDialog.show();
                        nama_paket.setText(sNamaPaket);
                        hargaawal.setText("Rp " + decimalFormat.format(harga_awal));
                        total.setText("Rp " + decimalFormat.format(harga_akhir));

                        list = new ArrayList<>();

                        JSONArray daftar = new JSONArray(result.getString("menu"));
                        for (int j = 0; j < daftar.length(); j++) {
                            final JSONObject paket1 = daftar.getJSONObject(j);

                            modal_berlangganan modalBerlangganan = new modal_berlangganan();
                            modalBerlangganan.setNama(paket1.getString("nama_menu"));
                            modalBerlangganan.setThumbnail(paket1.getString("thumbnail"));
                            modalBerlangganan.setId_paket(paket1.getInt("id_menu"));
                            modalBerlangganan.setQty(paket1.getInt("qty"));

                            list.add(modalBerlangganan);
                            adapterBerlangganan = new adapter_berlangganan(DaftarPaket.this, R.layout.cv_paket, list);
                            list_menu.setAdapter(adapterBerlangganan);
                            Helper.getListViewSize(list_menu);
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class CekCart extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("jenis", "berlangganan"));
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
                        if (jsonObject.getInt("cek") != 0){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(DaftarPaket.this);
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
            params.add(new BasicNameValuePair("id_menu", id_paket.toString()));


            JSONObject jsonObject = jsonParser.makeHttpRequest(urlPesan, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(DaftarPaket.this);
//            pDialog.setMessage("Mengirim data...");
//            pDialog.setCancelable(false);
//            pDialog.setIndeterminate(false);
//            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
//            if ((pDialog != null) && (pDialog.isShowing()))
//                pDialog.dismiss();
//            pDialog = null;

            try {
                if (result != null){
                    if (result.getInt("error")==1){

                    }else {

                        Intent intent = new Intent(getApplicationContext(),Berlangganan.class);
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
