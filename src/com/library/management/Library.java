package com.library.management;

import com.library.book.Book;
import com.library.book.Categorizable;
import com.library.book.Status;
import com.library.member.MemberRecord;
import com.library.person.Author;

import java.util.*;

public class Library implements LibraryOperations {
    private List<Book> inventory;                     //Kütüphanedeki tüm kitapların listesi
    private Map<String, MemberRecord> members;        // Üyeleri id keyi ile tutan Map
    private Map<Book, MemberRecord> borrowedBooksMap; // Kitabın kimde olduğunu tutan Map
    private List<Bill> bills;                         // Faturaların listesi

    // 1. CONSTRUCTOR : Sıfırdan açılan temiz kütüphane
    public Library() {
        this.inventory = new ArrayList<>();
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

        // Veri Göçü (Migration) Otomatik Eşleştirme Motoru
        if (oldMembers != null) {
            for (MemberRecord member : oldMembers.values()) {
                // Kitaplar üyenin kendi constructor'ında zaten List<Book> borrowedBooks içerisine eklendi.
                List<Book> elindekiKitaplar = member.getBorrowedBooks();
                if (elindekiKitaplar != null && !elindekiKitaplar.isEmpty()) {
                    for (Book book : elindekiKitaplar) {
                        this.borrowedBooksMap.put(book, member);
                        book.setStatus(Status.BORROWED);
                    }
                }
            }
        }
    }


    // Kütüphaneye Yeni Kitap Satın Alma / Ekleme
    @Override
    public void purchaseBook(Book book) {
        if (book == null) {
            System.out.println("[UYARI] : Geçersiz veya boş kitap eklenemez!");
            return;
        }
        book.setStatus(Status.AVAILABLE);
        this.inventory.add(book);
        System.out.println("[YENİ KİTAP] : \"" + book.getTitle() + "\" (ID: " + book.getBookId() + ") envantere eklendi.");
    }

    // Kitap Ödünç Verme
    @Override
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

        // Teslimat işlemleri
        book.setStatus(Status.BORROWED);

        // Kitabı üyenin kendi içindeki listeye güvenli metotla ekliyoruz
        member.addBorrowedBook(book, this);
        this.borrowedBooksMap.put(book, member);

        System.out.println("[İŞLEM ONAYLANDI] : " + member.getReader().getName() + " \"" + book.getTitle() + "\" kitabını aldı.");

        // Fatura Kesimi
        String billId = "BLL-" + (bills.size() + 1);
        Bill bill = new Bill(billId, member.getMemberId(), book.getPrice());
        this.bills.add(bill);
        bill.displayBill();
    }

    // Kitap İade Etme
    @Override
    public void takeBackBook(MemberRecord member, Book book) {
        if (member == null || book == null) return;

        if (this.borrowedBooksMap.containsKey(book) && this.borrowedBooksMap.get(book).equals(member)) {
            this.borrowedBooksMap.remove(book);
            // Üyenin listesinden de çıkarıyoruz
            member.removeBorrowedBook(book);
            book.setStatus(Status.AVAILABLE);
            System.out.println("[İADE ALINDI] : \"" + book.getTitle() + "\" teslim alındı.");
        }
    }

    // Üyenin Üzerindeki Kitapları Gösterme
    @Override
    public void showBooksOfMember(MemberRecord member) {
        if (member == null) return;

        // 🔑 DÜZELTME: Map'ten değil, doğrudan üyenin içindeki canlı listeden okuyoruz!
        List<Book> elindekiKitaplar = member.getBorrowedBooks();
        System.out.println("\n📚 " + member.getReader().getName() + " Üzerindeki Kitaplar:");
        if (elindekiKitaplar == null || elindekiKitaplar.isEmpty()) {
            System.out.println("- Ödünç alınmış kitap yok.");
        } else {
            for (Book b : elindekiKitaplar) {
                System.out.println("  > [" + b.getBookId() + "] " + b.getTitle());
            }
        }
    }

    // Gelişmiş Arama Motoru
    public Book searchBook(String query) {
        if (query == null || query.trim().isEmpty()) return null;
        String lowerQuery = query.toLowerCase().trim();

        int searchedId = -1;
        try {
            searchedId = Integer.parseInt(lowerQuery);
        } catch (NumberFormatException e) {
            // Sayı değilse ID araması pas geçilir
        }

        for (Book book : this.inventory) {
            if (searchedId != -1 && book.getBookId() == searchedId) return book;
            if (book.getTitle().toLowerCase().contains(lowerQuery)) return book;

            if (book.getAuthor() != null) {
                for (Author author : book.getAuthor()) {
                    if (author.getName().toLowerCase().contains(lowerQuery)) return book;
                }
            }
        }
        return null;
    }

    // Kitap Güncelleme
    public void updateBook(int bookId, double newPrice, String newEdition) {
        Book book = searchBook(String.valueOf(bookId));
        if (book != null) {
            book.setPrice(newPrice);
            book.setEdition(newEdition);
            System.out.println("[GÜNCELLENDİ] : ID: " + bookId + " olan kitabın bilgileri güncellendi.");
        } else {
            System.out.println("[HATA] : Güncellenecek kitap bulunamadı!");
        }
    }

    //  Kitap Silme
    public void deleteBook(int bookId) {
        Book book = searchBook(String.valueOf(bookId));
        if (book != null) {
            if (book.getStatus() == Status.BORROWED) {
                System.out.println("[HATA] : Ödünçte olan kitap sistemden silinemez!");
                return;
            }
            this.inventory.remove(book);
            System.out.println("[SİLİNDİ] : \"" + book.getTitle() + "\" envanterden kaldırıldı.");
        } else {
            System.out.println("[HATA] : Silinecek kitap bulunamadı!");
        }
    }

    // Kitap silme (nesne ile)
    public void deleteBook(Book book) {
        // Nesnenin envanterde var olduğunu doğrulayalım
        if (!this.inventory.contains(book)) {
            System.out.println("[HATA] : Bu kitap zaten envanterde kayıtlı değil!");
            return;
        }

        // Ödünçte olup olmadığını kontrol et
        if (book.getStatus() == Status.BORROWED) {
            System.out.println("[HATA] : \"" + book.getTitle() + "\" ödünçte olduğu için silinemez!");
            return;
        }

        this.inventory.remove(book);
        System.out.println("[SİLİNDİ] : \"" + book.getTitle() + "\" envanterden kaldırıldı.");
    }

    // Kategoriye Göre Listeleme (Polymorphism)
    // Sadece o sınıfa ait kitapları getirir (örn: listBooksByClass(Novel.class))
    public void listBooksByClass(Class<? extends Book> categoryClass) {
        System.out.println("\n--- " + categoryClass.getSimpleName() + " Türündeki Kitaplar ---");
        boolean found = false;
        for (Book book : this.inventory) {
            if (categoryClass.isInstance(book)) {
                book.display();
                found = true;
            }
        }
        if (!found) System.out.println("Bu türde kitap bulunmuyor.");
    }

    // Alt kategoriye göre listeleme
    public void listBooksBySubCategory(String subCategory) {
        System.out.println("\n--- '" + subCategory + "' Kategorisindeki Tüm Kitaplar ---");
        boolean found = false;

        for (Book book : this.inventory) {
            // Kitabın Categorizable arayüzünü uygulayıp uygulamadığını kontrol ediyoruz
            if (book instanceof Categorizable) {
                Categorizable categorizableBook = (Categorizable) book;

                // Kitabın kategorisini alıp, arananla eşleşiyor mu diye bakıyoruz
                if (categorizableBook.getCategory().equalsIgnoreCase(subCategory)) {
                    book.display();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("'" + subCategory + "' kategorisinde kitap bulunamadı.");
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