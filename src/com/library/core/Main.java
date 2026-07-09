package com.library.core;

import com.library.book.*;
import com.library.management.Librarian;
import com.library.management.Library;
import com.library.member.FacultyMember;
import com.library.member.MemberRecord;
import com.library.member.StudentMember;
import com.library.person.Author;
import com.library.person.Reader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Library library = new Library();
    private static final Scanner scanner = new Scanner(System.in);

    // Oturum (Session) değişkenleri
    private static MemberRecord currentUser = null;
    private static Librarian currentLibrarian = null;

    // Static blok, uygulama ayağa kalktığı anda SADECE BİR KERE çalışır.
    static {
        // 1. Kütüphaneciyi Tanımla
        Librarian admin = new Librarian("admin", "LIB1", "123456", library);

        // 4. Test Üyesi Ekle
        Reader r1 = new Reader("Ahmet Test");
        MemberRecord m1 = new StudentMember(r1, "İzmir", "555-0000");
        library.getMembers().put(m1.getMemberId(), m1);

        // 2. Yazar oluştur
        Author a1 = new Author("George Orwell");

        // 3. Kitapları Kütüphaneye ekle
        library.purchaseBook(new Novel("1984", 200, "1.Baskı", LocalDate.now(), Genre.CLASSIC, a1), admin);
        library.purchaseBook(new Novel("Hayvan Çiftliği", 150, "1.Baskı", LocalDate.now(), Genre.CLASSIC, a1), admin);

        System.out.println("--- Sistem verileri başarıyla yüklendi ---");
    }

    public static void main(String[] args) {
        boolean programRunning = true;

        while (programRunning) {
            // GİRİŞ EKRANI (Sadece kimse giriş yapmamışsa görünür)
            if (currentUser == null && currentLibrarian == null) {
                System.out.println("\n=================================");
                System.out.println("   KÜTÜPHANE YÖNETİM SİSTEMİ");
                System.out.println("=================================");
                System.out.println("1. Üye Girişi");
                System.out.println("2. Kütüphaneci Girişi");
                System.out.println("3. Yeni Üye Kaydı");
                System.out.println("4. Çıkış");
                System.out.print("Seçiminiz: ");
                String secim = scanner.nextLine();

                switch (secim) {
                    case "1" -> memberLogin();
                    case "2" -> librarianLogin();
                    case "3" -> register();
                    case "4" -> programRunning = false;
                    default -> System.out.println("Geçersiz!");
                }
            }
            // İŞLEM MENÜLERİ (Giriş yapıldıktan sonra buralara girer)
            else if (currentUser != null) {
                showMemberMenu(); // Bu metot içinde "Oturumu Kapat" diyene kadar dönecek
            } else if (currentLibrarian != null) {
                showLibrarianMenu(); // Bu metot içinde "Oturumu Kapat" diyene kadar dönecek
            }
        }
    }

    private static void memberLogin() {
        System.out.print("Lütfen Üye ID'nizi giriniz: ");
        String id = scanner.nextLine();

        MemberRecord member = library.getMembers().get(id);
        if (member != null) {
            currentUser = member;
            System.out.println("✅ Hoş geldin, " + currentUser.getReader().getName());
            // Burada üyeye özel işlemler menüsüne yönlendirme yapabilirsin
        } else {
            System.out.println("❌ HATA: Üye bulunamadı!");
        }
    }

    private static void librarianLogin() {
        System.out.print("Kullanıcı Adı: ");
        String user = scanner.nextLine();
        System.out.print("Şifre: ");
        String pass = scanner.nextLine();

        // Basit bir kontrol (Admin nesnesinin şifresini biliyoruz)
        if (user.equals("admin") && pass.equals("123456")) {
            currentLibrarian = new Librarian(user, "LIB1", pass, library);
            System.out.println("✅ Sayın Kütüphaneci, sistem yetkileri aktif.");
        } else {
            System.out.println("❌ HATA: Hatalı yetkili bilgileri!");
        }
    }

    private static void register() {
        System.out.println("--- ÜYELİK TÜRÜ SEÇİMİ ---");
        System.out.println("1. Öğrenci (Student)");
        System.out.println("2. Akademisyen (Faculty)");
        System.out.print("Seçiminiz (1/2): ");
        String tip = scanner.nextLine();

        System.out.print("İsim Soyisim: ");
        String name = scanner.nextLine();
        System.out.print("Adres: ");
        String adres = scanner.nextLine();
        System.out.print("Telefon: ");
        String tel = scanner.nextLine();

        Reader reader = new Reader(name);
        MemberRecord newMember = null;

        if (tip.equals("1")) {
            // Öğrenci ise StudentMember oluştur
            newMember = new StudentMember(reader, adres, tel);
        } else if (tip.equals("2")) {
            // Akademisyen ise FacultyMember oluştur
            newMember = new FacultyMember(reader, adres, tel);
        } else {
            System.out.println("❌ Geçersiz seçim, kayıt iptal edildi.");
            return;
        }

        // Sisteme ekle
        library.getMembers().put(newMember.getMemberId(), newMember);
        System.out.println("✅ Kayıt başarılı! Üye ID'niz: " + newMember.getMemberId());
        System.out.println("   (Bu ID ile giriş yapabilirsiniz.)");
    }

    private static void showMemberMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- ÜYE İŞLEM MENÜSÜ ---");
            System.out.println("1. Kitap ödünç al");
            System.out.println("2. Kitabı geri ver");
            System.out.println("3. Elimdeki kitapları listele");
            System.out.println("4. Kütüphanedeki kitapları listele");
            System.out.println("5. Çıkış");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> {
                    System.out.print("Ödünç almak istediğiniz kitabın ismini girin: ");

                    String name = scanner.nextLine();
                    Book b = library.searchBook(name); // String'i Book'a çeviriyoruz
                    if (b != null) {
                        library.lendBook(currentUser, b);
                    } else {
                        System.out.println("❌ Kitap bulunamadı!");
                    }
                }
                case "2" -> {
                    System.out.print("İade etmek istediğiniz kitabın ID'sini girin: ");
                    String id = scanner.nextLine();
                    Book b = library.searchBook(id);
                    if (b != null) {
                        library.takeBackBook(currentUser, b);
                    } else {
                        System.out.println("❌ Kitap bulunamadı!");
                    }
                }
                case "3" -> {
                    library.showBooksOfMember(currentUser);
                }
                case "4" -> {
                    System.out.println("\n--- MEVCUT KİTAPLAR ---");
                    for (Book b : library.getInventory()) {
                        b.display(); // Kitap sınıfındaki display metodunu kullandığını varsayıyorum
                    }
                }
                case "5" -> {
                    currentUser = null;
                    inMenu = false;
                    System.out.println("Oturum kapatıldı.");
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void showLibrarianMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- KÜTÜPHANECİ YÖNETİM PANELİ ---");
            System.out.println("1. Kitap Ekle\n2. Sil\n3. Güncelle\n4. Listele\n5. Çıkış");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();
            switch (secim) {
                case "1" -> handleAddBook();
                case "2" -> handleDeleteBook();
                case "3" -> handleUpdateBook();
                case "4" -> library.getInventory().forEach(Book::display);
                case "5" -> {
                    inMenu = false;
                    currentLibrarian = null;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void handleAddBook() {
        System.out.print("Kitap Adı: ");
        String title = scanner.nextLine();
        System.out.print("Fiyat: ");
        double price = Double.parseDouble(scanner.nextLine());

        // 1. Yazar Toplama
        List<Author> yazarListesi = new ArrayList<>();
        boolean dahaEkle = true;
        while (dahaEkle) {
            System.out.print("Yazar Adı: ");
            String authorName = scanner.nextLine();

            Author yazar = null;
            for (Book b : library.getInventory()) {
                for (Author a : b.getAuthor()) {
                    if (a.getName().equalsIgnoreCase(authorName)) {
                        yazar = a;
                        break;
                    }
                }
            }
            if (yazar == null) yazar = new Author(authorName);
            yazarListesi.add(yazar);

            System.out.print("Başka yazar eklemek ister misiniz? (E/H): ");
            dahaEkle = scanner.nextLine().equalsIgnoreCase("E");
        }
        Author[] yazarDizisi = yazarListesi.toArray(new Author[0]); //Yazarları diziye çevirdik

        // Edition Bilgisi
        System.out.print("Baskı: ");
        String edition = scanner.nextLine();

        // 2. Kategori ve Nesne Oluşturma
        System.out.println("Kategori: 1.Roman | 2.Akademik | 3.Ders | 4.Dergi");
        String cat = scanner.nextLine();

        Book newBook = null;

        switch (cat) {
            case "1" -> {
                for (Genre g : Genre.values()) System.out.println(g.ordinal() + ":" + g);
                System.out.print("Tür no: ");
                newBook = new Novel(title, price, edition, LocalDate.now(), Genre.values()[Integer.parseInt(scanner.nextLine())], yazarDizisi);
            }
            case "2" -> {
                for (FieldOfStudy f : FieldOfStudy.values()) System.out.println(f.ordinal() + ":" + f);
                System.out.print("Alan no: ");
                int fIdx = Integer.parseInt(scanner.nextLine());
                System.out.print("Yayınevi: ");
                String pub = scanner.nextLine();
                newBook = new Journal(title, price, edition, LocalDate.now(), FieldOfStudy.values()[fIdx], pub, yazarDizisi);
            }
            case "3" -> {
                for (SubjectType s : SubjectType.values()) System.out.println(s.ordinal() + ":" + s);
                System.out.print("Konu no: ");
                newBook = new StudyBook(title, price, edition, LocalDate.now(), SubjectType.values()[Integer.parseInt(scanner.nextLine())], yazarDizisi);
            }
            case "4" -> {
                for (MagazineType m : MagazineType.values()) System.out.println(m.ordinal() + ":" + m);
                System.out.print("Dergi no: ");
                newBook = new Magazine(title, price, edition, LocalDate.now(), MagazineType.values()[Integer.parseInt(scanner.nextLine())], yazarDizisi);
            }
        }

        if (newBook != null) {
            library.purchaseBook(newBook, currentLibrarian);
        }
    }


    private static void handleDeleteBook() {
        System.out.print("Silinecek Kitap ID: ");
        library.deleteBook(Integer.parseInt(scanner.nextLine()), currentLibrarian);
    }

    private static void handleUpdateBook() {
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Yeni Fiyat: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Yeni Baskı: ");
        String edition = scanner.nextLine();
        library.updateBook(id, price, edition, currentLibrarian);
    }
}
