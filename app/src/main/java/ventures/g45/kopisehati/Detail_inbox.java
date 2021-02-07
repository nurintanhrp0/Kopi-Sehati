package ventures.g45.kopisehati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Detail_inbox extends AppCompatActivity {

  TextView txtTitle, isi;
  ImageView icBack;
  Button btnGunakan;
  Typeface MontserratRegular;
  Intent intent;
  String jenis, sIsi, jenis_promo, kode, skode, status;
  Integer nilai, min, maks, pemakaian, hargaTotal, jumlah, id_voucher, pesanan, hasil, hargaOngkir;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  DecimalFormat decimalFormat = new DecimalFormat("#.##");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_inbox);

    sharedPreferences = getSharedPreferences("kopisehati", 0);
    editor = sharedPreferences.edit();
    hasil = sharedPreferences.getInt("hasil", 0);
    hargaOngkir = sharedPreferences.getInt("ongkir", 0);
    kode = sharedPreferences.getString("kode", "");


    MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();

    jenis = sharedPreferences.getString("jenis", "");
    sIsi = sharedPreferences.getString("isi", "");
    jenis_promo = sharedPreferences.getString("jenis_promo", "");
    nilai = sharedPreferences.getInt("nilai", 0);
    Log.d("nilai", nilai.toString());
    min = sharedPreferences.getInt("min", 0);
    maks = sharedPreferences.getInt("maks", 0);
    pemakaian = sharedPreferences.getInt("pemakaian", 0);
    jumlah = sharedPreferences.getInt("jumlah", 0);
    id_voucher = sharedPreferences.getInt("id", 0);
    pesanan = sharedPreferences.getInt("pesanan", 0);

    intent = getIntent();
    status = intent.getStringExtra("status");

    txtTitle = findViewById(R.id.txtTitle);
    txtTitle.setTypeface(MontserratRegular);
    isi = findViewById(R.id.isi);
    isi.setTypeface(MontserratRegular);
    btnGunakan = findViewById(R.id.btnGunakan);
    btnGunakan.setTypeface(MontserratRegular);
    icBack = findViewById(R.id.icBack);

    //Log.d("hasil", hargaTotal.toString());

    isi.setText(Html.fromHtml(sIsi));

    if (jenis.equals("voucher")) {
      btnGunakan.setVisibility(View.VISIBLE);
      if (status.equals("batalkan")) {
        btnGunakan.setBackground(getResources().getDrawable(R.drawable.border_white));
        btnGunakan.setBackgroundTintList(ContextCompat.getColorStateList(Detail_inbox.this, android.R.color.holo_red_dark));
        btnGunakan.setText("Batalkan");
      }
    }


    btnGunakan.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        hitungdiskon();
      }
    });

    icBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  public void hitungdiskon(){
    hargaTotal = hasil;
    int diskon;
    Log.d("status", status);
    if (status.equals("batalkan")){
      diskon = 0;
      editor.putString("jenis_promo", "");
      editor.putInt("nilai", 0);
      editor.putInt("min", 0);
      editor.putInt("maks", 0);
      editor.putInt("diskon", diskon);
      editor.putInt("id_voucher", 0);
      editor.putString("kode", "");
      editor.apply();
    }else if (status.equals("tambah")){
      Log.d("jumlah", jumlah.toString());
      Log.d("pemakaian", pemakaian.toString());
      if (jumlah < pemakaian) {
        if (hargaTotal >= min) {

          if (jenis_promo.equals("nominal")) {
            diskon = nilai;
            //hargaTotal = hargaTotal - nilai;
            //Log.d("total", hargaTotal.toString());

          } else {
            diskon = (hargaTotal * nilai / 1000);
            Log.d("diskon",String.valueOf(diskon));
            if (diskon > maks) {
              diskon = maks;
            }
          }

          editor.putInt("diskon", diskon);
          editor.putInt("id_voucher", id_voucher);
          editor.putString("kode", kode);
          editor.apply();

        } else {
          Toast.makeText(getApplicationContext(), "Maaf, pesanan Anda belum mencapai jumlah minimum pembelian", Toast.LENGTH_LONG).show();
          editor.putString("kode", "");
          editor.apply();
        }
      } else {
        Toast.makeText(getApplicationContext(), "Maaf, Anda telah mencapai jumlah maksimal pemakaian voucher ini", Toast.LENGTH_LONG).show();
        editor.putString("kode", "");
        editor.apply();
      }
    }
    Intent intent = new Intent(getApplicationContext(), Checkout.class);
    intent.putExtra("pesanan", pesanan);
    startActivity(intent);
  }
}
