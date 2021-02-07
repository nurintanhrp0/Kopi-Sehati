package ventures.g45.kopisehati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {

    TextView title;
    EditText inpKode, inpNohp;
    Button btnLanjutkan;
    Typeface MontserratRegular;
    String kode, nohp, defaultUrl, urlSignIn, token, jenis, noHp, nama, email;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        urlSignIn = defaultUrl + "signin.html";

        title = findViewById(R.id.txtTitle);
        title.setTypeface(MontserratRegular);
        inpKode = findViewById(R.id.inpKode);
        inpKode.setTypeface(MontserratRegular);
        inpNohp = findViewById(R.id.inpNohp);
        inpNohp.setTypeface(MontserratRegular);
        btnLanjutkan = findViewById(R.id.btnLanjutkan);
        btnLanjutkan.setTypeface(MontserratRegular);

        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kode = inpKode.getText().toString();
                nohp = inpNohp.getText().toString();

                if ((!kode.isEmpty()) && (!nohp.isEmpty())){
                    new Login().execute(kode, nohp);
                } else {
                    Toast.makeText(getApplicationContext(), "Kode Negara dan Nomor Handphone Tidak Boleh Kosong", Toast.LENGTH_LONG).show();

                }
            }
        });

        if (token.isEmpty()) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("", "getInstanceId failed", task.getException());
                        return;
                    }
                    token = task.getResult().getToken();
                    editor.putString("token", token);
                    editor.apply();
                }
            });
        }
    }

    public class Login extends AsyncTask<String,Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("kode", args[0]));
            params.add(new BasicNameValuePair("phone", args[1]));

            JSONObject json = jsonParser.makeHttpRequest(urlSignIn, "POST", params);

            return json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignIn.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if ((pDialog != null) && (pDialog.isShowing())){
                pDialog.dismiss();
                pDialog = null;
            }

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
                    }
                    else
                        if (result.getString("nama").isEmpty()) {
                            Intent intent = new Intent(getApplicationContext(), OTP.class);
                            intent.putExtra("phoneNumber", result.getString("nohp"));
                            intent.putExtra("status", "regis");
                            intent.putExtra("kode", kode);
                            intent.putExtra("nohp", nohp);
                            intent.putExtra("nama", result.getString("nama"));
                            startActivity(intent);
                        }
                        else{
                            noHp = result.getString("nohp");
                            nama = result.getString("nama");
                            email = result.getString("email");
                            if (result.getInt("id_login") == 1){
                                jenis = "facebook";
                                showPopup();
                            }else if (result.getInt("id_login") == 2){
                                jenis = "google";
                                showPopup();
                            }else {
                                Intent intent = new Intent(getApplicationContext(), OTP.class);
                                intent.putExtra("status", "login");
                                intent.putExtra("phoneNumber", result.getString("nohp"));
                                intent.putExtra("kode", kode);
                                intent.putExtra("nohp", nohp);
                                intent.putExtra("nama", result.getString("nama"));
                                startActivity(intent);
                            }
                        }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Silahkan cek koneksi internet kamu", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                e.printStackTrace();

            }
        }
    }

    public void showPopup(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
        builder1.setMessage("Anda sudah terdaftar menggunakan " + StringUtils.capitalize(jenis) + ". Apakah anda ingin melanjutkan dengan membuat PIN?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "YA",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), Registrasi.class);
                        intent.putExtra("noHp", noHp);
                        intent.putExtra("status", "loginulang");
                        intent.putExtra("kode", kode);
                        intent.putExtra("nohp", nohp);
                        intent.putExtra("nama",nama);
                        intent.putExtra("email", email);
                        startActivity(intent);

                    }
                });

        builder1.setNegativeButton(
                "TIDAK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
