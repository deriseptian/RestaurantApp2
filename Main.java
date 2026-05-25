import java.util.Scanner;

public class Main {
    // Array global untuk menampung menu restoran
    private static Menu[] daftarMenu = new Menu[100];
    private static int jumlahMenu = 0;

    // Array untuk menampung keranjang belanja pelanggan saat transaksi berjalan
    private static String[] pesananNama = new String[100];
    private static int[] pesananJumlah = new int[100];
    private static double[] pesananHargaSatuan = new double[100];
    private static int jumlahPesananUnik = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        inisialisasiMenuAwal();

        int pilihanAplikasi;
        // Pengulangan Utama Aplikasi (Sistem Navigasi Parent)
        do {
            tampilkanMenuUtamaAplikasi();
            while (!input.hasNextInt()) {
                System.out.println("Input harus berupa angka murni!");
                System.out.print("Pilih Hak Akses (1-3): ");
                input.next();
            }
            pilihanAplikasi = input.nextInt();
            input.nextLine(); // Pembersihan buffer

            switch (pilihanAplikasi) {
                case 1:
                    jalankanMenuPelanggan(input);
                    break;
                case 2:
                    jalankanMenuPemilik(input);
                    break;
                case 3:
                    System.out.println("Terima kasih! Keluar dari aplikasi UT-Resto.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Silakan input kembali.");
            }
        } while (pilihanAplikasi != 3);
    }

    // ==========================================
    // METHOD-METHOD NAVIGASI & TAMPILAN
    // ==========================================

    private static void inisialisasiMenuAwal() {
        daftarMenu[jumlahMenu++] = new Menu("Nasi Padang", 25000, "Makanan");
        daftarMenu[jumlahMenu++] = new Menu("Ayam Goreng", 20000, "Makanan");
        daftarMenu[jumlahMenu++] = new Menu("Sate Ayam", 22000, "Makanan");
        daftarMenu[jumlahMenu++] = new Menu("Mie Goreng", 15000, "Makanan");

        daftarMenu[jumlahMenu++] = new Menu("Es Teh Manis", 5000, "Minuman");
        daftarMenu[jumlahMenu++] = new Menu("Es Jeruk", 7000, "Minuman");
        daftarMenu[jumlahMenu++] = new Menu("Jus Alpukat", 12000, "Minuman");
        daftarMenu[jumlahMenu++] = new Menu("Kopi Susu", 10000, "Minuman");
    }

    private static void tampilkanMenuUtamaAplikasi() {
        System.out.println("\n=================================");
        System.out.println("     APLIKASI RESTORAN UT-RESTO  ");
        System.out.println("=================================");
        System.out.println("1. Menu Pelanggan (Pemesanan)");
        System.out.println("2. Menu Pemilik (Manajemen Menu)");
        System.out.println("3. Keluar Aplikasi");
        System.out.print("Pilih Hak Akses (1-3): ");
    }

    private static void tampilkanDaftarMenu() {
        System.out.println("\n======= DAFTAR MENU RESTORAN =======");
        System.out.println("[ MAKANAN ]");
        for (int i = 0; i < jumlahMenu; i++) {
            if (daftarMenu[i] != null && daftarMenu[i].getKategori().equalsIgnoreCase("Makanan")) {
                System.out.printf("%d. %-15s - Rp%,.0f\n", (i + 1), daftarMenu[i].getNama(), daftarMenu[i].getHarga());
            }
        }
        System.out.println("\n[ MINUMAN ]");
        for (int i = 0; i < jumlahMenu; i++) {
            if (daftarMenu[i] != null && daftarMenu[i].getKategori().equalsIgnoreCase("Minuman")) {
                System.out.printf("%d. %-15s - Rp%,.0f\n", (i + 1), daftarMenu[i].getNama(), daftarMenu[i].getHarga());
            }
        }
        System.out.println("====================================");
    }

    // ==========================================
    // METHOD UNTUK FITUR PELANGGAN (PEMESANAN)
    // ==========================================

    private static void jalankanMenuPelanggan(Scanner input) {
        jumlahPesananUnik = 0; // Reset keranjang belanja baru
        tampilkanDaftarMenu();

        System.out.println("Format order: Nama Menu = Jumlah (Contoh: Nasi Padang = 2)");
        System.out.println("Ketik 'selesai' untuk mencetak struk.");

        while (true) {
            System.out.print("Masukkan Pesanan: ");
            String inputLine = input.nextLine().trim();

            if (inputLine.equalsIgnoreCase("selesai")) {
                if (jumlahPesananUnik == 0) {
                    System.out.println("Keranjang belanja kosong! Kembali ke menu utama.");
                    return;
                }
                break;
            }

            // Validasi format pemesanan
            if (!inputLine.contains("=")) {
                System.out.println("Format salah! Gunakan tanda '=' (Contoh: Nasi Padang = 2).");
                continue;
            }

            String[] part = inputLine.split("=");
            String namaInput = part[0].trim();
            String jumlahStr = part[1].trim();

            Menu menuDitemukan = cariMenuBerdasarkanNama(namaInput);
            if (menuDitemukan == null) {
                System.out.println("Menu tidak ditemukan! Sistem meminta input kembali.");
                continue;
            }

            int jumlahBeli = parsingJumlahPesanan(jumlahStr);
            if (jumlahBeli <= 0)
                continue; // Jika tidak valid, ulangi input

            // Simpan ke array pesanan
            pesananNama[jumlahPesananUnik] = menuDitemukan.getNama();
            pesananJumlah[jumlahPesananUnik] = jumlahBeli;
            pesananHargaSatuan[jumlahPesananUnik] = menuDitemukan.getHarga();
            jumlahPesananUnik++;
            System.out.println("✓ " + jumlahBeli + " " + menuDitemukan.getNama() + " masuk keranjang.");
        }

        prosesDanCetakStruk();
    }

    private static Menu cariMenuBerdasarkanNama(String nama) {
        for (int i = 0; i < jumlahMenu; i++) {
            if (daftarMenu[i] != null && daftarMenu[i].getNama().equalsIgnoreCase(nama)) {
                return daftarMenu[i];
            }
        }
        return null;
    }

    private static int parsingJumlahPesanan(String jumlahStr) {
        try {
            int jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) {
                System.out.println("Jumlah beli minimal 1! Silakan input kembali.");
                return -1;
            }
            return jumlah;
        } catch (NumberFormatException e) {
            System.out.println("Jumlah harus berupa angka! Silakan input kembali.");
            return -1;
        }
    }

    // ==========================================
    // METHOD KALKULASI BIAYA & STRUK (LOGIKA BARU)
    // ==========================================

    private static void prosesDanCetakStruk() {
        double totalHargaDasar = hitungTotalHargaDasar();
        double diskon = hitungDiskon(totalHargaDasar);

        // Pajak ditanggung dari total harga dasar asli
        double pajak = totalHargaDasar * 0.10;
        double biayaPelayanan = 20000;

        // Logika Promo Beli 1 Gratis 1 Minuman
        String namaMinumanPromo = cariMinumanUntukPromo();
        boolean dapatPromoBogo = (totalHargaDasar > 50000 && !namaMinumanPromo.isEmpty());

        // Perhitungan Total Akhir
        double totalKeseluruhan = totalHargaDasar + pajak + biayaPelayanan;
        double totalAkhirBayar = totalKeseluruhan - diskon;

        // Cetak Output Struk ke Layar
        cetakTeksStruk(totalHargaDasar, pajak, biayaPelayanan, totalKeseluruhan, diskon, totalAkhirBayar,
                dapatPromoBogo, namaMinumanPromo);
    }

    private static double hitungTotalHargaDasar() {
        double total = 0;
        for (int i = 0; i < jumlahPesananUnik; i++) {
            total += (pesananHargaSatuan[i] * pesananJumlah[i]);
        }
        return total;
    }

    private static double hitungDiskon(double totalHargaDasar) {
        // PERUBAHAN: Kondisi diskon dicek berdasarkan nilai asli menu (sebelum pajak &
        // pelayanan)
        if (totalHargaDasar > 100000) {
            return totalHargaDasar * 0.10; // Diskon 10% dari harga dasar menu
        }
        return 0;
    }

    private static String cariMinumanUntukPromo() {
        for (int i = 0; i < jumlahPesananUnik; i++) {
            Menu m = cariMenuBerdasarkanNama(pesananNama[i]);
            if (m != null && m.getKategori().equalsIgnoreCase("Minuman")) {
                return m.getNama();
            }
        }
        return "";
    }

    private static void cetakTeksStruk(double totalDasar, double pajak, double pelayanan, double totalSemua,
            double diskon, double totalAkhir, boolean bogo, String namaMinuman) {
        System.out.println("\n=================================================");
        System.out.println("               STRUK PEMBAYARAN RESMI            ");
        System.out.println("=================================================");
        System.out.printf("%-20s %-5s %-10s %-10s\n", "Item Menu", "Qty", "Harga", "Subtotal");
        System.out.println("-------------------------------------------------");

        for (int i = 0; i < jumlahPesananUnik; i++) {
            double subtotalItem = pesananHargaSatuan[i] * pesananJumlah[i];
            System.out.printf("%-20s %-5d Rp%-9.0f Rp%-10.0f\n", pesananNama[i], pesananJumlah[i],
                    pesananHargaSatuan[i], subtotalItem);
        }

        if (bogo) {
            System.out.printf("%-20s %-5d %-10s %-10s\n", "[FREE] " + namaMinuman, 1, "Rp0", "Rp0 (Promo BOGO)");
        }

        System.out.println("-------------------------------------------------");
        System.out.printf("%-38s : Rp%,.0f\n", "Total Nilai Asli Menu", totalDasar);
        System.out.printf("%-38s : Rp%,.0f\n", "Pajak Restoran (10%)", pajak);
        System.out.printf("%-38s : Rp%,.0f\n", "Biaya Pelayanan", pelayanan);
        System.out.printf("%-38s : Rp%,.0f\n", "Total Keseluruhan (Gross)", totalSemua);
        System.out.println("-------------------------------------------------");

        // Menampilkan Info Promo/Diskon
        if (diskon > 0 || bogo) {
            System.out.println("Informasi Penawaran Terpenuhi:");
            if (diskon > 0) {
                System.out.printf(" - %-35s : -Rp%,.0f\n", "Potongan Diskon 10% (Menu > 100rb)", diskon);
            }
            if (bogo) {
                System.out.println(" - Promo Beli 1 Gratis 1 Minuman Terbaca!");
            }
        } else {
            System.out.println("Informasi Penawaran: Tidak ada promo terpenuhi.");
        }

        System.out.printf("TOTAL AKHIR YANG DIBAYAR               : Rp%,.0f\n", totalAkhir);
        System.out.println("=================================================");
    }

    // ==========================================
    // METHOD PANEL PEMILIK (MANAJEMEN MENU)
    // ==========================================

    private static void jalankanMenuPemilik(Scanner input) {
        int pilihanPemilik;
        do {
            System.out.println("\n=== PANEL PENGELOLAAN MENU (OWNER) ===");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu Restoran");
            System.out.println("4. Lihat Daftar Menu Saat Ini"); // FITUR BARU
            System.out.println("5. Kembali (Parent Menu)");
            System.out.print("Pilih Aksi (1-5): ");

            while (!input.hasNextInt()) {
                System.out.println("Input salah! Masukkan angka.");
                System.out.print("Pilih Aksi (1-5): ");
                input.next();
            }
            pilihanPemilik = input.nextInt();
            input.nextLine(); // Clear buffer

            if (pilihanPemilik == 1) {
                tambahMenuBaru(input);
            } else if (pilihanPemilik == 2) {
                ubahHargaMenu(input);
            } else if (pilihanPemilik == 3) {
                hapusMenuRestoran(input);
            } else if (pilihanPemilik == 4) {
                tampilkanDaftarMenu();
            } else if (pilihanPemilik != 5) {
                System.out.println("Pilihan tidak tersedia! Silakan ulangi.");
            }
        } while (pilihanPemilik != 5);
    }

    private static void tambahMenuBaru(Scanner input) {
        System.out.println("-> Ketik 'batal' pada nama untuk membatalkan dan kembali.");
        System.out.print("Masukkan nama menu baru: ");
        String nama = input.nextLine().trim();

        if (nama.equalsIgnoreCase("batal")) {
            System.out.println("Proses tambah menu dibatalkan. Kembali ke panel pemilik.");
            return;
        }

        System.out.print("Masukkan harga menu: ");
        while (!input.hasNextDouble()) {
            System.out.println("Harga harus berupa angka murni!");
            System.out.print("Masukkan harga menu: ");
            input.next();
        }
        double harga = input.nextDouble();
        input.nextLine();

        String kategori = "";
        while (true) {
            System.out.print("Masukkan kategori (Makanan/Minuman): ");
            kategori = input.nextLine().trim();
            if (kategori.equalsIgnoreCase("Makanan") || kategori.equalsIgnoreCase("Minuman")) {
                break;
            }
            System.out.println("Kategori salah! Ketik 'Makanan' atau 'Minuman'.");
        }

        System.out.print("Apakah Anda yakin ingin menambahkan menu ini? (Ya/Tidak): ");
        if (input.nextLine().trim().equalsIgnoreCase("Ya")) {
            if (jumlahMenu < daftarMenu.length) {
                daftarMenu[jumlahMenu++] = new Menu(nama, harga, kategori);
                System.out.println("✓ Sukses menambah menu baru.");
            } else {
                System.out.println("Penyimpanan array penuh!");
            }
        } else {
            System.out.println("Penambahan dibatalkan.");
        }
    }

    private static void ubahHargaMenu(Scanner input) {
        tampilkanDaftarMenu();
        System.out.println("-> Ketik '0' untuk membatalkan dan kembali.");
        System.out.print("Masukkan Nomor Urut Menu yang ingin diubah: ");
        int nomor = input.nextInt();
        input.nextLine();

        // Fitur Kembali Langsung jika input adalah 0
        if (nomor == 0) {
            System.out.println("Proses ubah harga dibatalkan. Kembali ke panel pemilik.");
            return;
        }

        if (nomor < 1 || nomor > jumlahMenu || daftarMenu[nomor - 1] == null) {
            System.out.println("Nomor menu salah! Kembali ke panel pemilik.");
            return;
        }

        Menu targetMenu = daftarMenu[nomor - 1];
        System.out.print("Masukkan Harga Baru untuk " + targetMenu.getNama() + ": ");
        double hargaBaru = input.nextDouble();
        input.nextLine();

        System.out.print("Konfirmasi perubahan harga? (Ya/Tidak): ");
        if (input.nextLine().trim().equalsIgnoreCase("Ya")) {
            targetMenu.setHarga(hargaBaru);
            System.out.println("✓ Harga berhasil diperbarui.");
        } else {
            System.out.println("Perubahan harga dibatalkan.");
        }
    }

    private static void hapusMenuRestoran(Scanner input) {
        tampilkanDaftarMenu();
        System.out.println("-> Ketik '0' untuk membatalkan dan kembali.");
        System.out.print("Masukkan Nomor Urut Menu yang ingin dihapus: ");
        int nomor = input.nextInt();
        input.nextLine();

        // Fitur Kembali Langsung jika input adalah 0
        if (nomor == 0) {
            System.out.println("Proses hapus menu dibatalkan. Kembali ke panel pemilik.");
            return;
        }

        if (nomor < 1 || nomor > jumlahMenu || daftarMenu[nomor - 1] == null) {
            System.out.println("Nomor menu tidak valid! Kembali ke panel pemilik.");
            return;
        }

        Menu targetMenu = daftarMenu[nomor - 1];
        System.out.print("Yakin ingin MENGHAPUS '" + targetMenu.getNama() + "'? (Ya/Tidak): ");
        if (input.nextLine().trim().equalsIgnoreCase("Ya")) {
            // Geser index array ke depan untuk menghapus item
            for (int i = nomor - 1; i < jumlahMenu - 1; i++) {
                daftarMenu[i] = daftarMenu[i + 1];
            }
            daftarMenu[jumlahMenu - 1] = null;
            jumlahMenu--;
            System.out.println("✓ Menu berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }
}