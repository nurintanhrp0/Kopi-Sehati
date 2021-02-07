package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AfterOrder extends AppCompatActivity {

    TextView txtTitle, keterangan, txtTotal, total, txtNorek, norek;
    Button btnKonfirmasi;
    ImageView icBack, share1, share;
    Typeface MontserratRegular;
    ProgressDialog pDialog;
    String id_orderan, defaultUrl, urlGetPembayaran,sKeterangan, no_tujuan, atas_nama;
    JSONParser jsonParser = new JSONParser();
    Integer hargaTotal, from;
    DecimalFormat format;
    LinearLayout blockNorek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_order);

        Intent intent = getIntent();
        id_orderan = intent.getStringExtra("id_orderan");
        sKeterangan = intent.getStringExtra("keterangan");
        hargaTotal = intent.getIntExtra("total", 0);
        no_tujuan = intent.getStringExtra("no_tujuan");
        atas_nama = intent.getStringExtra("atas_nama");
        from = intent.getIntExtra("from", 0);
        //Log.d("norek", no_tujuan);

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        urlGetPembayaran = defaultUrl + "getubahstatus.html";

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        format = new DecimalFormat("#,###.##");

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        keterangan = findViewById(R.id.keterangan);
        keterangan.setTypeface(MontserratRegular);
        keterangan.setText(sKeterangan);
        icBack = findViewById(R.id.icBack);
        btnKonfirmasi = findViewById(R.id.btnBKonfirmasi);
        btnKonfirmasi.setTypeface(MontserratRegular);
        txtTotal = findViewById(R.id.txtTotal);
        txtTotal.setTypeface(MontserratRegular);
        total = findViewById(R.id.total);
        total.setTypeface(MontserratRegular);
        total.setText("Rp " + format.format(hargaTotal));
        txtNorek = findViewById(R.id.txtNorek);
        txtNorek.setTypeface(MontserratRegular);
        txtNorek.setText("Nomor Tujuan ( A/N " + atas_nama + " )");
        norek = findViewById(R.id.norek);
        norek.setTypeface(MontserratRegular);
        norek.setText(no_tujuan);
        share1 = findViewById(R.id.btnShare1);
        share = findViewById(R.id.btnShare);
        blockNorek = findViewById(R.id.blockNorek);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPembayaran().execute();
            }
        });

        if (no_tujuan.equals("")){
            blockNorek.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if (from == 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else {
            super.onBackPressed();
        }
    }

    public void shareNorek(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(no_tujuan);
        Toast.makeText(this, "Disalin ke clipboard", Toast.LENGTH_SHORT).show();

    }

    public void shareTotal(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(hargaTotal + "");
        Toast.makeText(this, "Disalin ke clipboard", Toast.LENGTH_SHORT).show();
    }

    private class GetPembayaran extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id_orderan", id_orderan));
            params.add(new BasicNameValuePair("ubah", "konfirmasi"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetPembayaran, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AfterOrder.this);
            pDialog.setMessage("Mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
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
                        Intent intent = new Intent(getApplicationContext(), DetailOrders.class);
                        intent.putExtra("from", 1);
                        intent.putExtra("id_orderan", id_orderan);
                        startActivity(intent);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
