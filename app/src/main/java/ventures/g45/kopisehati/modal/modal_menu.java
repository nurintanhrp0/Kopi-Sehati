package ventures.g45.kopisehati.modal;

public class modal_menu {
    String nama;
    String thumbnail;
    String harga;
    String id_menu;
    String keterangan;
    String size;
    String id_kategori;
    String id;
    String nama_kategori;
    Integer baris;

    public modal_menu(){}

    public modal_menu(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public modal_menu(Integer baris) {
        this.baris = baris;
    }

    public modal_menu(String nama, String thumbnail, String harga, String id_menu, String keterangan, String size, String id_kategori, String id) {
        this.nama = nama;
        this.thumbnail = thumbnail;
        this.harga = harga;
        this.id_menu = id_menu;
        this.keterangan = keterangan;
        this.size = size;
        this.id_kategori = id_kategori;
        this.id = id;
    }

    public Integer getBaris() {
        return baris;
    }

    public void setBaris(Integer baris) {
        this.baris = baris;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
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

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
