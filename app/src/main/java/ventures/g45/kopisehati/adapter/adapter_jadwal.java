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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import ventures.g45.kopisehati.KopiSehati;
import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.modal.modal_berlangganan;
import ventures.g45.kopisehati.modal.modal_jadwal;

public class adapter_jadwal extends ArrayAdapter<modal_jadwal> {
    private List<modal_jadwal> daftarPesanan;
    private Context context;
    int layout;
    String  dataUrl = "https://datakopi.g45lab.xyz/", status;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Typeface  MontserratRegular;


    public adapter_jadwal(@NonNull Context context, int layout, List<modal_jadwal> daftarPesanan) {
        super(context, layout, daftarPesanan);

        sharedPreferences = context.getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();

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
        MontserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        if(v==null){
            LayoutInflater vi=((Activity)context).getLayoutInflater();
            v=vi.inflate(layout, parent,false);

            holder=new PesananHolder();

            holder.namaProduk = v.findViewById(R.id.namaProduk);
            holder.namaProduk.setTypeface(MontserratRegular);
            holder.qty = v.findViewById(R.id.qty);
            holder.qty.setTypeface(MontserratRegular);
            //holder.thumbnail = v.findViewById(R.id.thumbnail);
            holder.tanggal = v.findViewById(R.id.tanggal);
            holder.tanggal.setTypeface(MontserratRegular);
            holder.status = v.findViewById(R.id.status);
            holder.status.setTypeface(MontserratRegular);
            holder.jam = v.findViewById(R.id.jam);
            holder.jam.setTypeface(MontserratRegular);

            v.setTag(holder);
        }
        else{
            holder=(PesananHolder) v.getTag();
        }

        modal_jadwal pesanan= daftarPesanan.get(position);
        holder.namaProduk.setText(pesanan.getNama());
        holder.tanggal.setText(pesanan.getTanggal() + "");
        holder.qty.setText(pesanan.getCup() + " Cup");
        holder.jam.setText(pesanan.getJam());
        //Picasso.get().load(dataUrl + "menu/thumbnail/" + pesanan.getThumbnail()).into(holder.thumbnail);
        holder.status.setText(pesanan.getStatus());
        if (pesanan.getStatus().equals("cancel")){
            holder.status.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.tanggal.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }else if (pesanan.getStatus().equals("done")){
            holder.status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.hijau));
            holder.tanggal.setBackgroundColor(ContextCompat.getColor(context, R.color.hijau));
        }


        return v;
    }

    static class PesananHolder{
        TextView namaProduk, qty, tanggal, jam, status;
        ImageView thumbnail;
    }
}
