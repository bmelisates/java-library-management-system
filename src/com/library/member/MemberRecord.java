package com.library.member;

import com.library.book.Book;
import com.library.person.Reader;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class MemberRecord {
    private final String memberId;
    private final MemberType memberType;
    private final LocalDate dateOfMembership;
    private final int maxBookLimit;
    private Reader reader;
    private String address;
    private String phoneNo;
    // Üyenin elindeki kitap listesi
    private Set<Book> borrowedBooks; // Aynı kitabı iki defa ödünç alamaz (id'sine göre hash ve equals override ettim)

    // 1. CONSTRUCTOR: Yeni üyeler için (Elde kitap yok)
    protected MemberRecord(String memberId, MemberType memberType, int maxBookLimit, Reader reader, String address, String phoneNo) {
        this.memberId = memberId;
        this.memberType = memberType;
        this.maxBookLimit = maxBookLimit;
        this.dateOfMembership = LocalDate.now();
        this.borrowedBooks = new HashSet<>(); // Boş küme ile başlar
        this.reader = reader;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    // 2. CONSTRUCTOR: Eski sistemden elinde kitaplarla taşınan üyeler için (Migration)
    protected MemberRecord(String memberId, MemberType memberType, int maxBookLimit, Reader reader, String address, String phoneNo, Set<Book> migrationBooks) {
        this.memberId = memberId;
        this.memberType = memberType;
        this.maxBookLimit = maxBookLimit;
        this.dateOfMembership = LocalDate.now();

        // Eğer dışarıdan kitap listesi gelmişse onu bağla, gelmemişse boş liste aç
        this.borrowedBooks = (migrationBooks != null) ? new HashSet<>(migrationBooks) : new HashSet<>();
        this.reader = reader;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    // Adedi direkt listeden okuyoruz
    public int getNoBooksIssued() {
        return borrowedBooks.size();
    }

    // Ödünç alınan kitabı üye listesine ekleyen metot
    public void addBorrowedBook(Book book) {
        // 1. Adım: Kitap zaten elinde mi? (Listede varsa hata ver)
        if (this.borrowedBooks.contains(book)) {
            System.out.println("[HATA] : Bu kitap zaten üyede!");
            return;
        }

        // 2. Adım: Limit kontrolü yap
        if (this.borrowedBooks.size() >= maxBookLimit) {
            System.out.println("[UYARI] : Limit doldu!");
            return;
        }

        // 3. Adım: Her şey yolundaysa ekle
        this.borrowedBooks.add(book);
        System.out.println("[BAŞARILI] : " + book.getTitle() + " eklendi.");
    }

    // Ödünç alınan kitabı üye listesinden çıkaran metot
    public void removeBorrowedBook(Book book) {
        if (borrowedBooks.isEmpty()) {
            System.out.println("[UYARI] : " + reader.getName() + " isimli üyenin üzerinde ödünç alınmış hiçbir kitap yok!");
            return;
        }
        if (this.borrowedBooks.remove(book)) {
            System.out.println("[BAŞARILI] : '" + book.getTitle() + "' kitabı " + reader.getName() + " üzerinden başarıyla düşüldü.");
        } else {
            System.out.println("[HATA] : '" + book.getTitle() + "' isimli kitap " + reader.getName() + " isimli üyenin elinde görünmüyor!");
        }
    }

    // Üyenin üzerindeki kitapları gösteren metot
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

    public Set<Book> getBorrowedBooks() {
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
