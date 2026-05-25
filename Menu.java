public class Menu {
    private String nama;
    private double harga;
    private String kategori; // "Makanan" atau "Minuman"

    // Konstruktor
    public Menu(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    // Getter dan Setter untuk mendukung manajemen menu (ubah harga, dll)
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    public String getKategori() { return kategori; }
}