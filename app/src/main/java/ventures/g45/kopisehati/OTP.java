package ventures.g45.kopisehati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    TextView txtTitle, txtInfo, txtTimer;
    Button btnMasukkanPIN, btnVerifikasi;
    Typeface MontserratRegular;
    String status, nohp, defaultUrl, urlSignIn, token, code, nama, kode, nomorhp;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    Intent intent;
    private String verificationId;
    EditText inpOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        urlSignIn = defaultUrl + "signinpin.html";

        intent = getIntent();
        status = intent.getStringExtra("status");
        nohp = intent.getStringExtra("phoneNumber");
        nama = intent.getStringExtra("nama");
        kode = intent.getStringExtra("kode");
        nomorhp = intent.getStringExtra("nohp");

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        txtInfo = findViewById(R.id.txtInfo);
        txtInfo.setTypeface(MontserratRegular);
        btnMasukkanPIN = findViewById(R.id.btnInpPin);
        btnMasukkanPIN.setTypeface(MontserratRegular);
        inpOTP = findViewById(R.id.inpOtp);
        btnVerifikasi = findViewById(R.id.btnVerifikasi);
        txtTimer = findViewById(R.id.txtTimer);
        txtTimer.setTypeface(MontserratRegular);

        if (status.equals("regis")){
            btnMasukkanPIN.setVisibility(View.INVISIBLE);
            txtInfo.setVisibility(View.INVISIBLE);
        }

        btnMasukkanPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(OTP.this, R.style.DialogPutih);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.popup_input_pin, null);
                dialog.setView(dialogView);
                final AlertDialog alertDialog = dialog.create();
                final EditText inpPin = dialogView.findViewById(R.id.inpPin);
                Button btnLanjutkan = dialogView.findViewById(R.id.btnLanjutkan);

                btnLanjutkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Login().execute(inpPin.getText().toString());
                    }
                });

                alertDialog.show();
            }
        });

        btnVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(inpOTP.getText().toString());

            }
        });

        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(nohp);

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                //resend.setText("Request a new code in : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                int minutes = (int) (millisUntilFinished /1000) /60;
                int seconds  = (int) (millisUntilFinished / 1000) % 60;

                String left = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

                txtTimer.setText(left);

            }

            public void onFinish() {
                //resend.setText("Resend code!");
                Context context = OTP.this;
                if (! ((Activity) context).isFinishing()) {
                    txtTimer.setVisibility(View.GONE);
                    if (status.equals("login")) {
                        //Intent intent = getIntent();
                        //nama = intent.getStringExtra("nama");
                        btnMasukkanPIN.setVisibility(View.VISIBLE);
                        btnMasukkanPIN.setEnabled(true);
                        btnMasukkanPIN.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        txtInfo.setVisibility(View.VISIBLE);
                        btnVerifikasi.setEnabled(true);
                        btnVerifikasi.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        // btnMasukkanPIN.setEnabled(true);
                    } else {
                        intent = new Intent(OTP.this, Registrasi.class);
                        intent.putExtra("noHp", nohp);
                        intent.putExtra("kode", kode);
                        intent.putExtra("nohp", nomorhp);
                        intent.putExtra("status", status);
                        intent.putExtra("nama",nama);
                        intent.putExtra("email", "");
                        startActivity(intent);
                    }
                }
            }

        }.start();
    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            editor.putString("noHp", nohp);
                            editor.putString("nama", nama);
                            editor.apply();
                            if (status.equals("login")) {
                                intent = new Intent(OTP.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else {
                                intent = new Intent(OTP.this, Registrasi.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(OTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
          code = phoneAuthCredential.getSmsCode();

          Context context = OTP.this;
          if (! ((Activity) context).isFinishing()) {

              if (code != null) {
                  inpOTP.setText(code);
                  verifyCode(code);
              }
          }
           // Toast.makeText(OTP.this, code, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private class Login extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("phone", nomorhp));
            params.add(new BasicNameValuePair("kode", kode));
            params.add(new BasicNameValuePair("pin", args[0]));
            params.add(new BasicNameValuePair("token", token));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlSignIn, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(OTP.this);
            pDialog.setMessage("Mengirim data ...");
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
                    if (result.getInt("error") == 2) {
                        Toast.makeText(getApplicationContext(), result.getString("pesan"), Toast.LENGTH_LONG).show();
                    } else {
                        firebasetopic();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        editor.putString("noHp", nohp);
                        editor.putString("nama", result.getString("nama"));
                        Log.d("nohp", result.getString("nama"));
                        editor.apply();
                        finish();
                        startActivity(intent);
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

    public  void firebasetopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        });
    }
}
