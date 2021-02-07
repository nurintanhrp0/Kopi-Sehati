package ventures.g45.kopisehati;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ventures.g45.kopisehati.adapter.adapter_alamat;
import ventures.g45.kopisehati.modal.modal_alamat;


public class DaftarAlamat extends AppCompatActivity {

    TextView txtTitle;
    ImageView icBack;
    Button btnTambah;
    ListView listView;
    Typeface MontserratRegular;
    List list;
    adapter_alamat adapterAlamat;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String noHp, defaultUrl, urlAlamat;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_alamat);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        noHp = sharedPreferences.getString("noHp", "");
        editor.putString("dari", "alamat");
        editor.apply();

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        urlAlamat = defaultUrl + "getdaftaralamat.html";

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        icBack = findViewById(R.id.icBack);
        btnTambah = findViewById(R.id.tambah);
        btnTambah.setTypeface(MontserratRegular);
        listView = findViewById(R.id.blockAlamat);

        list = new ArrayList<>();
        adapterAlamat = new adapter_alamat(DaftarAlamat.this, R.layout.cv_daftar_alamat, list);
        listView.setAdapter(adapterAlamat);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenentukanAlamat.class);
                startActivity(intent);
            }
        });

        new GetAlamat().execute();
    }

    @Override
    public void onBackPressed() {
        Intent d = new Intent(DaftarAlamat.this,Account.class);
        d.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(d);
    }

    private class GetAlamat extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlAlamat, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DaftarAlamat.this);
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
                        if (result.has("data")){
                            JSONArray daftarOrder = new JSONArray(result.getString("data"));
                            if (daftarOrder.length() > 0) {
                                for (int j = 0; j < daftarOrder.length(); j++) {

                                    final JSONObject daftar = daftarOrder.getJSONObject(j);

                                    modal_alamat modalAlamat = new modal_alamat();
                                    modalAlamat.setId_alamat(daftar.getInt("id_alamat"));
                                    modalAlamat.setKategori(daftar.getString("kategori"));
                                    modalAlamat.setCatatan(daftar.getString("catatan"));
                                    modalAlamat.setDetail(daftar.getString("detail"));
                                    modalAlamat.setKoordinat(daftar.getString("koordinat"));

                                    list.add(modalAlamat);
                                    //Helper.getListViewSize(listView);
                                }
                            }
                        }
                    }
                }else{

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            adapterAlamat.notifyDataSetChanged();
        }
    }
}
