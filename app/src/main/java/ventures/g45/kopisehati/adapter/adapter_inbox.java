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

import ventures.g45.kopisehati.Inbox;
import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.modal.modal_inbox;


public class adapter_inbox extends ArrayAdapter<modal_inbox> {
    private List<modal_inbox> daftarInbox;
    private Context context;
    int layout;
    String  dataUrl = "https://datakopi.g45lab.xyz/";


    public adapter_inbox(@NonNull Context context, int layout, List<modal_inbox> daftarInbox) {
        super(context, layout, daftarInbox);
        this.context = context;
        this.layout = layout;
        this.daftarInbox = daftarInbox;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        InboxHolder holder;

        AssetManager assetManager = getContext().getApplicationContext().getAssets();
        Typeface MontserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        if(v==null){
            LayoutInflater vi=((Activity)context).getLayoutInflater();
            v=vi.inflate(layout, parent,false);

            holder=new InboxHolder();

            holder.judul = v.findViewById(R.id.title);
            holder.isi = v.findViewById(R.id.isi);
            holder.thumbnail = v.findViewById(R.id.thumbnail);
            holder.judul.setTypeface(MontserratRegular);
            holder.isi.setTypeface(MontserratRegular);

            v.setTag(holder);
        }
        else{
            holder=(InboxHolder) v.getTag();
        }

        modal_inbox inbox= daftarInbox.get(position);
        holder.judul.setText(inbox.getJudul());
        holder.isi.setText(inbox.getKeterangan());
        Picasso.get().load(dataUrl + "notif/" + inbox.getThumbnail()).into(holder.thumbnail);

        return v;
    }

    static class InboxHolder{
        TextView judul, isi;
        ImageView thumbnail;
    }
}
