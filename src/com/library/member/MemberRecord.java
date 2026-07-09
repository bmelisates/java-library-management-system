package com.library.member;

import com.library.book.Book;
import com.library.management.Library;
import com.library.person.Reader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class MemberRecord {
    private String memberId;
    private MemberType memberType;
    private LocalDate dateOfMembership;
    private int noBooksIssued;
    private int maxBookLimit;
    private Reader reader;
    private String address;
    private String phoneNo;
    // Üyenin elindeki kitap listesi
    private List<Book> borrowedBooks;

    // 1. CONSTRUCTOR: Yeni temiz üyeler için (Elde kitap yok)
    protected MemberRecord(String memberId, MemberType memberType, int maxBookLimit, Reader reader, String address, String phoneNo) {
        this.memberId = memberId;
        this.memberType = memberType;
        this.maxBookLimit = maxBookLimit;
        this.dateOfMembership = LocalDate.now();
        this.borrowedBooks = new ArrayList<>(); // Boş liste ile başlar
        this.reader = reader;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    // 2. CONSTRUCTOR: Eski sistemden elinde kitaplarla taşınan üyeler için (Migration)
    protected MemberRecord(String memberId, MemberType memberType, int maxBookLimit, Reader reader, String address, String phoneNo, List<Book> migrationBooks) {
        this.memberId = memberId;
        this.memberType = memberType;
        this.maxBookLimit = maxBookLimit;
        this.dateOfMembership = LocalDate.now();

        // Eğer dışarıdan kitap listesi gelmişse onu bağla, gelmemişse boş liste aç
        this.borrowedBooks = (migrationBooks != null) ? new ArrayList<>(migrationBooks) : new ArrayList<>();
        this.reader = reader;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    // Adedi direkt listeden okuyoruz
    public int getNoBooksIssued() {
        return borrowedBooks.size();
    }

    // Ödünç alınan kitabı üye listesine ekleme
    public void addBorrowedBook(Book book, Object requester) {
        if (!(requester instanceof Library)) {
            throw new SecurityException("HATA: Yetkisiz işlem!");
        }
        // Zaten elinde var mı kontrolü (Duplicate Prevention)
        if (this.borrowedBooks.contains(book)) {
            System.out.println("[HATA] : Bu kitap zaten üyede!");
            return;
        }
        // Limit kontrolü
        if (this.borrowedBooks.size() < maxBookLimit) {
            this.borrowedBooks.add(book);
        } else {
            System.out.println("[UYARI] : Limit doldu!");
        }
    }

    // Ödünç alınan kitabı üye listesinden çıkarma
    public void removeBorrowedBook(Book book) {
        if (borrowedBooks.isEmpty()) {
            System.out.println("[UYARI] : " + reader.getName() + " isimli üyenin üzerinde zaten ödünç alınmış hiçbir kitap yok!");
            return;
        }
        boolean isRemoved = this.borrowedBooks.remove(book);
        if (isRemoved) {
            System.out.println("[BAŞARILI] : '" + book.getTitle() + "' kitabı " + reader.getName() + " üzerinden başarıyla düşüldü.");
        } else {
            System.out.println("[HATA] : '" + book.getTitle() + "' isimli kitap " + reader.getName() + " isimli üyenin elinde görünmüyor!");
        }
    }

    public void showBooks() {
        System.out.println("=== " + reader.getName() + " Üzerindeki Kitaplar (" + getNoBooksIssued() + "/" + maxBookLimit + ") ===");
        if (borrowedBooks.isEmpty()) {
            System.out.println("> Üyenin üzerinde şu an hiçbir kitap bulunmamaktadır.");
        } else {
            for (Book book : borrowedBooks) {
                System.out.print("- ");
                book.display();
            }
        }
    }

    //  DELEGE METOT: Üye üzerinden kitap ödünç alma tetikleyicisi
    public void borrowBook(Book book, Library library) {
        // Üye işi kendisi çözmeye çalışmaz, kütüphane sistemine talimat verir
        library.lendBook(this, book);
    }

    // DELEGE METOT: Üye üzerinden kitap iade etme tetikleyicisi
    public void returnBook(Book book, Library library) {
        // Üye "Ben bu kitabı kütüphaneye geri veriyorum" der
        library.takeBackBook(this, book);
    }

    public String getMember() {
        return "Üye ID: " + memberId +
                " | İsim: " + reader.getName() +
                " | Üyelik Tipi: " + memberType +
                " | Alınan Kitap: " + getNoBooksIssued() + "/" + maxBookLimit;
    }


    public String getMemberId() {
        return memberId;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public LocalDate getDateOfMembership() {
        return dateOfMembership;
    }

    public int getMaxBookLimit() {
        return maxBookLimit;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemberRecord that = (MemberRecord) o;
        return Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(memberId);
    }
}
