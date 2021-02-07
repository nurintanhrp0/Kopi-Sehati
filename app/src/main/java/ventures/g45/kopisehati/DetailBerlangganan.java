package ventures.g45.kopisehati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ventures.g45.kopisehati.adapter.adapter_berlangganan;
import ventures.g45.kopisehati.adapter.adapter_jadwal;
import ventures.g45.kopisehati.modal.modal_berlangganan;
import ventures.g45.kopisehati.modal.modal_inbox;
import ventures.g45.kopisehati.modal.modal_jadwal;

public class DetailBerlangganan extends AppCompatActivity {

    TextView txtTitle, txtJadwal, namaPaket, jam, display;
    ImageView icBack, thumbnail2;
    Typeface MontserratRegular;
    DecimalFormat format;
    ListView list_item, list_jadwal;
    String id_orderan, urlGetJadwal, defaultUrl, dataUrl, nama_paket, tampil="", hour, mnt, namaMenu, urlGetMax, id_jadwal, urlSimpanUpdate, nama_awal, jenis, jam_pengirimn;
    Integer id_paket;
    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;
    List list;
    adapter_berlangganan adapterBerlangganan;
    adapter_jadwal adapterJadwal;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    AlertDialog alertDialog;
    ProgressBar pBar;
    LinearLayout blockOutlet;
    float factor;
    Spinner sp_nama;
    String[] nama_menu;
    private int uprange = 0;
    private int downrange = 1;
    private int values = 1;
    String[] waktu, tanggal;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berlangganan);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        id_paket = sharedPreferences.getInt("id_paket", 0);

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlGetJadwal = defaultUrl + "getjadwalberlangganan.html";
        urlGetMax = defaultUrl + "getqty.html";
        urlSimpanUpdate = defaultUrl + "updateberlangganan.html";

        final Intent intent = getIntent();
        id_orderan = intent.getStringExtra("id_orderan");
        nama_paket = intent.getStringExtra("nama_paket");

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        format = new DecimalFormat("#,###.##");
        factor = getResources().getDisplayMetrics().density;

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        txtJadwal = findViewById(R.id.txtJadwal);
        txtJadwal.setTypeface(MontserratRegular);
        list_item = findViewById(R.id.list_item);
        list_jadwal = findViewById(R.id.list_jadwal);
        namaPaket = findViewById(R.id.namaPaket);
        namaPaket.setTypeface(MontserratRegular);
        icBack = findViewById(R.id.icBack);
        thumbnail2 = findViewById(R.id.thumbnail2);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        list_jadwal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    modal_jadwal modalJadwal = (modal_jadwal) parent.getItemAtPosition(position);
                    id_jadwal = modalJadwal.getId_jadwal();

                    if (modalJadwal.getStatus().equals("waiting")) {
                        dialog = new AlertDialog.Builder(DetailBerlangganan.this, R.style.DialogPutih);
                        inflater = getLayoutInflater();
                        dialogView = inflater.inflate(R.layout.popup_ubah_jadwal, null);
                        dialog.setView(dialogView);
                        TextView txtMenu = dialogView.findViewById(R.id.txtMenu);
                        txtMenu.setTypeface(MontserratRegular);
                        TextView txtJam = dialogView.findViewById(R.id.txtJam);
                        txtJam.setTypeface(MontserratRegular);
                        jam = dialogView.findViewById(R.id.jam);
                        jam.setTypeface(MontserratRegular);
                        TextView txtQty = dialogView.findViewById(R.id.txtQty);
                        txtQty.setTypeface(MontserratRegular);
                        display = dialogView.findViewById(R.id.display);
                        TextView increment = dialogView.findViewById(R.id.increment);
                        TextView decrement = dialogView.findViewById(R.id.decrement);
                        btnSimpan = dialogView.findViewById(R.id.btnSimpan);
                        ImageView btnBatalkan = dialogView.findViewById(R.id.btnBatalkan);
                        btnSimpan.setTypeface(MontserratRegular);
                        //btnBatalkan.setTypeface(MontserratRegular);
                        TextView title = dialogView.findViewById(R.id.title);
                        title.setTypeface(MontserratRegular);
                        sp_nama = dialogView.findViewById(R.id.sp_nama);

                        waktu = modalJadwal.getJadwal().split(" ");
                        jam.setText(waktu[1]);
                        jam_pengirimn = waktu[1];

                        tanggal = modalJadwal.getJadwal().split(" ");

                        values = Integer.valueOf(modalJadwal.getCup());
                        display.setText(values + "");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailBerlangganan.this, android.R.layout.simple_spinner_item, nama_menu);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_nama.setAdapter(adapter);
                        Log.d("nama", String.valueOf(nama_menu.length));

                        Integer pos = 0;
                        for (int i = 0; i < nama_menu.length; i++) {
                            if (nama_menu[i].equals(modalJadwal.getNama())) {
                                pos = i;
                                Log.d("i", String.valueOf(i));
                                sp_nama.setSelection(i);
                                break;
                            }
                        }

                        namaMenu = sp_nama.getSelectedItem().toString();
                        nama_awal = namaMenu;

                        sp_nama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                namaMenu = sp_nama.getSelectedItem().toString();
                                new GetMax().execute();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        jam.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar mcurrentTime = Calendar.getInstance();
                                final int hourr = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                int minute = mcurrentTime.get(Calendar.MINUTE);
                                TimePickerDialog mTimePicker;
                                mTimePicker = new TimePickerDialog(DetailBerlangganan.this, new TimePickerDialog.OnTimeSetListener() {


                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        if (hourOfDay < 9 && hourOfDay > 0) {
                                            hour = "0" + hourOfDay;
                                        } else {
                                            hour = String.valueOf(hourOfDay);
                                        }
                                        if (minute < 9 && minute > 0) {
                                            mnt = "0" + minute;
                                        } else {
                                            mnt = String.valueOf(minute);
                                        }
                                        jam.setText(hour + ":" + mnt + ":00");
                                        jam_pengirimn = jam.getText().toString();

                                    }
                                }, hourr, minute, true);//Yes 24 hour time
                                mTimePicker.setTitle("Pilih Jam");
                                mTimePicker.show();
                            }
                        });

                        new GetMax().execute();

                        increment.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                if (values >= downrange && values < uprange)
                                    values += 1;
                                if (values > uprange)
                                    values = downrange;
                                display.setText(String.valueOf(values));

                            }
                        });

                        decrement.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (values > downrange && values <= uprange)
                                    values -= 1;

                                if (values < downrange)
                                    values = uprange;

                                display.setText(String.valueOf(values));

                            }
                        });


                        btnSimpan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (nama_awal == namaMenu) {
                                    jenis = "qty";
                                } else {
                                    jenis = "nama";
                                }
                                new SimpanUpdate().execute();
                            }
                        });


                        alertDialog = dialog.create();
                        alertDialog.setCancelable(false);

                        alertDialog.show();

                        btnBatalkan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(getApplicationContext(), DetailBerlangganan.class);
                                intent.putExtra("id_orderan", id_orderan);
                                intent.putExtra("nama_paket", nama_paket);
                                startActivity(intent);
                            }
                        });

                    }
            }
        });

        new GetJadwal().execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Orders.class);
        startActivity(intent);
    }

    private class GetJadwal extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id_orderan", id_orderan));
            params.add(new BasicNameValuePair("id_paket", id_paket.toString()));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetJadwal, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DetailBerlangganan.this);
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
                if (result != null){
                    if (result.getInt("error") == 1){

                    }else {
                        namaPaket.setText(nama_paket);
                        String status = "detail";
                        editor.putString("status", status);
                        editor.apply();
                        list =  new ArrayList<>();
                        JSONArray daftar = new JSONArray(result.getString("menu"));
                        nama_menu = new String[daftar.length()];
                        for (int j = 0; j < daftar.length(); j++) {
                            final JSONObject paket1 = daftar.getJSONObject(j);

                            modal_berlangganan modalBerlangganan = new modal_berlangganan();
                            modalBerlangganan.setNama(paket1.getString("nama_menu"));
                            modalBerlangganan.setThumbnail(paket1.getString("thumbnail"));
                            modalBerlangganan.setId_paket(paket1.getInt("id_menu"));
                            modalBerlangganan.setQty(paket1.getInt("qty"));

                            nama_menu[j] = paket1.getString("nama_menu");

                            list.add(modalBerlangganan);
                            adapterBerlangganan = new adapter_berlangganan(DetailBerlangganan.this, R.layout.cv_paket, list);
                            list_item.setAdapter(adapterBerlangganan);
                            Helper.getListViewSize(list_item);
                        }
                        Picasso.get().load(dataUrl + "paket/list/" +result.getString("thumbnail")).into(thumbnail2);


                        list =  new ArrayList<>();
                        JSONArray daftar2 = new JSONArray(result.getString("jadwal"));
                        for (int j = 0; j < daftar2.length(); j++) {
                            final JSONObject jadwal = daftar2.getJSONObject(j);

                            modal_jadwal modalJadwal = new modal_jadwal();
                            modalJadwal.setThumbnail(jadwal.getString("thumbnail"));
                            modalJadwal.setNama(jadwal.getString("nama"));
                            modalJadwal.setTanggal(jadwal.getString("tanggal"));
                            modalJadwal.setCup(jadwal.getString("qty"));
                            modalJadwal.setStatus(jadwal.getString("status"));
                            modalJadwal.setId_jadwal(jadwal.getString("id_jadwal"));
                            modalJadwal.setJam(jadwal.getString("jam"));
                            modalJadwal.setJadwal(jadwal.getString("jadwal"));

                            list.add(modalJadwal);
                            adapterJadwal = new adapter_jadwal(DetailBerlangganan.this, R.layout.cv_jadwal, list);
                            list_jadwal.setAdapter(adapterJadwal);
                            Helper.getListViewSize(list_jadwal);
                        }

                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class GetMax extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("nama_menu", namaMenu));
            params.add(new BasicNameValuePair("id_paket", id_paket.toString()));
            params.add(new BasicNameValuePair("id_jadwal", id_jadwal));
            params.add(new BasicNameValuePair("tanggal", waktu[0]));
            params.add(new BasicNameValuePair("id_orderan", id_orderan));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetMax, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DetailBerlangganan.this);
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
                if (result != null){
                    if (result.getInt("error") == 1){

                    }else {
                        uprange = result.getInt("max");
                        downrange = 1;
//                        if(result.getInt("max") == 0){
//                            values = 0;
//                            uprange = values;
//                            downrange = 0;
//                            display.setText(values + "");
//                        }
                        if (result.getInt("max") > 0){
                            if (values >= result.getInt("max")){
                                values = result.getInt("max");
                                display.setText(values + "");
                            }else{
                                display.setText(values + "");
                            }
                        }else if (result.getInt("max") <= 0){
                            Toast.makeText(getApplicationContext(), "Maaf, persediaan cup Anda untuk menu ini telah habis", Toast.LENGTH_LONG).show();
                            uprange = 0;
                            downrange = 0;
                            values = 0;
                            display.setText(values + "");
                        }

                        if(values == 0){
                            btnSimpan.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
                            btnSimpan.setClickable(false);
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class SimpanUpdate extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("nama_menu", namaMenu));
            params.add(new BasicNameValuePair("id_paket", id_paket.toString()));
            params.add(new BasicNameValuePair("id_jadwal", id_jadwal));
            params.add(new BasicNameValuePair("tanggal", tanggal[0]));
            params.add(new BasicNameValuePair("qty", String.valueOf(values)));
            params.add(new BasicNameValuePair("jenis", jenis));
            params.add(new BasicNameValuePair("id_orderan", id_orderan));
            params.add(new BasicNameValuePair("jam", jam_pengirimn));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlSimpanUpdate, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DetailBerlangganan.this);
            pDialog.setMessage("Mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
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
                        if(!result.getString("result").equals("null") && !result.getString("result").equals("1")) {
                            Toast.makeText(getApplicationContext(), result.getString("result"), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), DetailBerlangganan.class);
                            intent.putExtra("id_orderan", id_orderan);
                            intent.putExtra("nama_paket", nama_paket);
                            startActivity(intent);
                        }else if (result.getString("result").equals("1")){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailBerlangganan.this);
                            builder1.setMessage("Anda tidak dapat request cup. Request cup hanya dapat dilakukan 2 jam sebelum pengantaran/pengambilan");
                            builder1.setCancelable(true);

                            builder1.setNegativeButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getApplicationContext(), DetailBerlangganan.class);
                                            intent.putExtra("id_orderan", id_orderan);
                                            intent.putExtra("nama_paket", nama_paket);
                                            startActivity(intent);
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), DetailBerlangganan.class);
                            intent.putExtra("id_orderan", id_orderan);
                            intent.putExtra("nama_paket", nama_paket);
                            startActivity(intent);
                        }

                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
