package com.library.management;

import com.library.book.Book;
import com.library.book.Status;
import com.library.member.MemberRecord;
import com.library.person.Author;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Library {
    private List<Book> inventory;                     // Kütüphanedeki tüm kitapların listesi (Ekleme sırasını koruması için List seçtim)
    private Map<Long, Book> inventoryById = new HashMap<>(); // Arama hızını artırmak için
    private Map<String, MemberRecord> members;        // Üyeleri id keyi ile tutan Map
    private Map<Book, MemberRecord> borrowedBooksMap; // Kitapların kimde olduğunu tutan Map
    private List<Bill> bills;                         // Faturaların listesi

    // 1. CONSTRUCTOR : Sıfırdan açılan temiz kütüphane
    public Library() {
        this.inventory = new ArrayList<>();
        this.inventoryById = new HashMap<>();
        this.members = new HashMap<>();
        this.borrowedBooksMap = new HashMap<>();
        this.bills = new ArrayList<>();
    }

    // 2. CONSTRUCTOR : Eski sistem verileriyle (Migration) açılan kütüphane
    public Library(List<Book> oldInventory, Map<String, MemberRecord> oldMembers) {
        this.inventory = (oldInventory != null) ? new ArrayList<>(oldInventory) : new ArrayList<>();
        this.members = (oldMembers != null) ? new HashMap<>(oldMembers) : new HashMap<>();
        this.borrowedBooksMap = new HashMap<>();
        this.bills = new ArrayList<>();

        this.inventoryById = new HashMap<>();
        for (Book book : this.inventory) {
            this.inventoryById.put(book.getBookId(), book);
        }

        // Veri Göçü (Migration) Otomatik Eşleştirme Motoru
        if (oldMembers != null) {
            for (MemberRecord member : oldMembers.values()) {
                // Kitaplar üyenin kendi constructor'ında zaten Set<Book> borrowedBooks içerisine eklendi.
                Set<Book> elindekiKitaplar = member.getBorrowedBooks();
                if (elindekiKitaplar != null && !elindekiKitaplar.isEmpty()) {
                    for (Book book : elindekiKitaplar) {
                        this.borrowedBooksMap.put(book, member);
                        book.setStatus(Status.BORROWED);
                    }
                }
            }
        }
    }


    // Kitabı envantere ekle - protected -> başka paketten erişilemesin (librarianda çağırıyoruz)
    protected void addBook(Book book) {
        if (book == null) {
            System.out.println("[UYARI] : Geçersiz veya boş kitap eklenemez!");
            return;
        }

        // Eğer kitap sahipli değilse (kütüphanede duran kitap) AVAILABLE yap
        if (book.getOwner() == null) {
            book.setStatus(Status.AVAILABLE);
        } else {
            // Sahipli kitapları borrowedBooksMap'e ekle
            this.borrowedBooksMap.put(book, book.getOwner());
            if (book.getStatus() == null) {
                book.setStatus(Status.BORROWED);
            }
        }

        this.inventory.add(book);
        this.inventoryById.put(book.getBookId(), book);
        System.out.println("[YENİ KİTAP] : \"" + book.getTitle() + "\" (ID: " + book.getBookId() + ") envantere eklendi.");
    }

    // Kitap ödünç verme
    public void lendBook(MemberRecord member, Book book) {
        if (member == null || book == null) {
            System.out.println("[HATA] : Üye veya kitap bilgisi boş olamaz!");
            return;
        }
        if (book.getStatus() != Status.AVAILABLE) {
            System.out.println("[HATA] : \"" + book.getTitle() + "\" şu an rafta değil! Durumu: " + book.getStatus());
            return;
        }
        // Canlı liste boyutu kontrolü
        if (member.getNoBooksIssued() >= member.getMaxBookLimit()) {
            System.out.println("[HATA] : " + member.getReader().getName() + " maksimum limitine (" + member.getMaxBookLimit() + ") ulaştı!");
            return;
        }

        if (!members.containsValue(member) || !inventory.contains(book)) {
            System.out.println("[HATA] : Üye ve kitap kütüphanede kayıtlı olmalıdır!");
            return;
        }

        // Teslimat işlemleri
        book.setStatus(Status.BORROWED);
        book.setOwner(member);
        book.setBorrowDate(LocalDate.now());

        // Kitabı üyenin kendi içindeki listeye güvenli metotla ekliyoruz
        member.addBorrowedBook(book);
        this.borrowedBooksMap.put(book, member);

        System.out.println("[İŞLEM ONAYLANDI] : " + member.getReader().getName() + ", " + book.getBookId() + " id no'lu " + " \"" + book.getTitle() + "\" kitabını aldı.");

        // Depozito Kesimi (10 TL)
        String billId = "BLL-" + (bills.size() + 1);
        Bill depositBill = new Bill(billId, member.getMemberId(), 10.0, BillType.DEPOSIT);
        this.bills.add(depositBill);
        depositBill.displayBill();
    }

    // Kitap iade alma (temel operasyon - iş mantığı Librarian'da)
    public boolean takeBackBook(MemberRecord member, Book book) {
        if (member == null || book == null) return false;

        if (this.borrowedBooksMap.containsKey(book) && this.borrowedBooksMap.get(book).equals(member)) {
            this.borrowedBooksMap.remove(book);
            member.removeBorrowedBook(book);
            book.setStatus(Status.AVAILABLE);
            book.setOwner(null);
            System.out.println("[İADE ALINDI] : \"" + book.getTitle() + "\" teslim alındı.");

            // Depozito İade Faturası (10 TL)
            String billId = "BLL-" + (bills.size() + 1);
            Bill depositReturnBill = new Bill(billId, member.getMemberId(), 10.0, BillType.DEPOSIT_RETURN);
            this.bills.add(depositReturnBill);
            depositReturnBill.displayBill();

            // Gecikme Cezası Hesaplama (14 gün sonrası her gün 5 TL)
            LocalDate borrowDate = book.getBorrowDate();
            if (borrowDate != null) {
                long daysOverdue = ChronoUnit.DAYS.between(borrowDate, LocalDate.now());
                if (daysOverdue > 14) {
                    double lateFee = (daysOverdue - 14) * 5.0;
                    String lateBillId = "BLL-" + (bills.size() + 1);
                    Bill lateBill = new Bill(lateBillId, member.getMemberId(), lateFee, BillType.LENDING);
                    this.bills.add(lateBill);
                    System.out.println("[CEZA] : " + (daysOverdue - 14) + " gün gecikmeden " + lateFee + " TL ceza kesildi.");
                    lateBill.displayBill();
                }
            }

            book.setBorrowDate(null);
            return true;
        }
        return false;
    }


    // Gelişmiş Arama Motoru
    public Book searchBook(String query) {
        if (query == null || query.trim().isEmpty()) return null;
        String lowerQuery = query.toLowerCase(Locale.ROOT).trim();

        long searchedId = -1;
        try {
            searchedId = Long.parseLong(lowerQuery);
            Book exactMatch = inventoryById.get(searchedId);
            if (exactMatch != null) return exactMatch;
        } catch (NumberFormatException e) {
            // Sayı değilse ID araması pas geçilir
        }

        for (Book book : this.inventory) {
            if (searchedId != -1 && book.getBookId() == searchedId) return book;
            if (book.getTitle().toLowerCase(Locale.ROOT).contains(lowerQuery)) return book;

            if (book.getAuthor() != null) {
                for (Author author : book.getAuthor()) {
                    if (author.getName().toLowerCase(Locale.ROOT).contains(lowerQuery)) return book;
                }
            }
        }
        return null;
    }

    // Kitap güncelleme - protected -> başka paketten erişilemesin (librarianda çağırıyoruz)
    protected void updateBook(int bookId, double newPrice, String newEdition) {
        Book book = inventoryById.get((long) bookId);
        if (book != null) {
            book.setPrice(newPrice);
            book.setEdition(newEdition);
            System.out.println("[GÜNCELLENDİ] : ID: " + bookId + " olan kitabın bilgileri güncellendi.");
        } else {
            System.out.println("[HATA] : Güncellenecek kitap bulunamadı!");
        }
    }

    // Kitap silme(id ile) - protected -> başka paketten erişilemesin (librarianda çağırıyoruz)
    protected void deleteBook(int bookId) {
        Book book = inventoryById.get((long) bookId);
        if (book != null) {
            if (book.getStatus() == Status.BORROWED) {
                System.out.println("[HATA] : Ödünçte olan kitap sistemden silinemez!");
                return;
            }
            this.inventory.remove(book);
            this.inventoryById.remove(book.getBookId());
            System.out.println("[SİLİNDİ] : \"" + book.getTitle() + "\" envanterden kaldırıldı.");
        } else {
            System.out.println("[HATA] : Silinecek kitap bulunamadı!");
        }
    }

    // Kitap silme (nesne ile) - protected -> başka paketten erişilemesin (librarianda çağırıyoruz)
    protected void deleteBook(Book book) {
        if (!this.inventory.contains(book)) {
            System.out.println("[HATA] : Bu kitap zaten envanterde kayıtlı değil!");
            return;
        }

        if (book.getStatus() == Status.BORROWED) {
            System.out.println("[HATA] : \"" + book.getTitle() + "\" ödünçte olduğu için silinemez!");
            return;
        }

        this.inventory.remove(book);
        this.inventoryById.remove(book.getBookId());
        System.out.println("[SİLİNDİ] : \"" + book.getTitle() + "\" envanterden kaldırıldı.");
    }

    // Kitap türüne göre listeleme (Novel, Journal, StudyBook, Magazine)
    public void listBooksByType(Class<? extends Book> bookType) {
        System.out.println("\n--- " + bookType.getSimpleName() + " Türündeki Kitaplar ---");
        boolean found = false;

        for (Book book : this.inventory) {
            if (bookType.isInstance(book)) {
                book.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println(bookType.getSimpleName() + " türünde kitap bulunamadı.");
        }
    }


    // Yazara Göre Kitap Listeleme
    public void listBooksByAuthor(String authorName) {
        System.out.println("\n--- " + authorName + " Tarafından Yazılan Kitaplar ---");
        boolean found = false;
        for (Book book : this.inventory) {
            if (book.getAuthor() != null) {
                for (Author auth : book.getAuthor()) {
                    if (auth.getName().toLowerCase().contains(authorName.toLowerCase())) {
                        book.display();
                        found = true;
                    }
                }
            }
        }
        if (!found) System.out.println("Yazara ait kitap bulunamadı.");
    }

    //  Benzersiz Tüm Yazarları Listeleme
    public void showAllUniqueAuthors() {
        Set<String> uniqueAuthors = new HashSet<>();
        for (Book book : this.inventory) {
            if (book.getAuthor() != null) {
                for (Author auth : book.getAuthor()) {
                    uniqueAuthors.add(auth.getName());
                }
            }
        }
        System.out.println("\n👤 KÜTÜPHANEDEKİ BENZERSİZ YAZARLAR:");
        for (String name : uniqueAuthors) {
            System.out.println("- " + name);
        }
    }

    // 4. GETTER & SETTER METOTLARI
    public List<Book> getInventory() {
        return inventory;
    }

    public Map<String, MemberRecord> getMembers() {
        return members;
    }

    public List<Bill> getBills() {
        return bills;
    }
}
