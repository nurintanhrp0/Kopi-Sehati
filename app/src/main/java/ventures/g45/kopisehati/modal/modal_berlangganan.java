package ventures.g45.kopisehati.modal;

import android.widget.LinearLayout;

public class modal_berlangganan {
    String nama, thumbnail, nama_menu, isi;
    Integer qty, id_paket, normal_price, subscribe_price, id_menu, ongkos_kirim, periode;

    public modal_berlangganan(){}

    public modal_berlangganan(String nama, String thumbnail, String nama_menu, String isi, Integer qty, Integer id_paket, Integer normal_price, Integer subscribe_price, Integer id_menu, Integer ongkos_kirim, Integer periode) {
        this.nama = nama;
        this.thumbnail = thumbnail;
        this.nama_menu = nama_menu;
        this.isi = isi;
        this.qty = qty;
        this.id_paket = id_paket;
        this.normal_price = normal_price;
        this.subscribe_price = subscribe_price;
        this.id_menu = id_menu;
        this.ongkos_kirim = ongkos_kirim;
        this.periode = periode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public Integer getId_paket() {
        return id_paket;
    }

    public void setId_paket(Integer id_paket) {
        this.id_paket = id_paket;
    }

    public Integer getNormal_price() {
        return normal_price;
    }

    public void setNormal_price(Integer normal_price) {
        this.normal_price = normal_price;
    }

    public Integer getSubscribe_price() {
        return subscribe_price;
    }

    public void setSubscribe_price(Integer subscribe_price) {
        this.subscribe_price = subscribe_price;
    }

    public Integer getId_menu() {
        return id_menu;
    }

    public void setId_menu(Integer id_menu) {
        this.id_menu = id_menu;
    }

    public Integer getOngkos_kirim() {
        return ongkos_kirim;
    }

    public void setOngkos_kirim(Integer ongkos_kirim) {
        this.ongkos_kirim = ongkos_kirim;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public Integer getPeriode() {
        return periode;
    }

    public void setPeriode(Integer periode) {
        this.periode = periode;
    }
}
