package com.library.management;

import com.library.book.Book;
import com.library.member.MemberRecord;

public class Librarian {
    private String name;
    private String employeeId;
    private String password;
    private Library library; // Bağlı olduğu kütüphane sistemi

    public Librarian(String name, String employeeId, String password, Library library) {
        this.name = name;
        this.employeeId = employeeId;
        this.password = password;
        this.library = library;
    }

    // Üyenin kütüphaneye kayıtlı olup olmadığını doğrular
    public boolean verifyMember(String memberId) {
        System.out.println("[GÖREVLİ] : " + name + ", " + memberId + " kimlikli üyeyi doğruluyor...");
        return library.getMembers().containsKey(memberId);
    }

    // Kitap satın alma (yetkili operasyon)
    public void purchaseBook(Book book) {
        System.out.println("[GÖREVLİ] : " + name + " yeni kitap satın alıyor: " + book.getTitle());
        library.addBook(book);
    }

    // Kitap ödünç verme işlemini başlatır
    public void issueBook(MemberRecord member, Book book) {
        System.out.println("[GÖREVLİ] : " + name + ", " + member.getReader().getName() + " için ödünç işlemini onaylıyor.");
        library.lendBook(member, book);
    }

    // Kitap iade alma
    public boolean takeBackBook(MemberRecord member, Book book) {
        System.out.println("[GÖREVLİ] : " + name + ", " + member.getReader().getName() + "'den kitap iade alıyor: " + book.getTitle());
        return library.takeBackBook(member, book);
    }

    // Kitap güncelleme (yetkili operasyon)
    public void updateBook(int bookId, double newPrice, String newEdition) {
        System.out.println("[GÖREVLİ] : " + name + " kitap güncelliyor: ID " + bookId);
        library.updateBook(bookId, newPrice, newEdition);
    }

    // Kitap silme (yetkili operasyon)
    public void deleteBook(int bookId) {
        System.out.println("[GÖREVLİ] : " + name + " kitap siliyor: ID " + bookId);
        library.deleteBook(bookId);
    }

    // Kitap silme (nesne ile - yetkili operasyon)
    public void deleteBook(Book book) {
        System.out.println("[GÖREVLİ] : " + name + " kitap siliyor: " + book.getTitle());
        library.deleteBook(book);
    }


    // Getter ve Setterlar
    public String getName() {
        return name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}