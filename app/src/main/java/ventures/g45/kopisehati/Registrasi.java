package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrasi extends AppCompatActivity {

  TextView txtTitle, txtNama, txtEmail, txtPin, txtReferralCode;
  EditText inpNama, inpEmail, inpPin, inpReferralCode;
  Button btnDaftar;
  Typeface MontserratRegular;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String token, sNoHP, status, sNama, sEmail, sPIN, defaultUrl, urlSignUp, kode, nomorhp, sReferral, nama, email;
  Intent intent;
  ProgressDialog pDialog;
  JSONParser jsonParser = new JSONParser();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registrasi);

    sharedPreferences = getSharedPreferences("kopisehati", 0);
    editor = sharedPreferences.edit();
    token = sharedPreferences.getString("token", "");

    intent = getIntent();
    sNoHP = intent.getStringExtra("noHp");
    status = intent.getStringExtra("status");
    kode = intent.getStringExtra("kode");
    nomorhp = intent.getStringExtra("nohp");
    nama = intent.getStringExtra("nama");
    email  = intent.getStringExtra("email");

    defaultUrl = ((KopiSehati) getApplication()).getUrl();
    urlSignUp = defaultUrl + "registrasi.html";

    MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

    txtTitle = findViewById(R.id.txtTitle);
    txtTitle.setTypeface(MontserratRegular);
    txtNama = findViewById(R.id.txtNama);
    txtNama.setTypeface(MontserratRegular);
    txtEmail = findViewById(R.id.txtEmail);
    txtPin = findViewById(R.id.txtPin);
    inpNama = findViewById(R.id.inpNama);
    inpNama.setTypeface(MontserratRegular);
    inpEmail = findViewById(R.id.inpEmail);
    inpEmail.setTypeface(MontserratRegular);
    inpPin = findViewById(R.id.inpPin);
    inpPin.setTypeface(MontserratRegular);
    btnDaftar = findViewById(R.id.btnDaftar);
    btnDaftar.setTypeface(MontserratRegular);
    txtReferralCode = findViewById(R.id.txtReferralCode);
    txtReferralCode.setTypeface(MontserratRegular);
    inpReferralCode = findViewById(R.id.inpReferralCode);
    inpReferralCode.setTypeface(MontserratRegular);

    if (status.equals("loginulang")){
      inpNama.setText(nama);
      inpEmail.setText(email);
      txtTitle.setVisibility(View.GONE);
      txtReferralCode.setText("Konfirmasi PIN");
      inpReferralCode.setHint("PIN");
      inpReferralCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
      inpNama.setFocusable(false);
      inpEmail.setFocusable(false);
      btnDaftar.setText("SIMPAN");
    }


    btnDaftar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sNama = inpNama.getText().toString();
        sEmail = inpEmail.getText().toString();
        sPIN = inpPin.getText().toString();
        sReferral = inpReferralCode.getText().toString();
        if ((!sNama.isEmpty()) && (!sEmail.isEmpty()) && (!sPIN.isEmpty())) {
          if (isEmailValid(sEmail)) {
            if (status.equals("loginulang")){
              if (sPIN.equals(sReferral)){
                new SignUp().execute();
              }else {
                Toast.makeText(Registrasi.this, "Konfimrasi PIN yang Anda masukkan salah", Toast.LENGTH_SHORT).show();
              }
            }else {
              new SignUp().execute();
            }

          } else {
            Toast.makeText(getApplicationContext(), "Email yang anda isikan tidak valid", Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(getApplicationContext(), "Nama, Email dan PIN tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  public static boolean isEmailValid(String email) {
    String domainChars = "a-z0-9\\-";
    String atomChars = "a-z0-9\\Q!#$%&'*+-/=?^_`{|}~\\E";
    String emailRegex = "^" + dot(atomChars) + "@" + dot(domainChars) + "$";
    Pattern pattern = Pattern.compile(emailRegex);

    //String expression = "^[\\\\\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\\\\\.[\\\\\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\\\\\.)+[a-zA-Z0-9]{2,6}$";
   // Pattern pattern = Pattern.compile(expression);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  private static String dot(String chars) {
    return "[" + chars + "]+(?:\\.[" + chars + "]+)*";
  }


  private class SignUp extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected void onPreExecute() {

      super.onPreExecute();

      pDialog = new ProgressDialog(Registrasi.this);
      pDialog.setMessage("Mengirim data ...");
      pDialog.setIndeterminate(false);
      pDialog.setCancelable(true);
      pDialog.show();

    }
    @Override
    protected JSONObject doInBackground(String... args) {

      ArrayList params = new ArrayList();

      params.add(new BasicNameValuePair("phone", nomorhp));
      params.add(new BasicNameValuePair("kode", kode));
      params.add(new BasicNameValuePair("nama", sNama));
      params.add(new BasicNameValuePair("email", sEmail));
      params.add(new BasicNameValuePair("pin", sPIN));
      params.add(new BasicNameValuePair("token", token));
      params.add(new BasicNameValuePair("status", status));
      params.add(new BasicNameValuePair("referral_code", sReferral));

      JSONObject json = jsonParser.makeHttpRequest(urlSignUp, "POST", params);

      return json;

    }

    protected void onPostExecute(JSONObject result) {

      if ((pDialog != null) && pDialog.isShowing())
        pDialog.dismiss();
      pDialog = null;

      try {
        if (result != null) {
          if (result.getInt("error") == 1) {
            Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
          } else {
            intent = new Intent(Registrasi.this, MainActivity.class);
            editor.putString("nama", sNama);
            editor.putString("noHp", result.getString("nohp"));
            editor.putString("kategori", "1");
            editor.apply();
            startActivity(intent);
          }
        } else {
          Toast.makeText(getApplicationContext(), "ilahkan cek koneksi internet kamu", Toast.LENGTH_LONG).show();
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }

  }
}
