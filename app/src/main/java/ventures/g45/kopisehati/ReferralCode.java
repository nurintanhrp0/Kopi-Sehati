package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReferralCode extends AppCompatActivity {

  TextView txtTitle, isi, txtBagikan, txtKode;
  ImageView icBack, thumbnail, btnShare;
  Typeface MontserratRegular;
  String defaultUrl, dataUrl, urlContent, noHp;
  JSONParser jsonParser = new JSONParser();
  ProgressDialog pDialog;
  String sText, sUrl;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_referral_code);

    sharedPreferences = getSharedPreferences("kopisehati", 0);
    editor = sharedPreferences.edit();
    noHp = sharedPreferences.getString("noHp", "");

    MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
    defaultUrl = ((KopiSehati) getApplication()).getUrl();
    dataUrl = ((KopiSehati) getApplication()).getUrlData();
    urlContent = defaultUrl + "getcontent.html";

    txtTitle = findViewById(R.id.txtTitle);
    txtTitle.setTypeface(MontserratRegular);
    isi = findViewById(R.id.isi);
    isi.setTypeface(MontserratRegular);
    icBack = findViewById(R.id.icBack);
    thumbnail = findViewById(R.id.thumbnail);
    txtBagikan = findViewById(R.id.txtBagikan);
    txtBagikan.setTypeface(MontserratRegular);
    txtKode = findViewById(R.id.txtKode);
    txtKode.setTypeface(MontserratRegular);

    icBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    new GetContent().execute();

  }

  private class GetContent extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... strings) {

      ArrayList params = new ArrayList();

      params.add(new BasicNameValuePair("id", "6"));
      params.add(new BasicNameValuePair("noHp", noHp));

      JSONObject json = jsonParser.makeHttpRequest(urlContent, "POST", params);

      return json;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

      pDialog = new ProgressDialog(ReferralCode.this);
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
            isi.setText(Html.fromHtml(result.getString("isi")));
            Picasso.get().load(dataUrl + "content/" + result.getString("thumbnail")).into(thumbnail);
            sText = result.getString("isii");
            sUrl = result.getString("link");
            txtKode.setText(result.getString("kode"));
          }
        } else {
          Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server. Silahkan cek koneksi anda dan coba lagi.", Toast.LENGTH_LONG).show();
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

  }

  public void shareProduk(View view) {
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    //intent.putExtra(Intent.EXTRA_SUBJECT, sText);
    intent.putExtra(Intent.EXTRA_TEXT, sText + System.lineSeparator() +  sUrl);

    startActivity(Intent.createChooser(intent, "Kopi Gratis dari Kopi Sehati"));
  }
}
