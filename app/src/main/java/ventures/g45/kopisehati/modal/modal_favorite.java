package ventures.g45.kopisehati.modal;

public class modal_favorite {
    String nama, thumbnail;
    Integer id_menu;

    public modal_favorite(){}

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

    public Integer getId_menu() {
        return id_menu;
    }

    public void setId_menu(Integer id_menu) {
        this.id_menu = id_menu;
    }

    public modal_favorite(String nama, String thumbnail, Integer id_menu) {
        this.nama = nama;
        this.thumbnail = thumbnail;
        this.id_menu = id_menu;
    }
}
