package ventures.g45.kopisehati.modal;

import com.google.android.gms.maps.model.LatLng;

public class modal_alamat {
    String kategori, detail, catatan;
    String koordinat;
    Integer id_alamat;

    public modal_alamat(){}

    public modal_alamat(String kategori, String detail, String catatan, String koordinat, Integer id_alamat) {
        this.kategori = kategori;
        this.detail = detail;
        this.catatan = catatan;
        this.koordinat = koordinat;
        this.id_alamat = id_alamat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getKoordinat() {
        return koordinat;
    }

    public void setKoordinat(String koordinat) {
        this.koordinat = koordinat;
    }

    public Integer getId_alamat() {
        return id_alamat;
    }

    public void setId_alamat(Integer id_alamat) {
        this.id_alamat = id_alamat;
    }
}
