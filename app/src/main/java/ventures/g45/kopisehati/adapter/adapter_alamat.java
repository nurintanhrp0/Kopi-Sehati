package ventures.g45.kopisehati.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.datatransport.runtime.logging.Logging;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ventures.g45.kopisehati.DaftarAlamat;
import ventures.g45.kopisehati.DaftarMenu;
import ventures.g45.kopisehati.JSONParser;
import ventures.g45.kopisehati.MenentukanAlamat;
import ventures.g45.kopisehati.R;
import ventures.g45.kopisehati.SimpanAlamat;
import ventures.g45.kopisehati.modal.modal_alamat;


public class adapter_alamat extends ArrayAdapter<modal_alamat> {

    private List<modal_alamat> daftarAlamat;
    private Context context;
    int layout;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    Integer id_alamat;
    String urlSimpanAlamat = "https://kopi.g45lab.xyz/api/getsimpanalamat.html";

    public adapter_alamat(@NonNull Context context, int layout, List<modal_alamat> daftarAlamat) {
        super(context, layout, daftarAlamat);
        this.context = context;
        this.layout = layout;
        this.daftarAlamat = daftarAlamat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Holder holder;

        AssetManager assetManager = getContext().getApplicationContext().getAssets();
        Typeface MontserratRegular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        if (v == null) {
            LayoutInflater vi = ((Activity) context).getLayoutInflater();
            v = vi.inflate(layout, parent, false);

            holder = new Holder();

            holder.kategori = v.findViewById(R.id.kategori);
            holder.kategori.setTypeface(MontserratRegular);
            holder.detail = v.findViewById(R.id.detailAlamat);
            holder.detail.setTypeface(MontserratRegular);
            holder.edit = v.findViewById(R.id.edit);
            holder.hapus = v.findViewById(R.id.hapus);

            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        modal_alamat alamat = daftarAlamat.get(position);
        holder.kategori.setText(alamat.getKategori());
        holder.detail.setText(alamat.getDetail());
        Log.d("kategori", alamat.getKategori());

        if (alamat.getKategori().length() < 0 || alamat.getKategori().equals("null") || alamat.getKategori().equals("")){
            holder.kategori.setVisibility(View.GONE);
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] latlong =  alamat.getKoordinat().split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);
                LatLng location = new LatLng(latitude, longitude);
                Intent intent = new Intent(v.getContext(), MenentukanAlamat.class);
                intent.putExtra("detailAlamat", alamat.getDetail());
                intent.putExtra("latitude", latitude);
                intent.putExtra("longtitude", longitude);
                intent.putExtra("kategori", alamat.getKategori());
                intent.putExtra("catatan", alamat.getCatatan());
                intent.putExtra("id_alamat", alamat.getId_alamat());
                v.getContext().startActivity(intent);
            }
        });

        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setMessage("Apakah Anda yakin ingin menghapus alamat ini?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Iya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                id_alamat = alamat.getId_alamat();
                                new SaveAlamat().execute();

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

            }
        });

        return v;
    }

    static class Holder {
        TextView kategori, detail;
        ImageView edit, hapus;
    }

    private class SaveAlamat extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Mengirim data...");
            pDialog.setCancelable(true);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            ArrayList params = new ArrayList();

            params.add(new BasicNameValuePair("id_alamat", id_alamat.toString()));
            params.add(new BasicNameValuePair("hapus", "hapus"));

            JSONObject jsonObject = jsonParser.makeHttpRequest(urlSimpanAlamat, "POST", params);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if ((pDialog != null) && (pDialog.isShowing()))
                pDialog.dismiss();
            pDialog = null;

            try {
                if (result != null){
                    if (result.getInt("error") == 1){
                        Intent intent = new Intent(context, DaftarAlamat.class);
                        context.startActivity(intent);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
