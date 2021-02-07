package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Account extends AppCompatActivity implements View.OnClickListener {

    TextView txtTitle, txtNama, txtEmail, txt1, txt2, txt3, help, sk, device, kebijakan, rate, txtHome, txtOrders, txtInbox, txtAkun, voucher, alamat;
    Button btnBagikan, btnLogOut;
    Typeface MontserratRegular;
    ImageView inpAvatar;
    FloatingActionButton editFoto;
    JSONParser jsonParser = new JSONParser();
    String defaultUrl, dataUrl, urlGetProfil, noHp, foto, encodedImage, urlUploadFoto, urUpdateProfil, mUrl, sText, kategori, sUrl;
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    Bitmap bitmap;
    Uri filepath;
    ProgressDialog pDialog;
    AlertDialog.Builder dialog;
    AlertDialog alertDialog;
    View dialogView;
    LayoutInflater inflater;
    LinearLayout home, orders, inbox, akun;
    ScrollView blockAkun;
    Intent a,b,c,d;
    RelativeLayout blockShare;
    SharedPreferences.Editor editor;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sharedPreferences = getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        noHp = sharedPreferences.getString("noHp", "");

        defaultUrl = ((KopiSehati) getApplication()).getUrl();
        dataUrl = ((KopiSehati) getApplication()).getUrlData();
        mUrl = ((KopiSehati) getApplication()).getmUrl();
        urlGetProfil = defaultUrl + "getprofil.html";
        urlUploadFoto = defaultUrl + "getupdatefoto.html";
        urUpdateProfil = defaultUrl + "getupdateprofil.html";

        MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(MontserratRegular);
        txtNama = findViewById(R.id.txtNama);
        txtNama.setTypeface(MontserratRegular);
        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setTypeface(MontserratRegular);
        txt1 = findViewById(R.id.txt1);
        txt1.setTypeface(MontserratRegular);
        txt2 = findViewById(R.id.txt2);
        txt2.setTypeface(MontserratRegular);
        txt2.setPaintFlags(txt2.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        txt2.setTypeface(null, Typeface.ITALIC);
        txt3 = findViewById(R.id.txt3);
        txt3.setTypeface(MontserratRegular);
        help = findViewById(R.id.help);
        help.setTypeface(MontserratRegular);
        sk = findViewById(R.id.sk);
        sk.setTypeface(MontserratRegular);
        device = findViewById(R.id.device);
        device.setTypeface(MontserratRegular);
        kebijakan = findViewById(R.id.kebijakan);
        kebijakan.setTypeface(MontserratRegular);
        rate = findViewById(R.id.rate);
        rate.setTypeface(MontserratRegular);
        txtHome = findViewById(R.id.txtHome);
        txtHome.setTypeface(MontserratRegular);
        txtOrders = findViewById(R.id.txtOrders);
        txtOrders.setTypeface(MontserratRegular);
        txtInbox = findViewById(R.id.txtInbox);
        txtInbox.setTypeface(MontserratRegular);
        txtAkun = findViewById(R.id.txtAccount);
        txtAkun.setTypeface(MontserratRegular);
        btnBagikan = findViewById(R.id.btnBagikan);
        btnBagikan.setTypeface(MontserratRegular);
        inpAvatar = findViewById(R.id.inpAvatar);
        editFoto = findViewById(R.id.editFoto);
        home = findViewById(R.id.home);
        orders = findViewById(R.id.orders);
        inbox = findViewById(R.id.inbox);
        akun = findViewById(R.id.account);
        home.setOnClickListener(this);
        orders.setOnClickListener(this);
        inbox.setOnClickListener(this);
        akun.setOnClickListener(this);
        blockAkun = findViewById(R.id.blockAkun);
        blockShare = findViewById(R.id.blcokShare);
        voucher = findViewById(R.id.voucher);
        voucher.setTypeface(MontserratRegular);
        alamat = findViewById(R.id.alamat);
        alamat.setTypeface(MontserratRegular);

        txtNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(Account.this, R.style.DialogPutih);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.edit_profil, null);
                dialog.setView(dialogView);
                dialog.setTitle("Edit Nama Akun Anda");
                edit = dialogView.findViewById(R.id.edit);
                edit.setText(txtNama.getText().toString());
                Button btnEdit = dialogView.findViewById(R.id.btnEdit);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kategori = "nama";
                        new updateProfil().execute(edit.getText().toString(), kategori);
                    }
                });

                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(Account.this, R.style.DialogPutih);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.edit_profil, null);
                dialog.setView(dialogView);
                dialog.setTitle("Edit Email Anda");
                edit = dialogView.findViewById(R.id.edit);
                edit.setText(txtEmail.getText().toString());

                Button btnEdit = dialogView.findViewById(R.id.btnEdit);

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kategori = "email";
                        new updateProfil().execute(edit.getText().toString(), kategori);
                    }
                });


                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        editFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Voucher.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Content.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        sk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Content.class);
                intent.putExtra("id", 2);
                startActivity(intent);
            }
        });

        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Content.class);
                intent.putExtra("id", 3);
                startActivity(intent);
            }
        });

        kebijakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Content.class);
                intent.putExtra("id", 4);
                startActivity(intent);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/"));
                startActivity(intent);
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReferralCode.class);
                intent.putExtra("id", 6);
                startActivity(intent);
            }
        });

        alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DaftarAlamat.class);
                startActivity(intent);
            }
        });

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                getSharedPreferences("kopisehati", 0)
                        .edit()
                        .putBoolean("isFirstRun", false)
                        .apply();
                Intent intent = new Intent(Account.this, MainActivity.class);
                startActivity(intent);
            }
        });


        new GetDetailProfil().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                if (noHp.isEmpty()) {
                    a = new Intent(this, SignIn.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a);
                } else {
                    a = new Intent(this, MainActivity.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a); }
                break;
            case R.id.orders:
                if (noHp.isEmpty()) {
                    b = new Intent(this, SignIn.class);

                } else {
                    b = new Intent(this, Orders.class);

                }
                b.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(b);
                break;
            case  R.id.inbox:
                if (noHp.isEmpty()){
                    c = new Intent(this, SignIn.class);
                } else {
                    c = new Intent(this, Inbox.class); }
                c.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(c);
                break;
            case R.id.account:
                if (noHp.isEmpty()) {
                    d = new Intent(this, SignIn.class);
                    d.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(d);
                }else {blockAkun.smoothScrollTo(0,0);}

                break;
        }
    }

    private class GetDetailProfil extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject jsonObject= jsonParser.makeHttpRequest(urlGetProfil, "POST", params);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null){
                    if (result.getInt("error") == 1){

                    }else {
                        txtNama.setText(result.getString("nama"));
                        txtEmail.setText(result.getString("email"));
                        Picasso.get().load(dataUrl + "avatar/"  + result.getString("thumbnail")).into(inpAvatar);
                        foto = result.getString("thumbnail");
                        btnBagikan.setText(result.getString("referall_code"));
                        sText = result.getString("isi");
                        sUrl = result.getString("link");

                    }
                }else {

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void getImage() {

        if (foto.equals("coffee.png")){
            final CharSequence[] menu = {"Kamera", "Galeri"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Upload Foto")
                    .setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //Mengambil gambar dari Kemara ponsel
                                    Intent imageIntentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(imageIntentCamera, REQUEST_CODE_CAMERA);
                                    break;

                                case 1:
                                    //Mengambil gambar dari galeri
                                    Intent imageIntentGallery = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);
                                    break;
                            }
                        }
                    });

            dialog.create();
            dialog.show();
        }
        else {
            final CharSequence[] menu = {"Kamera", "Galeri", "Hapus"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Upload Foto")
                    .setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //Mengambil gambar dari Kemara ponsel
                                    Intent imageIntentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(imageIntentCamera, REQUEST_CODE_CAMERA);
                                    break;

                                case 1:
                                    //Mengambil gambar dari galeri
                                    Intent imageIntentGallery = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);
                                    break;

                                case 2 :
                                    String status = "hapus";
                                    new uploadFoto().execute(status);
                            }
                        }
                    });

            dialog.create();
            dialog.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    inpAvatar.setVisibility(View.VISIBLE);
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    inpAvatar.setImageBitmap(bitmap);

                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    inpAvatar.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    inpAvatar.setImageURI(uri);
                    filepath = data.getData();
                }
                break;
        }
        String status = "ubah";
        new uploadFoto().execute(status);
    }

    private String imageToString (){
        bitmap = ((BitmapDrawable) inpAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("encode", encodedImage);

        return encodedImage;
    }

    private class uploadFoto extends AsyncTask<String, Void, JSONObject> {

        @Override

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Account.this);
            pDialog.setMessage("Sending data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            String image = imageToString();

            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("noHp", noHp));
            params.add(new BasicNameValuePair("image", image));
            params.add(new BasicNameValuePair("status", args[0]));

            JSONObject json = jsonParser.makeHttpRequest(urlUploadFoto, "POST", params);

            return json;
        }


        protected void onPostExecute(JSONObject result) {

            if ((pDialog != null) && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null) {
                    if (result.getInt("error") == 1) {
                        Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), result.getString("result"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),Account.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class updateProfil extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (kategori.equals("nama")){
                txtNama.setText("Please wait ...");
            }else {
                txtEmail.setText("Please wait ...");
            }

            pDialog = new ProgressDialog(Account.this);
            pDialog.setMessage("Sending data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("kategori", args[1]));
            params.add(new BasicNameValuePair("nilai", args[0]));
            params.add(new BasicNameValuePair("noHp", noHp));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urUpdateProfil, "POST", params );

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if ((pDialog != null) && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = null;

           try {
               if (result != null){
                   if (result.getInt("error") == 1) {

                   }else{
                       if (kategori.equals("nama")){
                           editor.putString("nama",edit.getText().toString());
                           editor.apply();
                       }

                       Intent intent = new Intent(getApplicationContext(),Account.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                       startActivity(intent);

                   }
               }else {

               }
           }catch (JSONException e){
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
