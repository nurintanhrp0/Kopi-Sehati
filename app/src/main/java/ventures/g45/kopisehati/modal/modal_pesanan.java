package ventures.g45.kopisehati.modal;

public class modal_pesanan {
  String nama, thumbnail, size, addons, qty;
  String harga, session, keterangan;
  Integer id_size, id_addons, harga_addons, id_menu, harga_size;


  public modal_pesanan(){}

  public modal_pesanan(String nama, String thumbnail, String size, String addons, String qty, String harga, Integer id_size, Integer id_addons, Integer harga_addons, String session, Integer id_menu, String keterangan, Integer harga_size) {
    this.nama = nama;
    this.thumbnail = thumbnail;
    this.size = size;
    this.addons = addons;
    this.qty = qty;
    this.harga = harga;
    this.id_size = id_size;
    this.id_addons = id_addons;
    this.harga_addons = harga_addons;
    this.session = session;
    this.id_menu = id_menu;
    this.keterangan = keterangan;
    this.harga_size = harga_size;
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

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getAddons() {
    return addons;
  }

  public void setAddons(String addons) {
    this.addons = addons;
  }

  public String getQty() {
    return qty;
  }

  public void setQty(String qty) {
    this.qty = qty;
  }

  public String getHarga() {
    return harga;
  }

  public void setHarga(String harga) {
    this.harga = harga;
  }

  public Integer getId_size() {
    return id_size;
  }

  public void setId_size(Integer id_size) {
    this.id_size = id_size;
  }

  public Integer getId_addons() {
    return id_addons;
  }

  public void setId_addons(Integer id_addons) {
    this.id_addons = id_addons;
  }

  public Integer getHarga_addons() {
    return harga_addons;
  }

  public void setHarga_addons(Integer harga_addons) {
    this.harga_addons = harga_addons;
  }

  public String getSession() {
    return session;
  }

  public void setSession(String session) {
    this.session = session;
  }

  public Integer getId_menu() {
    return id_menu;
  }

  public void setId_menu(Integer id_menu) {
    this.id_menu = id_menu;
  }

  public String getKeterangan() {
    return keterangan;
  }

  public void setKeterangan(String keterangan) {
    this.keterangan = keterangan;
  }

  public Integer getHarga_size() {
    return harga_size;
  }

  public void setHarga_size(Integer harga_size) {
    this.harga_size = harga_size;
  }
}
