package ventures.g45.kopisehati.adapter;

import android.app.Activity;
import android.content.Context;
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

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.modal.modal_inbox;
import ventures.g45.kopisehati.modal.modal_pesanan;

public class adapter_pesanan extends ArrayAdapter<modal_pesanan> {
    private List<modal_pesanan> daftarPesanan;
    private Context context;
    int layout;
    String  dataUrl = "https://datakopi.g45lab.xyz/";
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");


    public adapter_pesanan(@NonNull Context context, int layout, List<modal_pesanan> daftarPesanan) {
        super(context, layout, daftarPesanan);
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
            holder.hargaProduk = v.findViewById(R.id.hargaProduk);
            holder.jenisProduk = v.findViewById(R.id.jenisProduk);
            holder.jumlahItem = v.findViewById(R.id.jumlahItem);
            holder.thumbnail = v.findViewById(R.id.thumbnail);
            holder.namaProduk.setTypeface(MontserratRegular);
            holder.hargaProduk.setTypeface(MontserratRegular);
            holder.jenisProduk.setTypeface(MontserratRegular);
            holder.jumlahItem.setTypeface(MontserratRegular);

            v.setTag(holder);
        }
        else{
            holder=(PesananHolder) v.getTag();
        }

        modal_pesanan pesanan= daftarPesanan.get(position);
        holder.namaProduk.setText(pesanan.getNama());
        holder.hargaProduk.setText("Rp " + decimalFormat.format(Integer.valueOf(pesanan.getHarga())));
        if (pesanan.getAddons().equals("null")) {
            holder.jenisProduk.setText(pesanan.getSize());
        }else {
            holder.jenisProduk.setText(pesanan.getSize() + " | " + pesanan.getAddons());
        }
        holder.jumlahItem.setText(pesanan.getQty());
        Picasso.get().load(dataUrl + "menu/thumbnail/" + pesanan.getThumbnail()).into(holder.thumbnail);

        return v;
    }

    static class PesananHolder{
        TextView namaProduk, hargaProduk, jenisProduk, jumlahItem;
        ImageView thumbnail;
    }
}
