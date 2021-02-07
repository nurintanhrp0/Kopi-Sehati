package ventures.g45.kopisehati.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.modal.modal_berlangganan;
import ventures.g45.kopisehati.modal.modal_pesanan;

public class adapter_berlangganan extends ArrayAdapter<modal_berlangganan> {

  private List<modal_berlangganan> daftarPesanan;
  private Context context;
  int layout;
  String  dataUrl = "https://datakopi.g45lab.xyz/", status;
  DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;


  public adapter_berlangganan(@NonNull Context context, int layout, List<modal_berlangganan> daftarPesanan) {
    super(context, layout, daftarPesanan);

    sharedPreferences = context.getSharedPreferences("kopisehati", 0);
    editor = sharedPreferences.edit();
    status = sharedPreferences.getString("status", "");

    this.context = context;
    this.layout = layout;
    this.daftarPesanan = daftarPesanan;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View v=convertView;
    PesananHolder holder;

    AssetManager assetManager = getContext().getApplicationContext().getAssets();
    Typeface MontserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

    if(v==null){
      LayoutInflater vi=((Activity)context).getLayoutInflater();
      v=vi.inflate(layout, parent,false);

      holder=new PesananHolder();

      holder.namaProduk = v.findViewById(R.id.namaProduk);
      holder.qty = v.findViewById(R.id.qty);
      holder.thumbnail = v.findViewById(R.id.thumbnail);
      holder.isi = v.findViewById(R.id.isi);
      holder.periode = v.findViewById(R.id.periode);
      holder.normal_price = v.findViewById(R.id.normal_price);
      holder.subsribe_price = v.findViewById(R.id.subscribe_price);
      holder.line = v.findViewById(R.id.line);
      holder.cvMain = v.findViewById(R.id.cvMain);
      holder.detail = v.findViewById(R.id.detail);
      holder.thumbnail2 = v.findViewById(R.id.thumbnail2);

      holder.namaProduk.setTypeface(MontserratRegular);
      holder.qty.setTypeface(MontserratRegular);
      holder.isi.setTypeface(MontserratRegular);
      holder.periode.setTypeface(MontserratRegular);
      holder.normal_price.setTypeface(MontserratRegular);
      holder.subsribe_price.setTypeface(MontserratRegular);

      v.setTag(holder);
    }
    else{
      holder=(PesananHolder) v.getTag();
    }

    modal_berlangganan pesanan= daftarPesanan.get(position);
    holder.namaProduk.setText(pesanan.getNama());
    if (status.equals("daftar")){
      Picasso.get().load(dataUrl + "paket/list/" + pesanan.getThumbnail()).into(holder.thumbnail2);
      holder.periode.setText(pesanan.getPeriode() + " hari");
      holder.normal_price.setText("Rp " + decimalFormat.format(pesanan.getNormal_price()));
      holder.subsribe_price.setText("Rp " + decimalFormat.format(pesanan.getSubscribe_price()));
      holder.isi.setText(pesanan.getIsi());

    }
    else {
      holder.thumbnail2.setVisibility(View.GONE);
      holder.thumbnail.setVisibility(View.VISIBLE);
      holder.detail.setVisibility(View.VISIBLE);
      holder.line.setVisibility(View.GONE);
      holder.subsribe_price.setVisibility(View.GONE);
      holder.normal_price.setVisibility(View.GONE);
      holder.isi.setVisibility(View.GONE);
      holder.periode.setVisibility(View.GONE);
      holder.qty.setVisibility(View.VISIBLE);
      holder.qty.setText(String.valueOf(pesanan.getQty()) + " Cup");
      Picasso.get().load(dataUrl + "menu/thumbnail/" + pesanan.getThumbnail()).into(holder.thumbnail);
    }

    return v;
  }

  static class PesananHolder{
    TextView namaProduk, qty, periode, normal_price, subsribe_price, isi;
    ImageView thumbnail, thumbnail2;
    View line;
    CardView cvMain;
    LinearLayout detail;
  }
}
