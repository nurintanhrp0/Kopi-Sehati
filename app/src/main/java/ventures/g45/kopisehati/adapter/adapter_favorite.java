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
import ventures.g45.kopisehati.modal.modal_favorite;
import ventures.g45.kopisehati.modal.modal_pesanan;

public class adapter_favorite extends ArrayAdapter<modal_favorite> {
	private List<modal_favorite> daftarPesanan;
	private Context context;
	int layout;
	String  dataUrl = "https://datakopi.g45lab.xyz/";

	public adapter_favorite(@NonNull Context context, int layout, List<modal_favorite> daftarPesanan) {
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
			holder.thumbnail = v.findViewById(R.id.thumbnail);
			holder.namaProduk.setTypeface(MontserratRegular);

			v.setTag(holder);
		}
		else{
			holder=(PesananHolder) v.getTag();
		}

		modal_favorite pesanan= daftarPesanan.get(position);
		holder.namaProduk.setText(pesanan.getNama());
		Picasso.get().load(dataUrl + "menu/thumbnail/" + pesanan.getThumbnail()).into(holder.thumbnail);

		return v;
	}

	static class PesananHolder{
		TextView namaProduk;
		ImageView thumbnail;
	}
}
