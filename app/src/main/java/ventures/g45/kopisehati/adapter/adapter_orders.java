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

import java.util.List;

import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.modal.modal_menu;
import ventures.g45.kopisehati.modal.modal_orders;

public class adapter_orders extends ArrayAdapter<modal_orders> {
    private List<modal_orders> daftarOrders;
    private Context context;
    int layout;
    String  dataUrl = "https://datakopi.g45lab.xyz/";


    public adapter_orders(@NonNull Context context, int layout, List<modal_orders> daftarOrders) {
        super(context, layout, daftarOrders);
        this.context = context;
        this.layout = layout;
        this.daftarOrders = daftarOrders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        OrderHolder holder;

        AssetManager assetManager = getContext().getApplicationContext().getAssets();
        Typeface MontserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        if(v==null){
            LayoutInflater vi=((Activity)context).getLayoutInflater();
            v=vi.inflate(layout, parent,false);

            holder=new OrderHolder();

            holder.id_order = v.findViewById(R.id.noOrder);
            holder.waktu = v.findViewById(R.id.tanggal);
            holder.status_order = v.findViewById(R.id.status);
            holder.thumbnail = v.findViewById(R.id.qrcode);
            holder.id_order.setTypeface(MontserratRegular);
            holder.waktu.setTypeface(MontserratRegular);
            holder.status_order.setTypeface(MontserratRegular);

            v.setTag(holder);
        }
        else{
            holder=(OrderHolder) v.getTag();
        }

        modal_orders orders = daftarOrders.get(position);
        holder.id_order.setText("No Order   : " + orders.getId_orderan());
        holder.waktu.setText("Tanggal     : " + orders.getWaktu());
        holder.status_order.setText("Status        : " + orders.getStatus_orderan());
        Picasso.get().load(dataUrl + "qr_code/" + orders.getThumbnail()).into(holder.thumbnail);

        return v;
    }

    static class OrderHolder{
        TextView id_order, waktu, status_order;
        ImageView thumbnail;
    }
}
