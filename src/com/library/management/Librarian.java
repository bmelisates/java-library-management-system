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

    // 1. searchBook: Kütüphanecinin sistemde kitap araması
    public void searchBook(String title) {
        System.out.println("[GÖREVLİ] : " + name + ", \"" + title + "\" isimli kitabı sistemde arıyor...");
        // 🎯 Geliştirmek istersen ileride burayı şuna bağlayabilirsin: library.searchBook(title);
    }

    // 2. verifyMember: Üyenin kütüphaneye kayıtlı olup olmadığını doğrular
    public boolean verifyMember(String memberId) {
        System.out.println("[GÖREVLİ] : " + name + ", " + memberId + " kimlikli üyeyi doğruluyor...");
        return library.getMembers().containsKey(memberId);
    }

    // 3. issueBook: Kitap ödünç verme işlemini başlatır
    public void issueBook(MemberRecord member, Book book) {
        System.out.println("[GÖREVLİ] : " + name + ", " + member.getReader().getName() + " için ödünç işlemini onaylıyor.");
        library.lendBook(member, book);
    }

    // 4. calculateFine: Geciken günler için cezayı hesaplar
    public double calculateFine(MemberRecord member) {
        System.out.println("[GÖREVLİ] : " + name + ", " + member.getReader().getName() + " için ceza hesaplıyor...");
        return 0.0;
    }

    // 5. createBill: Ceza miktarından resmi fatura/makbuz üretir
    public void createBill(MemberRecord member, double amount) {
        System.out.println("[GÖREVLİ] : " + name + ", " + member.getReader().getName() + " adına " + amount + " TL değerinde fatura kesiyor.");
    }

    // 6. returnBook: Kitap iade alma işlemini başlatır
    public void returnBook(MemberRecord member, Book book) {
        System.out.println("[GÖREVLİ] : " + name + ", " + member.getReader().getName() + " isimli üyeden kitabı teslim alıyor.");
        // 🔑 DÜZELTME: returnBook yerine takeBackBook çağırıyoruz
        library.takeBackBook(member, book);
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