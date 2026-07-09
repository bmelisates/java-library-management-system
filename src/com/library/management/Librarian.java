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