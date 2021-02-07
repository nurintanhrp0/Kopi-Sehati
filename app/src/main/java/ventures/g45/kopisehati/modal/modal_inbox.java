package ventures.g45.kopisehati.modal;

public class modal_inbox {
    String judul, isi, thumbnail, keterangan, jenis, jenis_promo;
    Integer nilai, maks, min, pemakaian, jumlah, id;

    public modal_inbox(){}

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJenis_promo() {
        return jenis_promo;
    }

    public void setJenis_promo(String jenis_promo) {
        this.jenis_promo = jenis_promo;
    }

    public Integer getNilai() {
        return nilai;
    }

    public void setNilai(Integer nilai) {
        this.nilai = nilai;
    }

    public Integer getMaks() {
        return maks;
    }

    public void setMaks(Integer maks) {
        this.maks = maks;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getPemakaian() {
        return pemakaian;
    }

    public void setPemakaian(Integer pemakaian) {
        this.pemakaian = pemakaian;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public modal_inbox(String judul, String isi, String thumbnail, String keterangan, String jenis, String jenis_promo, Integer nilai, Integer maks, Integer min, Integer pemakaian, Integer jumlah, Integer id) {
        this.judul = judul;
        this.isi = isi;
        this.thumbnail = thumbnail;
        this.keterangan =keterangan;
        this.jenis = jenis;
        this.jenis_promo = jenis_promo;
        this.nilai = nilai;
        this.maks = maks;
        this.min = min;
        this.pemakaian = pemakaian;
        this.jumlah = jumlah;
        this.id = id;
    }
}
