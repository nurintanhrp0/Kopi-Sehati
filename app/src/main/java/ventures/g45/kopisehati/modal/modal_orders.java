package ventures.g45.kopisehati.modal;

public class modal_orders {
  String id_orderan, waktu, status_orderan, thumbnail, status_pembayaran;
  Integer id_paket;

  public modal_orders(){

  }

  public Integer getId_paket() {
    return id_paket;
  }

  public void setId_paket(Integer id_paket) {
    this.id_paket = id_paket;
  }

  public String getStatus_pembayaran() {
    return status_pembayaran;
  }

  public void setStatus_pembayaran(String status_pembayaran) {
    this.status_pembayaran = status_pembayaran;
  }

  public String getId_orderan() {
    return id_orderan;
  }

  public void setId_orderan(String id_orderan) {
    this.id_orderan = id_orderan;
  }

  public String getWaktu() {
    return waktu;
  }

  public void setWaktu(String waktu) {
    this.waktu = waktu;
  }

  public String getStatus_orderan() {
    return status_orderan;
  }

  public void setStatus_orderan(String status_orderan) {
    this.status_orderan = status_orderan;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public modal_orders(String id_orderan, String waktu, String status_orderan, String thumbnail) {
    this.id_orderan = id_orderan;
    this.waktu = waktu;
    this.status_orderan = status_orderan;
    this.thumbnail = thumbnail;
  }

  public modal_orders(String status_pembayaran) {
    this.status_pembayaran = status_pembayaran;
  }

  public modal_orders(Integer id_paket) {
    this.id_paket = id_paket;
  }
}
