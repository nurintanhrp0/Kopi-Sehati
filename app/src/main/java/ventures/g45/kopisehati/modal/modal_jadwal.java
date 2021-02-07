package ventures.g45.kopisehati.modal;

import android.content.SharedPreferences;

public class modal_jadwal {
  String id_jadwal;
  String thumbnail, nama, tanggal, jam, cup, status, jadwal;

  public modal_jadwal(){}

  public modal_jadwal(String jadwal) {
    this.jadwal = jadwal;
  }

  public modal_jadwal(String id_jadwal, String thumbnail, String nama, String tanggal, String jam, String cup, String status) {
    this.id_jadwal = id_jadwal;
    this.thumbnail = thumbnail;
    this.nama = nama;
    this.tanggal = tanggal;
    this.jam = jam;
    this.cup = cup;
    this.status = status;
  }

  public String getJadwal() {
    return jadwal;
  }

  public void setJadwal(String jadwal) {
    this.jadwal = jadwal;
  }

  public String getId_jadwal() {
    return id_jadwal;
  }

  public void setId_jadwal(String id_jadwal) {
    this.id_jadwal = id_jadwal;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getNama() {
    return nama;
  }

  public void setNama(String nama) {
    this.nama = nama;
  }

  public String getTanggal() {
    return tanggal;
  }

  public void setTanggal(String tanggal) {
    this.tanggal = tanggal;
  }

  public String getJam() {
    return jam;
  }

  public void setJam(String jam) {
    this.jam = jam;
  }

  public String getCup() {
    return cup;
  }

  public void setCup(String cup) {
    this.cup = cup;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
