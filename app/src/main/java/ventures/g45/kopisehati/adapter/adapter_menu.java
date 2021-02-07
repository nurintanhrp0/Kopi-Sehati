package ventures.g45.kopisehati.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import ventures.g45.kopisehati.DaftarMenu;
import ventures.g45.kopisehati.KopiSehati;
import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.modal.modal_menu;

public class adapter_menu extends ArrayAdapter<modal_menu> {
    private List<modal_menu> daftarMenu;
    private Context context;
    int layout;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    String  dataUrl = "https://datakopi.g45lab.xyz/";
    Integer id_menu =0;

    public adapter_menu(@NonNull Context context, int layout, List<modal_menu> daftarMenu) {
        super(context, layout, daftarMenu);
        this.daftarMenu = daftarMenu;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        MenuHolder holder;

        AssetManager assetManager = getContext().getApplicationContext().getAssets();
        Typeface MontserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        if(v==null){
            LayoutInflater vi=((Activity)context).getLayoutInflater();
            v=vi.inflate(layout, parent,false);

            holder=new MenuHolder();

            holder.nama = v.findViewById(R.id.namaProduk);
            holder.harga_jual = v.findViewById(R.id.harga);
            holder.thumbnail = v.findViewById(R.id.thumbnail);
            holder.txtNoMenu = v.findViewById(R.id.txtNoMenu);
            holder.daftarmenu = v.findViewById(R.id.daftarmenu);
            holder.nama.setTypeface(MontserratRegular);
            holder.harga_jual.setTypeface(MontserratRegular);
            holder.txtNoMenu.setTypeface(MontserratRegular);
            holder.txtNamaMenu = v.findViewById(R.id.txtNamaMenu);
            holder.txtNamaMenu.setTypeface(MontserratRegular);

            v.setTag(holder);
        }
        else{
            holder=(MenuHolder) v.getTag();
        }

        modal_menu menu = daftarMenu.get(position);
        if (menu.getNama().equals("null")){
            holder.daftarmenu.setVisibility(View.GONE);
            holder.txtNoMenu.setVisibility(View.VISIBLE);
            Log.d("nama kategori", String.valueOf(position));
            if (position == menu.getBaris() - 1) {
                holder.txtNamaMenu.setVisibility(View.VISIBLE);
                holder.txtNamaMenu.setText(menu.getNama_kategori());
            } else {
                holder.txtNamaMenu.setVisibility(View.GONE);

            }
        }else {
            if (position == menu.getBaris()) {
                holder.txtNoMenu.setVisibility(View.GONE);
                holder.txtNamaMenu.setVisibility(View.VISIBLE);
                holder.txtNamaMenu.setText(menu.getNama_kategori());
            } else {
                holder.txtNamaMenu.setVisibility(View.GONE);
                holder.daftarmenu.setVisibility(View.VISIBLE);
                holder.txtNoMenu.setVisibility(View.GONE);
            }
                holder.nama.setText(menu.getNama());
                holder.harga_jual.setText("Rp" + " " + decimalFormat.format(Integer.valueOf(menu.getHarga())));
                Picasso.get().load(dataUrl + "menu/thumbnail/" + menu.getThumbnail()).into(holder.thumbnail);

        }

        return v;
    }

    static class MenuHolder{
        TextView nama, harga_jual, txtNoMenu, txtNamaMenu;
        ImageView thumbnail;
        LinearLayout daftarmenu;
    }
}
