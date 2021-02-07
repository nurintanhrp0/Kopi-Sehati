package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Content extends AppCompatActivity {

    TextView isi, txtTitle;
    ImageView icoBack, thumbnail;
    String defaultUrl, dataUrl, urlContent;
    Integer id;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    Typeface MontserratRegular;
    float factor;
    LinearLayout blockfaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        factor = getResources().getDisplayMetrics().density;

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        isi = findViewById(R.id.isi);
        isi.setTypeface(MontserratRegular);
        thumbnail = findViewById(R.id.thumbnail);
        icoBack = findViewById(R.id.icBack);
        blockfaq = findViewById(R.id.blockfaq);
        icoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        urlContent = defaultUrl + "getcontent.html";

        new GetContent().execute();

    }

    private class GetContent extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("id", id.toString()));

            JSONObject json = jsonParser.makeHttpRequest(urlContent, "POST", params);

            return json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Content.this);
            pDialog.setMessage("Mengambil data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if ((pDialog != null) && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null) {
                    if (result.getInt("error") == 1) {
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server. Silahkan cek koneksi anda dan coba lagi.", Toast.LENGTH_LONG).show();
                    } else {
                        txtTitle.setText(result.getString("judul"));
                        isi.setText(Html.fromHtml(result.getString("isi")));

                        if (id == 1){
                            blockfaq.setVisibility(View.VISIBLE);
                            isi.setVisibility(View.GONE);
                            JSONArray kategoriMenu = new JSONArray(result.getString("faq"));
                            if (kategoriMenu.length() > 0) {
                                for (int i = 0; i < kategoriMenu.length(); i++) {

                                    final JSONObject menu = kategoriMenu.getJSONObject(i);

                                    //String id_kat = menu.getString("id_kategori");

                                    LinearLayout kategori = new LinearLayout(Content.this);
                                    kategori.setPadding((int) (1 * factor), (int) (2 * factor), (int) (1 * factor), 0);
                                    kategori.setOrientation(LinearLayout.VERTICAL);

                                    ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    TextView nama = new TextView(Content.this);
                                    nama.setLayoutParams(layoutNama);
                                    //nama.setTypeface(Typeface.DEFAULT_BOLD);
                                    nama.setTypeface(MontserratRegular);
                                    nama.setPadding((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (0 * factor));
                                    nama.setText(menu.getString("question"));
                                    nama.setMaxLines(2);
                                    nama.setTextSize(20);
                                    nama.setEllipsize(TextUtils.TruncateAt.END);
                                    nama.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    kategori.addView(nama);

                                    ViewGroup.LayoutParams layoutHarga = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    TextView harga = new TextView(Content.this);
                                    harga.setLayoutParams(layoutHarga);
                                    harga.setTextSize(18);
                                    harga.setTypeface(MontserratRegular);
                                    harga.setTextColor(getResources().getColor(android.R.color.black));
                                    harga.setPadding((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (0 * factor));
                                    harga.setText(Html.fromHtml(menu.getString("answer")));
                                    harga.setMaxLines(100);
                                    harga.setEllipsize(TextUtils.TruncateAt.END);
                                    kategori.addView(harga);

                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (i == 0) {
                                        layoutParams1.setMargins((int) (10 * factor), 0, (int) (0 * factor), (int) (5 * factor));
                                    } else {
                                        layoutParams1.setMargins((int) (10 * factor), (int) (0 * factor), (int) (0 * factor), (int) (5 * factor));
                                    }

                                    kategori.setLayoutParams(layoutParams1);

                                    //kategori.setOnClickListener(new DaftarMenu.KategoriMenu(menu.getString("id_kategori"), "dipilih"));

                                    blockfaq.addView(kategori);
                                }
                            }
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server. Silahkan cek koneksi anda dan coba lagi.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
