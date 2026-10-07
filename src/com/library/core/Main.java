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
    private static final Librarian adminLibrarian; // Global kütüphaneci (bir kere oluşturulur)

    // Oturum (Session) değişkenleri
    private static MemberRecord currentUser = null;
    private static Librarian currentLibrarian = null;

    // Static blok, uygulama ayağa kalktığı anda SADECE BİR KERE çalışır.
    static {
        // Admin kütüphanecisini oluştur ve sakla (sonsuza kadar)
        adminLibrarian = new Librarian("admin", "LIB1", "123456", library);

        // Test Üyesi Ekle
        Reader r1 = new Reader("Ahmet Test");
        MemberRecord m1 = new StudentMember(r1, "İzmir", "555-0000");
        library.getMembers().put(m1.getMemberId(), m1);

        // Yazar oluştur
        Author a1 = new Author("George Orwell");
        Author a2 = new Author("Dostoyevski");

        // Kitapları Kütüphaneye ekle
        adminLibrarian.purchaseBook(new Novel("1984", 200, "1.Baskı", LocalDate.now(), m1, Genre.CLASSIC, a1));
        adminLibrarian.purchaseBook(new Novel("Hayvan Çiftliği", 150, "1.Baskı", LocalDate.now(), m1, Genre.CLASSIC, a1));
        adminLibrarian.purchaseBook(new Novel("Suç ve Ceza", 150, "1.Baskı", LocalDate.now(), m1, Genre.CLASSIC, a2));
        adminLibrarian.purchaseBook(new Novel("Karamazov Kardeşler", 150, "1.Baskı", LocalDate.now(), m1, Genre.CLASSIC, a2));
        adminLibrarian.purchaseBook(new Novel("Kumarbaz", 150, "1.Baskı", LocalDate.now(), m1, Genre.CLASSIC, a2));
        adminLibrarian.purchaseBook(new StudyBook("Borçlar Hukuku", 600, "3.Baskı", LocalDate.now(), SubjectType.LAW, new Author("Kemal Oğuzman"), new Author("Samet Öz")));

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

        // Şifre kontrolü yap ve doğruysa önceden oluşturulmuş admin nesnesini kullan
        if (user.equals("admin") && pass.equals("123456")) {
            currentLibrarian = adminLibrarian; // Aynı nesneyi kullan (yeniden oluşturma yok!)
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
            System.out.println("4. Kütüphanedeki tüm kitapları listele");
            System.out.println("5. Yazara göre kitapları listele");
            System.out.println("6. Türe göre kitapları listele");
            System.out.println("7. Kitap ara");
            System.out.println("8. Çıkış");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            switch (secim) {
                case "1" -> {
                    System.out.print("Ödünç almak istediğiniz kitabın ismini girin: ");

                    String name = scanner.nextLine();
                    Book b = library.searchBook(name);
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
                        if (!currentUser.getBorrowedBooks().contains(b)) {
                            System.out.println("❌ HATA: Bu kitap sizin elinizdeki ödünç kitaplar arasında değil!");
                        } else {
                            boolean success = library.takeBackBook(currentUser, b);
                            if (success) {
                                System.out.println("✅ Kitap başarıyla iade edildi. Depozitonuz (10 TL) geri alındı.");
                            } else {
                                System.out.println("❌ HATA: Kitap iade işlemi başarısız oldu!");
                            }
                        }
                    } else {
                        System.out.println("❌ Kitap bulunamadı!");
                    }
                }
                case "3" -> {
                    currentUser.showBooks();
                }
                case "4" -> {
                    System.out.println("\n--- MEVCUT KİTAPLAR ---");
                    for (Book b : library.getInventory()) {
                        b.display();
                    }
                }
                case "5" -> {
                    System.out.print("Yazarın adını girin: ");
                    String authorName = scanner.nextLine();
                    library.listBooksByAuthor(authorName);
                }
                case "6" -> {
                    showBookTypeMenu();
                }
                case "7" -> {
                    handleSearchBook();
                }
                case "8" -> {
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
            System.out.println("1. Kitap Ekle\n2. Sil\n3. Güncelle\n4. Tüm Kitapları Listele");
            System.out.println("5. Üyeye Ödünç Ver\n6. Kitap Ara\n7. Yazara Göre Kitapları Listele");
            System.out.println("8. Türe Göre Kitapları Listele\n9. Tüm Yazarları Listele\n10. Çıkış");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();
            switch (secim) {
                case "1" -> handleAddBook();
                case "2" -> handleDeleteBook();
                case "3" -> handleUpdateBook();
                case "4" -> library.getInventory().forEach(Book::display);
                case "5" -> handleLendBookByLibrarian();
                case "6" -> handleSearchBook();
                case "7" -> {
                    System.out.print("Yazarın adını girin: ");
                    String authorName = scanner.nextLine();
                    library.listBooksByAuthor(authorName);
                }
                case "8" -> {
                    showBookTypeMenu();
                }
                case "9" -> library.showAllUniqueAuthors();
                case "10" -> {
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
        if (title.trim().isEmpty()) {
            System.out.println("❌ HATA: Kitap adı boş olamaz!");
            return;
        }

        double price = readDoubleInput("Fiyat: ");
        if (price < 0) {
            System.out.println("❌ HATA: Fiyat negatif olamaz!");
            return;
        }

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
                Genre genre = readEnumInput("Tür no: ", Genre.values());
                newBook = new Novel(title, price, edition, LocalDate.now(), genre, yazarDizisi);
            }
            case "2" -> {
                for (FieldOfStudy f : FieldOfStudy.values()) System.out.println(f.ordinal() + ":" + f);
                FieldOfStudy field = readEnumInput("Alan no: ", FieldOfStudy.values());
                System.out.print("Yayınevi: ");
                String pub = scanner.nextLine();
                newBook = new Journal(title, price, edition, LocalDate.now(), field, pub, yazarDizisi);
            }
            case "3" -> {
                for (SubjectType s : SubjectType.values()) System.out.println(s.ordinal() + ":" + s);
                SubjectType subject = readEnumInput("Konu no: ", SubjectType.values());
                newBook = new StudyBook(title, price, edition, LocalDate.now(), subject, yazarDizisi);
            }
            case "4" -> {
                for (MagazineType m : MagazineType.values()) System.out.println(m.ordinal() + ":" + m);
                MagazineType type = readEnumInput("Dergi no: ", MagazineType.values());
                newBook = new Magazine(title, price, edition, LocalDate.now(), type, yazarDizisi);
            }
        }

        if (newBook == null) {
            System.out.println("Geçersiz kategori, kitap eklenmedi.");
        }
        if (newBook != null) {
            currentLibrarian.purchaseBook(newBook);
        }
    }

    private static void handleDeleteBook() {
        int id = readIntegerInput("Silinecek Kitap ID: ");
        if (id > 0) {
            currentLibrarian.deleteBook(id);
        } else {
            System.out.println("❌ HATA: Geçersiz kitap ID!");
        }
    }

    private static void handleUpdateBook() {
        int id = readIntegerInput("ID: ");
        if (id <= 0) {
            System.out.println("❌ HATA: Geçersiz kitap ID!");
            return;
        }

        double price = readDoubleInput("Yeni Fiyat: ");
        if (price < 0) {
            System.out.println("❌ HATA: Fiyat negatif olamaz!");
            return;
        }

        System.out.print("Yeni Baskı: ");
        String edition = scanner.nextLine();
        if (edition.trim().isEmpty()) {
            System.out.println("❌ HATA: Baskı bilgisi boş olamaz!");
            return;
        }

        currentLibrarian.updateBook(id, price, edition);
    }

    private static void handleLendBookByLibrarian() {
        System.out.print("Üye ID'sini girin: ");
        String memberId = scanner.nextLine();

        if (!currentLibrarian.verifyMember(memberId)) {
            System.out.println("❌ HATA: Bu üye sistemde kayıtlı değil!");
            return;
        }

        MemberRecord member = library.getMembers().get(memberId);
        System.out.print("Kitap id girin: ");
        String bookId = scanner.nextLine();
        Book book = library.searchBook(bookId);

        if (book == null) {
            System.out.println("❌ HATA: Kitap bulunamadı!");
            return;
        }

        currentLibrarian.issueBook(member, book);
    }

    private static void handleSearchBook() {
        System.out.print("Aradığınız kitabın adını veya ID'sini girin: ");
        String query = scanner.nextLine();

        Book book = library.searchBook(query);
        if (book != null) {
            System.out.println("\n--- ARAMA SONUCU ---");
            book.display();
        } else {
            System.out.println("❌ HATA: Kitap bulunamadı!");
        }
    }

    private static void showBookTypeMenu() {
        System.out.println("\n--- KİTAP TÜRÜ SEÇİMİ ---");
        System.out.println("1. Roman (Novel)");
        System.out.println("2. Akademik Dergi (Journal)");
        System.out.println("3. Ders Kitabı (StudyBook)");
        System.out.println("4. Dergi (Magazine)");
        System.out.print("Seçiminiz (1-4): ");

        String secim = scanner.nextLine();
        switch (secim) {
            case "1" -> library.listBooksByType(Novel.class);
            case "2" -> library.listBooksByType(Journal.class);
            case "3" -> library.listBooksByType(StudyBook.class);
            case "4" -> library.listBooksByType(Magazine.class);
            default -> System.out.println("❌ Geçersiz seçim!");
        }
    }

    // ===== INPUT VALIDATION HELPER METODLAR =====

    private static double readDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (Double.isFinite(value)) return value;
                System.out.println("Lütfen sonlu bir sayı girin!");
            } catch (NumberFormatException e) {
                System.out.println("❌ HATA: Lütfen geçerli bir sayı girin!");
            }
        }
    }

    private static <E extends Enum<E>> E readEnumInput(String prompt, E[] values) {
        while (true) {
            int index = readIntegerInput(prompt);
            if (index >= 0 && index < values.length) return values[index];
            System.out.println("Lütfen 0 ile " + (values.length - 1) + " arasında bir seçim yapın!");
        }
    }

    private static int readIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ HATA: Lütfen geçerli bir tam sayı girin!");
            }
        }
    }
}
