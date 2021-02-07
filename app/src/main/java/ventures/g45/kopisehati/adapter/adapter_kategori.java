package ventures.g45.kopisehati.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ventures.g45.kopisehati.DaftarMenu;
import ventures.g45.kopisehati.KopiSehati;
import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.modal.modal_kaegori;
import ventures.g45.kopisehati.modal.modal_menu;

public class adapter_kategori extends RecyclerView.Adapter<adapter_kategori.ViewHolder> {
    public static Object ViewHolder;
    private static int clickedTextViewPos;
    private List<modal_kaegori> data;
    private static Context context;
    int layout;
    DaftarMenu listener;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    String dataUrl = "https://datakopi.g45lab.xyz/";
    static Integer id_kategori;
    //private int clickedTextViewPos;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static Integer selected;
    Typeface MontserratRegular;
    public static final String PAYLOAD_NAME = "PAYLOAD_NAME";
    public static final String PAYLOAD_KOSONG = "PAYLOAD_KOSONG";

    public adapter_kategori(ArrayList<modal_kaegori> daftar, Context ctx) {

        sharedPreferences = ctx.getSharedPreferences("kopisehati", 0);
        editor = sharedPreferences.edit();
        id_kategori = sharedPreferences.getInt("id_kategori", 0);
        selected = sharedPreferences.getInt("selected", 0);
        clickedTextViewPos = sharedPreferences.getInt("click", 0);


        data = daftar;
        context = ctx;
        listener = (DaftarMenu) ctx;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_kategori,
                viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull adapter_kategori.ViewHolder viewHolder, int i) {
        viewHolder.nama.setText(data.get(i).getNama());
        Picasso.get().load(dataUrl + "kategori/" + data.get(i).getThumbnail()).into(viewHolder.thumbnail);

        if (data.get(i).getId_kategori() == i){
            viewHolder.line.setVisibility(View.VISIBLE);
            viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            viewHolder.nama.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }


        if (clickedTextViewPos == i) {

            viewHolder.line.setVisibility(View.VISIBLE);
            //viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            viewHolder.nama.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            id_kategori = data.get(i).getId_kategori();
            editor.putInt("id_kategori", id_kategori);
            editor.apply();
            if (selected == 0){
                Integer dari = 0;
                Log.d("selected", selected.toString());
                DaftarMenu.focusOnView(id_kategori, dari);
            }else {
                Integer dari = 1;
                Log.d("selected", selected.toString());

                DaftarMenu.focusOnView(selected, dari);
                editor.putInt("selected", 0);
                editor.apply();
            }
            editor.remove("click").apply();
            editor.putInt("selected", 0);
            editor.apply();

        } else {
            viewHolder.line.setVisibility(View.GONE);
            // viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, android.R.color.black));
            viewHolder.nama.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final adapter_kategori.ViewHolder viewHolder, final int i, List<Object> playLoad) {
        viewHolder.nama.setText(data.get(i).getNama());
        Picasso.get().load(dataUrl + "kategori/" + data.get(i).getThumbnail()).into(viewHolder.thumbnail);


        if (data.get(i).getId_kategori() == i){
            viewHolder.line.setVisibility(View.VISIBLE);
            viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            viewHolder.nama.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        if (!playLoad.isEmpty()) {
            for (final Object payload : playLoad) {
                String pay = (String) payload;
                if (pay.equals("ganti")){
                    viewHolder.line.setVisibility(View.VISIBLE);
                    //viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    viewHolder.nama.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    }
                else {
                    viewHolder.line.setVisibility(View.GONE);
                    // viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, android.R.color.black));
                    viewHolder.nama.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                }

            }

        }else {
            if (clickedTextViewPos == i) {

                viewHolder.line.setVisibility(View.VISIBLE);
                //viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                viewHolder.nama.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                id_kategori = data.get(i).getId_kategori();
                editor.putInt("id_kategori", id_kategori);
                editor.apply();
                if (selected == 0){
                    Integer dari = 0;
                    Log.d("selected", selected.toString());
                    DaftarMenu.focusOnView(id_kategori, dari);
                }else {
                    Integer dari = 1;
                    Log.d("selected", selected.toString());

                    DaftarMenu.focusOnView(selected, dari);
                    editor.putInt("selected", 0);
                    editor.apply();
                }
                editor.remove("click").apply();
                editor.putInt("selected", 0);
                editor.apply();

            } else {
                viewHolder.line.setVisibility(View.GONE);
                // viewHolder.thumbnail.setColorFilter(ContextCompat.getColor(context, android.R.color.black));
                viewHolder.nama.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cvMain;
        ImageView thumbnail;
        TextView nama;
        View line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            nama = (TextView) itemView.findViewById(R.id.nama);
            cvMain = (CardView) itemView.findViewById(R.id.cvMain);
            line = itemView.findViewById(R.id.line);

                //clickedTextViewPos = id_kategori + 1;


            cvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = 0;
                    String play = "";
                    clickedTextViewPos = getAdapterPosition();
                    notifyDataSetChanged();
                    //notifyItemChanged(id_kategori, PAYLOAD_KOSONG);

                }
            });

        }

    }





}

