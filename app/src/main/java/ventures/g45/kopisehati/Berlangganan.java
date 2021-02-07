package ventures.g45.kopisehati;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ventures.g45.kopisehati.adapter.adapter_berlangganan;
import ventures.g45.kopisehati.modal.modal_berlangganan;

public class Berlangganan extends Activity {

  String[] lama = {"1 BLN", "2 BLN"}, hari = {"Tanggal", "1"}, bulan = {"Bulan", "Januari"}, tahun = {"Tahun", "2019"};
  Spinner sp_lama, sp_tanggal, sp_bulan, sp_tahun;
  ArrayAdapter<String> adapter_lama, adapter_hari, adapter_bulan, adapter_tahun;
  TextView txtTitle, dikirim, ambilsendiri, txtInfo, txtAlamat, txtDetailAlamat, txtDaftarPesanan, txtTanggal, txtJam, jam, txtPengiriman, txtPembayaran, txtDetailPembayaran, txtHarga, harga, txtOngkir, ongkir, txtTotal, total, txtTanggall, txtBulan, txtTahun, txtNoPesanan, txtLokasi;
  ImageView icBack;
  Button btnPesan;
  ListView blockPesanan;
  LinearLayout blockPengiriman, blockOngkir, blockMetode, blockAlamat, blockOutlet, blockImagePengiriman, blockImagePembayaran, blockMetodePembayaran, blockMetodePengiriman, blockTanggal, blockWaktu;
  Typeface MontserratRegular;
  Integer pesanan;
  Integer id_outlet;
  double jarak;
  Integer id_pengiriman;
  Integer id_pembayaran;
  Integer thn;
  Integer ongkos;
  Integer id_paket;
  Integer id_alamat;
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  Intent a,b;
  View linePengiriman;
  AlertDialog.Builder dialog;
  LayoutInflater inflater;
  View dialogView;
  AlertDialog alertDialog;
  ProgressBar pBar;
  double latitude, longtitude, lat_alamat, long_alamat, lat_outlet, long_outlet;
  JSONParser jsonParser = new JSONParser();
  String defaultUrl, dataUrl, urlGetOutlet, noHp, urlGetCheckout, alamat, detailAlamat, keterangan, nama_pembayaran, bln, tgl, status, sketerangan, session, urlGantiPesanan, urlDeleteTempCart, jenis, urlsimpanorder, bulann, hour, mnt, nama_alamat, urlHitungJarak, no_tujuan, nama_outlet, lokasi, detail_outlet, atas_nama;
  float factor, distance;
  DecimalFormat decimalFormat, format;
  ProgressDialog pDialog;
  private int hargaTotal = 0;
  private double hargaOngkir = 0;
  private  int harga_per_km =0;
  private  int hasil =0;
  private DatePickerDialog datePickerDialog;
  private SimpleDateFormat date, month, yearr, mnth, dateFormat;
  ListView list_paket;
  private List<modal_berlangganan> list;
  adapter_berlangganan adapterBerlangganan;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_berlangganan);

//        sp_tanggal = findViewById(R.id.tanggal);
//        sp_bulan = findViewById(R.id.bulan);
//        sp_tahun = findViewById(R.id.tahun);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lama);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    sharedPreferences = getSharedPreferences("kopisehati", 0);
    editor = sharedPreferences.edit();
    latitude = Double.parseDouble(sharedPreferences.getString("latitude", ""));
    longtitude = Double.parseDouble(sharedPreferences.getString("longtitude", ""));
    noHp = sharedPreferences.getString("noHp", "");
    id_outlet = sharedPreferences.getInt("id_outlet", 0);
    lat_alamat = Double.parseDouble(sharedPreferences.getString("lat_alamat", "0"));
    Log.d("lat_alamat", String.valueOf(lat_alamat));
    long_alamat = Double.parseDouble(sharedPreferences.getString("long_alamat", "0"));
    jarak = sharedPreferences.getInt("jarak", 0);
    detailAlamat = sharedPreferences.getString("detailAlamat","");
    alamat = sharedPreferences.getString("alamat", "");
    id_pembayaran = sharedPreferences.getInt("id_pembayaran", 0);
    id_pengiriman = sharedPreferences.getInt("id_pengiriman", 1);
    tgl = sharedPreferences.getString("tanggal", "");
    bln = sharedPreferences.getString("bulan", "");
    thn = sharedPreferences.getInt("tahun", 0);
    hour = sharedPreferences.getString("jam", "0");
    mnt = sharedPreferences.getString("menit", "");
    nama_pembayaran = sharedPreferences.getString("nama_pembayaran", "");
    sketerangan = sharedPreferences.getString("sketerangan", "");
    id_paket = sharedPreferences.getInt("id_paket", 0);
    session = sharedPreferences.getString("session", "");
    id_alamat = sharedPreferences.getInt("id_alamat", 0);
    Log.d("id_alamat", id_alamat.toString());
    bulann = sharedPreferences.getString("bln", "");
    nama_alamat = sharedPreferences.getString("nama_alamat", " ");
    distance = sharedPreferences.getFloat("jarak2", 0);


    Intent intent = getIntent();
    pesanan = intent.getIntExtra("pesanan", 2);

    defaultUrl = ((KopiSehati) getApplication()).getUrl();
    dataUrl = ((KopiSehati) getApplication()).getUrlData();
    urlGetOutlet = defaultUrl + "getdataoutlet.html";
    urlGetCheckout = defaultUrl + "getdatacheckout.html";
    urlGantiPesanan = defaultUrl + "gantioutlet.html";
    urlDeleteTempCart = defaultUrl + "deletetemp.html";
    urlsimpanorder = defaultUrl + "getsimpanberlangganan.html";
    urlHitungJarak = defaultUrl + "getjarak.html";

    factor = getResources().getDisplayMetrics().density;
    decimalFormat = new DecimalFormat("#.##");
    date= new SimpleDateFormat("dd", Locale.getDefault());
    month = new SimpleDateFormat("MMMM", Locale.getDefault());
    mnth = new SimpleDateFormat("MM", Locale.getDefault());
    yearr = new SimpleDateFormat("yyyy", Locale.getDefault());
    dateFormat = new SimpleDateFormat("dd-MMMMM-yyyy", Locale.getDefault());
    format = new DecimalFormat("#,###.##");
    MontserratRegular = ((KopiSehati) getApplication()).getMontserratRegular();
    txtTitle = findViewById(R.id.txtTitle);
    txtTitle.setTypeface(MontserratRegular);
    dikirim = findViewById(R.id.dikirim);
    dikirim.setTypeface(MontserratRegular);
    ambilsendiri = findViewById(R.id.ambilSendiri);
    ambilsendiri.setTypeface(MontserratRegular);
    txtInfo = findViewById(R.id.txtInfo);
    txtInfo.setTypeface(MontserratRegular);
    txtAlamat = findViewById(R.id.txtAlamat);
    txtAlamat.setTypeface(MontserratRegular);
    txtDetailAlamat = findViewById(R.id.txtDetailAlamat);
    txtDetailAlamat.setTypeface(MontserratRegular);
    txtDaftarPesanan = findViewById(R.id.txtDaftarPesanan);
    txtDaftarPesanan.setTypeface(MontserratRegular);
    txtTanggal = findViewById(R.id.txtTanggal);
    txtTanggal.setTypeface(MontserratRegular);
    txtJam = findViewById(R.id.txtJam);
    txtJam.setTypeface(MontserratRegular);
    jam = findViewById(R.id.jam);
    jam.setTypeface(MontserratRegular);
    txtPengiriman = findViewById(R.id.txtPengiriman);
    txtPengiriman.setTypeface(MontserratRegular);
    txtPembayaran = findViewById(R.id.txtPembayaran);
    txtPembayaran.setTypeface(MontserratRegular);
    txtDetailPembayaran = findViewById(R.id.txtDetailPembayaran);
    txtDetailPembayaran.setTypeface(MontserratRegular);
    txtHarga = findViewById(R.id.txtHarga);
    txtHarga.setTypeface(MontserratRegular);
    txtOngkir = findViewById(R.id.txtOngkir);
    txtOngkir.setTypeface(MontserratRegular);
    txtTotal = findViewById(R.id.txtTotal);
    txtTotal.setTypeface(MontserratRegular);
    harga = findViewById(R.id.harga);
    harga.setTypeface(MontserratRegular);
    ongkir = findViewById(R.id.ongkir);
    ongkir.setTypeface(MontserratRegular);
    total = findViewById(R.id.total);
    total.setTypeface(MontserratRegular);
    icBack = findViewById(R.id.icBack);
    btnPesan = findViewById(R.id.btnPesan);
    btnPesan.setTypeface(MontserratRegular);
    blockPesanan = findViewById(R.id.blockPesanan);
    blockPengiriman = findViewById(R.id.blockPengiriman);
    blockOngkir = findViewById(R.id.blockOngkir);
    blockMetode = findViewById(R.id.blockMetode);
    linePengiriman = findViewById(R.id.linePengiriman);
    blockAlamat = findViewById(R.id.blockAlamat);
    blockImagePembayaran = findViewById(R.id.blockImagePembayaran);
    blockImagePengiriman = findViewById(R.id.blockImagePengiriman);
    blockMetodePembayaran = findViewById(R.id.blockMetodePembayaran);
    blockMetodePengiriman = findViewById(R.id.blockMetodePengiriman);
    blockTanggal = findViewById(R.id.blockTanggal);
    blockWaktu = findViewById(R.id.blockWaktu);
    txtTanggall = findViewById(R.id.txtTanggall);
    txtBulan = findViewById(R.id.txtBulan);
    txtTahun = findViewById(R.id.txtTahun);
    txtTanggall.setTypeface(MontserratRegular);
    txtBulan.setTypeface(MontserratRegular);
    txtTahun.setTypeface(MontserratRegular);
    Log.d("hour", hour.toString());
    list_paket = findViewById(R.id.blockPesanan);
    txtNoPesanan = findViewById(R.id.txtNoPesanan);
    txtNoPesanan.setTypeface(MontserratRegular);
    txtLokasi = findViewById(R.id.txtLokasi);
    txtLokasi.setTypeface(MontserratRegular);

    list = new ArrayList<>();

    icBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    if (pesanan == 1){
      jenis = "antar";
      txtLokasi.setVisibility(View.GONE);
      blockMetode.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
      dikirim.setTextColor(ContextCompat.getColor(getApplicationContext(),android.R.color.white));
      txtInfo.setText("Pesanan akan dikirim ke");
      blockPengiriman.setVisibility(View.VISIBLE);
      linePengiriman.setVisibility(View.VISIBLE);
      blockOngkir.setVisibility(View.VISIBLE);
      txtJam.setText("Jam Pengiriman");

      blockAlamat.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          dialog = new AlertDialog.Builder(Berlangganan.this, R.style.DialogPutih);
          inflater = getLayoutInflater();
          dialogView = inflater.inflate(R.layout.popup_pilih_outlet, null);
          dialog.setView(dialogView);
          blockOutlet = dialogView.findViewById(R.id.blockOutlet);
          TextView txtTitle = dialogView.findViewById(R.id.pilihan_title);
          txtTitle.setText("Pilih Alamat");
          TextView txtTambahAlamat = dialogView.findViewById(R.id.tambahAlamat);
          txtTambahAlamat.setVisibility(View.VISIBLE);
          pBar = dialogView.findViewById(R.id.pBar);
          alertDialog = dialog.create();

          txtTambahAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getApplicationContext(), MenentukanAlamat.class);
              startActivity(intent);
            }
          });

          new GetOutlet().execute();

          alertDialog.show();
        }
      });


    }else {
      jenis = "ambil";
      ambilsendiri.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
      ambilsendiri.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
      txtJam.setText("Jam Diambil");

      blockAlamat.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          dialog = new AlertDialog.Builder(Berlangganan.this, R.style.DialogPutih);
          inflater = getLayoutInflater();
          dialogView = inflater.inflate(R.layout.popup_pilih_outlet, null);
          dialog.setView(dialogView);
          blockOutlet = dialogView.findViewById(R.id.blockOutlet);
          TextView txtTitle = dialogView.findViewById(R.id.pilihan_title);
          txtTitle.setText("Pilih Outlet");
          TextView txtTambahAlamat = dialogView.findViewById(R.id.tambahAlamat);
          pBar = dialogView.findViewById(R.id.pBar);
          alertDialog = dialog.create();

          txtTambahAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getApplicationContext(), MenentukanAlamat.class);
              String dari = "berlangganan";
              editor.putString("dari", dari);
              editor.apply();
              startActivity(intent);
            }
          });

          new GetOutlet().execute();

          alertDialog.show();
        }
      });
    }

    if (!hour.equals("0")){
      jam.setText(hour + " : " + mnt);
    }

    if (!tgl.equals("")){
      txtTanggall.setText(tgl + " " + bln + " " + thn);
    }else {
      Calendar now = Calendar.getInstance();
      now.add(Calendar.DATE, 1);
      now.get(Calendar.DATE + Calendar.MONTH + Calendar.YEAR);
      DateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
      txtTanggall.setText(dateFormatter.format(now.getTime()));
      bulann = mnth.format(now.getTime());
      tgl = date.format(now.getTime());
      thn = Integer.valueOf(yearr.format(now.getTime()));

    }

    blockTanggal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(Berlangganan.this, new DatePickerDialog.OnDateSetListener() {

          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);

            bln = month.format(newDate.getTime());
            bulann = mnth.format(newDate.getTime());
            tgl = date.format(newDate.getTime());
            thn = Integer.valueOf(yearr.format(newDate.getTime()));
            txtTanggall.setText(tgl + " " + bln + " " + thn);

            editor.putString("tanggal", date.format(newDate.getTime()));
            editor.putString("bulan", month.format(newDate.getTime()));
            editor.putString("bln", mnth.format(newDate.getTime()));
            editor.putInt("tahun", Integer.valueOf(yearr.format(newDate.getTime())));
            editor.apply();

          }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
      }
    });

    jam.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar mcurrentTime = Calendar.getInstance();
        final int hourr = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Berlangganan.this, new TimePickerDialog.OnTimeSetListener() {


          @Override
          public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (hourOfDay <= 9 && hourOfDay >= 0) {
              hour = "0" + hourOfDay;
            }else {
              hour = String.valueOf(hourOfDay);
            }
            if (minute <= 9 && minute >= 0) {
              mnt = "0" + minute;
            }else {
              mnt = String.valueOf(minute);
            }
            jam.setText(hour + " : " + mnt);

            editor.putString("jam", hour);
            editor.putString("menit", mnt);
            editor.apply();
          }
        }, hourr, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Pilih Jam");
        mTimePicker.show();

      }
    });

    btnPesan.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d("id_pembayaran", id_pembayaran.toString());
        if (id_pembayaran != 0) {
          if (!hour.equals("0") && thn != 0) {
              if (pesanan == 1) {
               if (nama_pembayaran.equals("Cash")) {
                 Toast.makeText(getApplicationContext(), "Tidak dapat memilih metode pembayaran tunai jika orderan dikirimkan", Toast.LENGTH_LONG).show();
               } else {
                new simpanorder().execute();
               }
             } else {
              new simpanorder().execute();
              }
           }else {
          Toast.makeText(getApplicationContext(), "Silihkan pilih tanggal mulai dan jam pengiriman", Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(getApplicationContext(), "Silihkan pilih salah satu metode pembayaran", Toast.LENGTH_LONG).show();
        }
      }
    });




    dikirim.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        pesanan = 1;
        Log.d("pesanan", pesanan.toString());
        Intent a = new Intent(getApplicationContext(), Berlangganan.class);
        a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        a.putExtra("pesanan", pesanan);
        startActivity(a);
      }
    });

    ambilsendiri.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        pesanan = 2;
        Intent b = new Intent(getApplicationContext(), Berlangganan.class);
        b.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        b.putExtra("pesanan", pesanan);
        startActivity(b);
      }
    });



    status = "daftar";
    editor.putString("status", status);
    editor.apply();
    new GetCheckout().execute();

  }

  @Override
  public void onBackPressed() {
    if (hasil == 0){
      new DeleteTempCart().execute();
    }else {
      Intent intent = new Intent(getApplicationContext(), DaftarPaket.class);
      startActivity(intent);
    }
  }

  private class GetOutlet extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... strings) {

      ArrayList params = new ArrayList();

      params.add(new BasicNameValuePair("pesanan", pesanan.toString()));
      if (pesanan == 2) {
        params.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
        params.add(new BasicNameValuePair("long", String.valueOf(longtitude)));

      }else {
        params.add(new BasicNameValuePair("lat", String.valueOf(lat_outlet)));
        params.add(new BasicNameValuePair("long", String.valueOf(long_outlet)));
      }
      params.add(new BasicNameValuePair("noHp", noHp));

      JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetOutlet, "POST", params);
      return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
      pBar.setVisibility(View.GONE);

      try {
        if (result != null){
          if (result.getInt("error") == 1){
            Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
          }
          else {
            JSONArray daftar = new JSONArray(result.getString("list"));
            if (daftar.length() > 0){

              LinearLayout linearLayout= new LinearLayout(Berlangganan.this);
              linearLayout.setOrientation(LinearLayout.VERTICAL);
              linearLayout.setId(View.generateViewId());

              for (int i=0; i < daftar.length(); i++){

                final JSONObject outlet = daftar.getJSONObject(i);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                layoutParams.setMargins((int) (20 * factor), 0, (int) (15 * factor), (int) (15 * factor));
                linearLayout.setLayoutParams(layoutParams);

                ViewGroup.LayoutParams layoutNama = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView nama= new TextView(Berlangganan.this);
                nama.setLayoutParams(layoutNama);
                nama.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), 0);
                nama.setTextSize(16);
                nama.setAllCaps(true);
                nama.setText(outlet.getString("nama"));
                nama.setTextColor(getResources().getColor(android.R.color.black));
                nama.setMaxLines(1);
                nama.setEllipsize(TextUtils.TruncateAt.END);
                linearLayout.addView(nama);

                ViewGroup.LayoutParams layoutJarak = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView jarak= new TextView(Berlangganan.this);
                jarak.setLayoutParams(layoutJarak);
                jarak.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), 0);
                jarak.setTextSize(14);
                jarak.setAllCaps(true);
                jarak.setText(outlet.getString("jarak")+ " KM");
                jarak.setTextColor(getResources().getColor(android.R.color.black));
                jarak.setMaxLines(1);
                jarak.setEllipsize(TextUtils.TruncateAt.END);
                linearLayout.addView(jarak);

                ViewGroup.LayoutParams layoutAlamat= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView alamat = new TextView(Berlangganan.this);
                alamat.setLayoutParams(layoutAlamat);
                alamat.setPadding((int) (10 * factor), (int) (5 * factor), (int) (8 * factor), (int) (8 * factor));
                alamat.setTextSize(14);
                if (pesanan == 2){
                  alamat.setText(outlet.getString("lokasi") + ", " + outlet.getString("alamat"));}
                else {
                  alamat.setText( outlet.getString("alamat"));
                }
                alamat.setMaxLines(3);
                alamat.setEllipsize(TextUtils.TruncateAt.END);
                linearLayout.addView(alamat);

                if (pesanan == 2){
                  nama.setOnClickListener(new selesai(outlet.getInt("id"), "outlet", "", "", outlet.getString("nama"), outlet.getString("jarak")));
                  alamat.setOnClickListener(new selesai(outlet.getInt("id"), "outlet","","", outlet.getString("nama"), outlet.getString("jarak"))); }
                else {
                  nama.setOnClickListener(new selesai(outlet.getInt("id"), "alamat", outlet.getString("alamat"), outlet.getString("koordinat"), outlet.getString("nama"), outlet.getString("jarak")));
                  alamat.setOnClickListener(new selesai(outlet.getInt("id"), "alamat", outlet.getString("alamat"), outlet.getString("koordinat"), outlet.getString("nama"), outlet.getString("jarak")));
                }
              }


              blockOutlet.addView(linearLayout);
            }
          }
        }else {
          Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
        }
      } catch (JSONException e){
        e.printStackTrace();
      }
    }
  }

  public class selesai implements View.OnClickListener{
    Integer id_pilih;
    String kat, alamat, koordinat, nama, jarak;
    public selesai(Integer id, String kat, String alamat, String koordinat, String nama, String jarak) {
      this.id_pilih = id;
      this.kat = kat;
      this.alamat = alamat;
      this.koordinat = koordinat;
      this.nama = nama;
      this.jarak = jarak;
    }

    @Override
    public void onClick(View v) {
      if (kat == "outlet"){
        pesanan = 2;

        AlertDialog.Builder builder1 = new AlertDialog.Builder(Berlangganan.this);
        builder1.setTitle("Keranjang Belanja Tidak Kosong");
        builder1.setMessage("Jika anda mengganti outlet, maka keranjang belanja anda akan dikosongkan. Apakah anda akan berganti outlet?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Iya",
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    id_outlet = id_pilih;
                    editor.putInt("id_outlet", id_pilih);
                    editor.putFloat("jarak2", Float.parseFloat(jarak));
                    editor.apply();
                    new DeleteTempCart().execute();
                    dialog.cancel();
                  }
                });

        builder1.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                  }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        alertDialog.dismiss();
      }
      else {
        //String[] area = alamat.split(",", 2);
        String[] titik = koordinat.split(",");
        lat_alamat = Double.parseDouble(titik[0]);
        long_alamat = Double.parseDouble(titik[1]);
        pesanan = 1;
        editor.putInt("id_alamat", id_pilih);
        editor.putString("nama_alamat", nama);
        editor.putString("alamat", alamat);
        editor.putString("lat_alamat", String.valueOf(lat_alamat));
        editor.putString("long_alamat", String.valueOf(long_alamat));
        editor.putInt("jarak", (int) Math.ceil(Float.parseFloat(jarak)));
        editor.apply();
        Intent intent = new Intent(Berlangganan.this, Berlangganan.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("pesanan", pesanan);
        startActivity(intent);
      }
    }

  }

  private class GetCheckout extends AsyncTask<String, Void, JSONObject>{

    @Override
    protected JSONObject doInBackground(String... strings) {
      ArrayList params = new ArrayList();

      params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
      params.add(new BasicNameValuePair("noHp", noHp));
      params.add(new BasicNameValuePair("jenis", "berlangganan"));

      JSONObject jsonObject = jsonParser.makeHttpRequest(urlGetCheckout, "POST", params);

      return jsonObject;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

      pDialog = new ProgressDialog(Berlangganan.this);
      pDialog.setMessage("Mengambil data...");
      pDialog.setIndeterminate(false);
      pDialog.setCancelable(true);
      pDialog.show();
    }

    @Override
    protected void onPostExecute(JSONObject result) {

      try {
        if (result != null){
          if (result.getInt("error") == 1){
            Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
          }else {

            Integer banyak = result.getInt("banyak");
            if(banyak > 0) {
              JSONArray daftar = new JSONArray(result.getString("pesanan"));
              if (daftar.length() > 0) {
                for (int i = 0; i < daftar.length(); i++) {
                  final JSONObject paket1 = daftar.getJSONObject(i);

                  modal_berlangganan modalBerlangganan = new modal_berlangganan();
                  modalBerlangganan.setNama(paket1.getString("nama"));
                  modalBerlangganan.setThumbnail(paket1.getString("thumbnail"));
                  modalBerlangganan.setId_paket(paket1.getInt("id_paket"));
                  id_paket = paket1.getInt("id_paket");
                  modalBerlangganan.setNormal_price(paket1.getInt("normal_price"));
                  modalBerlangganan.setSubscribe_price(paket1.getInt("subscribe_price"));
                  hasil = paket1.getInt("subscribe_price");
                  modalBerlangganan.setOngkos_kirim(paket1.getInt("ongkos_kirim"));
                  ongkos = paket1.getInt("ongkos_kirim");
                  session = paket1.getString("session");

                  editor.putInt("id_paket", id_paket);
                  editor.putString("session", session);
                  editor.apply();

                  modalBerlangganan.setPeriode(paket1.getInt("periode"));
                  modalBerlangganan.setIsi(paket1.getString("keterangan"));

                  list.add(modalBerlangganan);
                  adapterBerlangganan = new adapter_berlangganan(Berlangganan.this, R.layout.cv_paket, list);
                  list_paket.setAdapter(adapterBerlangganan);

                  harga.setText("Rp " + format.format(hasil));
                }
              }
            }else {
              txtNoPesanan.setVisibility(View.VISIBLE);
              hasil = 0;
            }

            JSONArray metodePengiriman = new JSONArray(result.getString("pengiriman"));
            if (metodePengiriman.length() > 0) {
              for (int i = 0; i < metodePengiriman.length(); i++) {

                final JSONObject pengiriman = metodePengiriman.getJSONObject(i);

                final Integer id = pengiriman.getInt("id_pengiriman");
                final Integer harga = pengiriman.getInt("harga_per_km");

                ImageView thumbnail = new ImageView(Berlangganan.this);
                thumbnail.setPadding((int) (0 * factor),(int) (5 * factor) , (int) (0 * factor), (int) (5 * factor));
                Picasso.get().load(dataUrl + "metode/" + pengiriman.getString("thumbnail")).into(thumbnail);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (100 * factor), (int) (50 * factor));
                layoutParams.setMargins((int) (5 * factor),(int) (5 * factor) , (int) (0 * factor), (int) (5 * factor));
                thumbnail.setLayoutParams(layoutParams);

                blockImagePengiriman.addView(thumbnail);

                LinearLayout.LayoutParams layoutButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * factor));
                layoutButton.setMargins((int)(0*factor), (int)(5*factor), (int)(0*factor), (int)(5 * factor));


                final RadioButton rdBtn = new RadioButton(Berlangganan.this);
                rdBtn.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
                rdBtn.setLayoutParams(layoutButton);
                rdBtn.setPadding((int)(0*factor), (int)(0*factor), (int)(0*factor), (int)(0*factor));
                rdBtn.setId(id);

              blockMetodePengiriman.addView(rdBtn);


                if (id_pengiriman == rdBtn.getId()){
                     Log.d("jarak", String.valueOf(jarak));
                     rdBtn.setChecked(true);
                     harga_per_km = harga;
                     id_pengiriman = rdBtn.getId();
                     editor.putInt("id_pengiriman", id_pengiriman);
                     editor.apply();
                }


                rdBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    harga_per_km = harga;
                    id_pengiriman = rdBtn.getId();
                    Log.d("jarak", String.valueOf(id_pengiriman));
                    editor.putInt("id_pengiriman", id_pengiriman);
                    editor.apply();

                    cekharga();
                  }
                });

              }
            }

            JSONArray metodePembayaran = new JSONArray(result.getString("pembayaran"));
            if (metodePembayaran.length() > 0) {
              for (int i = 0; i < metodePembayaran.length(); i++) {

                final JSONObject pembayaran = metodePembayaran.getJSONObject(i);

                final Integer id = pembayaran.getInt("id_pembayaran");
                final String ket = pembayaran.getString("keterangan_pembayaran");
                final String nama = pembayaran.getString("nama");
                final String no = pembayaran.getString("no_tujuan");
                final String a_n = pembayaran.getString("atas_nama");

                ImageView thumbnail = new ImageView(Berlangganan.this);
                thumbnail.setPadding((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));
                Picasso.get().load(dataUrl + "metode/" + pembayaran.getString("thumbnail")).into(thumbnail);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (100 * factor), (int) (50 * factor));
                layoutParams.setMargins((int) (5 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));
                thumbnail.setLayoutParams(layoutParams);

                blockImagePembayaran.addView(thumbnail);

                LinearLayout.LayoutParams layoutButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (50 * factor));
                layoutButton.setMargins((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));

                final RadioButton rdBtn = new RadioButton(Berlangganan.this);
                rdBtn.setLayoutParams(layoutButton);
                rdBtn.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
                rdBtn.setPadding((int) (0 * factor), (int) (0 * factor), (int) (0 * factor), (int) (0 * factor));
                rdBtn.setId(pembayaran.getInt("id_pembayaran"));


                if (id_pembayaran == rdBtn.getId()) {
                  rdBtn.setChecked(true);
                  editor.putInt("id_pembayaran", id_pembayaran);
                }

                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins((int) (0 * factor), (int) (5 * factor), (int) (0 * factor), (int) (5 * factor));

                rdBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    id_pembayaran = rdBtn.getId();
                    sketerangan = ket;
                    nama_pembayaran = nama;
                    no_tujuan = no;
                    atas_nama = a_n;
                    Log.d("pembayaran", id_pembayaran.toString());
                    editor.putInt("id_pembayaran", id_pembayaran);
                    editor.putString("nama_pembayaran", nama_pembayaran);
                    editor.putString("keterangan", sketerangan);
                    editor.putString("no_tujuan",no_tujuan);
                    editor.apply();
                  }
                });

                blockMetodePembayaran.addView(rdBtn);
              }
            }

            id_outlet = result.getInt("id_outlet");
            editor.putInt("id_outlet", id_outlet).apply();

            String titik = result.getString("koordinat");
            String[] koordinat = titik.split(",");
            double titikLatitude = Double.parseDouble(koordinat[0]);
            lat_outlet = titikLatitude;
            double titikLongtitude = Double.parseDouble(koordinat[1]);
            long_outlet = titikLongtitude;
            editor.putString("lat_outlet", String.valueOf(lat_outlet));
            editor.putString("long_outlet", String.valueOf(long_outlet));
            editor.apply();
            final float[] hasill = new float[1];
            if (pesanan == 2){
              nama_outlet = result.getString("nama_outlet");
              lokasi = result.getString("lokasi");
              detail_outlet = result.getString("alamat_outlet");
              new hitungjarak().execute(String.valueOf(latitude), String.valueOf(longtitude));}
            else {
              if (id_alamat != 0){
                new hitungjarak().execute(String.valueOf(lat_alamat), String.valueOf(long_alamat));}
              else {
                String namaarea[] = detailAlamat.split(",");
                Location.distanceBetween(titikLatitude, titikLongtitude, latitude, longtitude, hasill);
                jarak = (int) Math.ceil(hasill[0]/1000);
                txtAlamat.setText(nama_alamat + " ( " + decimalFormat.format(hasill[0]/1000) + " KM )");
                txtDetailAlamat.setText(detailAlamat) ;

                cekharga();
              }
            }

          }
        }else {
          Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
        }
      } catch (JSONException e){
        e.printStackTrace();
      }
    }
  }

  private class hitungjarak extends AsyncTask<String, Void, JSONObject>{

    @Override
    protected JSONObject doInBackground(String... args) {

      ArrayList params = new ArrayList();
      params.add(new BasicNameValuePair("lat_alamat", args[0]));
      params.add(new BasicNameValuePair("long_alamat", args[1]));
      params.add(new BasicNameValuePair("lat_outlet", String.valueOf(lat_outlet)));
      params.add(new BasicNameValuePair("long_outlet", String.valueOf(long_outlet)));

      JSONObject jsonObject = jsonParser.makeHttpRequest(urlHitungJarak, "POST", params);
      return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

      try {
        if (result != null){
          if (result.getInt("error") == 1){
            Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
          }
          else {
            if (pesanan == 1){
              jarak = (int) Math.ceil(Float.parseFloat(result.getString("jarak")));
              editor.putInt("jarak", (int) jarak);
              editor.apply();
              txtAlamat.setText(nama_alamat + " ( " + decimalFormat.format(Float.parseFloat(result.getString("jarak"))) + " KM )");
              txtDetailAlamat.setText(alamat);
            }else{
              distance = Float.parseFloat(result.getString("jarak"));
              editor.putFloat("jarak2", distance);
              editor.apply();
              txtAlamat.setText(nama_outlet + " (" + decimalFormat.format(distance) + " KM )");
              txtLokasi.setText(lokasi);
              txtDetailAlamat.setText(detail_outlet);
            }

            cekharga();
          }
        }else {
          Toast.makeText(getApplicationContext(), "Tidak dapat mengambil data dari server", Toast.LENGTH_LONG).show();
        }
      } catch (JSONException e){
        e.printStackTrace();
      }
    }
  }

  public void cekharga(){
        pDialog.dismiss();
        pDialog = null;

      if (hasil == 0) {
      btnPesan.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
      btnPesan.setClickable(false);
      hargaOngkir =0;
      blockOngkir.setVisibility(View.GONE);
      hargaTotal = 0;
    }else {
      if (pesanan ==  1){
              if (ongkos == 0) {
                  if (jarak <= 4) {
                      hargaOngkir = 8000;
                  } else {
                      hargaOngkir = harga_per_km * jarak;
                  }
              } else {
                  hargaOngkir = 0;
                  blockOngkir.setVisibility(View.GONE);
              }
      }else {
            hargaOngkir = 0;
            hargaTotal = hasil;
      }
          hargaTotal = (int) (hasil + hargaOngkir);
    }

    ongkir.setText("Rp " + format.format(hargaOngkir));
    harga.setText("Rp " + format.format(hasil));
    total.setText("Rp " + format.format(hargaTotal));

  }

  private class GantiPesanan extends AsyncTask<String, Void, JSONObject>{

    @Override
    protected JSONObject doInBackground(String... strings) {

      ArrayList params = new ArrayList();
      params.add(new BasicNameValuePair("noHp", noHp));
      params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
      params.add(new BasicNameValuePair("id_paket", id_paket.toString()));
      params.add(new BasicNameValuePair("session", session));

      JSONObject jsonObject = jsonParser.makeHttpRequest(urlGantiPesanan, "POST", params);
      return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
      try {
        if (jsonObject != null){
          if (jsonObject.getInt("error") == 1){

          }else {
            //Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Berlangganan.this, Berlangganan.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("pesanan", pesanan);
            startActivity(intent);
          }
        }
      }catch (JSONException e){
        e.printStackTrace();
      }

    }
  }

  private class DeleteTempCart extends AsyncTask<String, Void, JSONObject>{

    @Override
    protected JSONObject doInBackground(String... strings) {

      ArrayList params = new ArrayList();

      params.add(new BasicNameValuePair("noHp", noHp));

      JSONObject jsonObject = jsonParser.makeHttpRequest(urlDeleteTempCart, "POST", params);
      return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
      super.onPostExecute(jsonObject);

      try {
        if (jsonObject != null){
          if (jsonObject.getInt("error") == 1){

          }else{
            Intent intent = new Intent(getApplicationContext(), DaftarPaket.class);
            sharedPreferences.edit().remove("hasil").apply();
            sharedPreferences.edit().remove("id_pembayaran").apply();
            sharedPreferences.edit().remove("id_pengiriman").apply();
            sharedPreferences.edit().remove("total").apply();
            sharedPreferences.edit().remove("ongkir").apply();
            sharedPreferences.edit().remove("harga_per_km").apply();
            sharedPreferences.edit().remove("jarak").apply();
            sharedPreferences.edit().remove("id_voucher").apply();
            sharedPreferences.edit().remove("kode").apply();
            startActivity(intent);
          }
        }
      }catch (JSONException e){
        e.printStackTrace();
      }
    }
  }

  private class simpanorder extends AsyncTask<String, Void, JSONObject>{

    @Override
    protected JSONObject doInBackground(String... strings) {

      ArrayList params = new ArrayList();
      params.add(new BasicNameValuePair("noHp", noHp));
      params.add(new BasicNameValuePair("id_outlet", id_outlet.toString()));
      params.add(new BasicNameValuePair("id_alamat", String.valueOf(id_alamat)));
      params.add(new BasicNameValuePair("jenis", jenis));
      params.add(new BasicNameValuePair("id_pembayaran", id_pembayaran.toString()));
      params.add(new BasicNameValuePair("hargaOngkir", String.valueOf(hargaOngkir)));
      params.add(new BasicNameValuePair("id_voucher", "0"));
      params.add(new BasicNameValuePair("id_pengiriman", String.valueOf(id_pengiriman)));
      params.add(new BasicNameValuePair("id_paket", id_paket.toString()));
      params.add(new BasicNameValuePair("tahun", thn.toString()));
      params.add(new BasicNameValuePair("bulan", bulann));
      params.add(new BasicNameValuePair("tanggal", tgl));
      params.add(new BasicNameValuePair("jam", hour));
      params.add(new BasicNameValuePair("menit", mnt));
      params.add(new BasicNameValuePair("alamat", detailAlamat));
      params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
      params.add(new BasicNameValuePair("longtitude", String.valueOf(longtitude)));
      params.add(new BasicNameValuePair("harga", String.valueOf(hasil)));


      JSONObject jsonObject = jsonParser.makeHttpRequest(urlsimpanorder, "POST", params);

      return jsonObject;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

      pDialog = new ProgressDialog(Berlangganan.this);
      pDialog.setMessage("Mengirim data...");
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
            Intent intent = new Intent(getApplicationContext(), AfterOrder.class);
            intent.putExtra("from", 1);
            intent.putExtra("id_orderan", result.getString("id_orderan"));
            intent.putExtra("keterangan", sketerangan);
            intent.putExtra("total", hargaTotal);
            intent.putExtra("no_tujuan", no_tujuan);
            intent.putExtra("atas_nama", atas_nama);
            intent.putExtra("from", 0);
            sharedPreferences.edit().remove("hasil").apply();
            sharedPreferences.edit().remove("id_pembayaran").apply();
            sharedPreferences.edit().remove("id_pengiriman").apply();
            sharedPreferences.edit().remove("total").apply();
            sharedPreferences.edit().remove("ongkir").apply();
            sharedPreferences.edit().remove("harga_per_km").apply();
            sharedPreferences.edit().remove("jarak").apply();
            sharedPreferences.edit().remove("id_voucher").apply();
            sharedPreferences.edit().remove("kode").apply();
            sharedPreferences.edit().remove("tanggal").apply();
            sharedPreferences.edit().remove("bulan").apply();
            sharedPreferences.edit().remove("tahun").apply();
            sharedPreferences.edit().remove("jam").apply();
            sharedPreferences.edit().remove("menit").apply();
            startActivity(intent);
          }
        }
      }catch (JSONException e){
        e.printStackTrace();
      }
    }
  }
}
